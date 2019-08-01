/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.data

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.api.ApiService
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.StopCode
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.settings.data.AppSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class TripsRepository @Inject constructor(
    private val database: TransitDatabase,
    private val apiService: ApiService,
    private val settings: AppSettings,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {
    private val cachedResults: MutableMap<StopCode, ConflatedBroadcastChannel<Result<ApiResponse>>> = mutableMapOf()
    private val cachedStopCodes: MutableMap<StopId, StopCode> = mutableMapOf()

    suspend fun getResultCache(stopId: StopId): ConflatedBroadcastChannel<Result<ApiResponse>> {
        val stopCode = cachedStopCodes.getOrPut(
            stopId, {
                withContext(dispatcherProvider.io) { database.stopQueries.getStopById(stopId).executeAsOne().code }
            }
        )

        return cachedResults.getOrPut(stopCode, { ConflatedBroadcastChannel() })
    }

    fun clearCacheFor(stopId: StopId) {
        cachedResults.remove(cachedStopCodes[stopId])
    }

    suspend fun getStop(stopId: StopId): Result<Stop> = withContext(dispatcherProvider.io) {
        try {
            val stop = database.stopQueries.getStopById(stopId).executeAsOne()

            // Cache the stop code for other uses
            if (!cachedStopCodes.containsKey(stopId)) {
                cachedStopCodes[stopId] = stop.code
            }

            Result.Success(stop)
        } catch (e: Exception) {
            Result.Error<Stop>(e)
        }
    }

    suspend fun getTrips(stopId: StopId): Result<Unit> = withContext(dispatcherProvider.io) {
        try {
            val stopCode =
                cachedStopCodes.getOrPut(stopId, { database.stopQueries.getStopById(stopId).executeAsOne().code })

            val channel = cachedResults.getOrPut(stopCode, { ConflatedBroadcastChannel() })

            channel.offer(Result.Loading(channel.valueOrNull?.data))
            try {
                val result = apiService.getTrips(stopCode.value)
                channel.offer(Result.Success(result))
            } catch (e: Exception) {
                channel.offer(Result.Error(e, channel.valueOrNull?.data))
            }

            Result.Success(Unit)
        } catch (e: IOException) {
            Result.Error<Unit>(e)
        }
    }

    fun setGroupByRoute(group: Boolean) {
        settings.groupByRoute.value = group
        rebroadcast()
    }

    fun getGroupByRoute(): Boolean = settings.groupByRoute.value

    /**
     * Rebroadcasts the last response
     */
    private fun rebroadcast() {
        cachedResults.forEach { (_, broadcastChannel) -> broadcastChannel.offer(broadcastChannel.value) }
    }

    companion object {
        const val KEY_GROUP_BY_ROUTE = "KEY_GROUP_BY_ROUTE"
    }
}
/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import ca.llamabagel.transpo.data.api.TripsService
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.StopCode
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.models.trips.ApiResponse
import kotlinx.coroutines.Dispatchers
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
    private val apiService: TripsService,
    private val sharedPreferences: SharedPreferences
) {
    private val cachedResults: MutableMap<StopCode, ConflatedBroadcastChannel<ApiResponse>> = mutableMapOf()
    private val cachedStopCodes: MutableMap<StopId, StopCode> = mutableMapOf()

    suspend fun getResultCache(stopId: StopId): ConflatedBroadcastChannel<ApiResponse> = withContext(Dispatchers.IO) {
        val stopCode =
            cachedStopCodes.getOrPut(stopId, { database.stopQueries.getStopById(stopId).executeAsOne().code })

        cachedResults.getOrPut(stopCode, { ConflatedBroadcastChannel() })
    }

    fun clearCacheFor(stopId: StopId) {
        cachedStopCodes[stopId]?.let(cachedResults::remove)
    }

    suspend fun getStop(stopId: StopId): Result<Stop> = withContext(Dispatchers.IO) {
        try {
            val stop = database.stopQueries.getStopById(stopId).executeAsOne()

            // Cache the stop code for other uses
            if (!cachedStopCodes.containsKey(stopId)) {
                cachedStopCodes[stopId] = stop.code
            }

            Result.Success(stop)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getTrips(stopId: StopId): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val stopCode =
                cachedStopCodes.getOrPut(stopId, { database.stopQueries.getStopById(stopId).executeAsOne().code })

            cachedResults.getOrPut(stopCode, { ConflatedBroadcastChannel() })
                .offer(apiService.getTrips(stopCode.value).await())
            Result.Success(Unit)
        } catch (e: IOException) {
            Result.Error(e)
        }
    }

    @SuppressLint("ApplySharedPref")
    suspend fun setGroupByRoute(group: Boolean) = withContext(Dispatchers.IO) {
        sharedPreferences.edit().putBoolean(KEY_GROUP_BY_ROUTE, group).commit()
        rebroadcast()
    }

    suspend fun getGroupByRoute(): Boolean =
        withContext(Dispatchers.IO) { sharedPreferences.getBoolean(KEY_GROUP_BY_ROUTE, false) }

    /**
     * Rebroadcasts the last response
     */
    private fun rebroadcast() {
        cachedResults.forEach { (_, v) ->
            v.offer(v.value)
        }
    }

    companion object {
        const val KEY_GROUP_BY_ROUTE = "KEY_GROUP_BY_ROUTE"
    }
}
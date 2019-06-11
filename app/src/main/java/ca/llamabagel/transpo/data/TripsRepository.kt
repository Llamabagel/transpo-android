/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import ca.llamabagel.transpo.data.api.TripsService
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.models.trips.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalCoroutinesApi
class TripsRepository @Inject constructor(
    private val database: TransitDatabase,
    private val apiService: TripsService,
    private val sharedPreferences: SharedPreferences
) {
    private val cachedResults: MutableMap<String, ConflatedBroadcastChannel<ApiResponse>> = mutableMapOf()

    fun getResultCache(stopCode: String): ConflatedBroadcastChannel<ApiResponse> =
        cachedResults.getOrPut(stopCode, { ConflatedBroadcastChannel() })

    fun clearCacheFor(stopCode: String) {
        cachedResults.remove(stopCode)
    }

    suspend fun getStop(stopId: String): Result<Stop> = withContext(Dispatchers.IO) {
        try {
            val stop = database.stopQueries.getStopById(stopId).executeAsOne()
            Result.Success(stop)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getTrips(stopCode: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            cachedResults.getOrPut(stopCode, { ConflatedBroadcastChannel() })
                .offer(apiService.getTrips(stopCode).await())
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
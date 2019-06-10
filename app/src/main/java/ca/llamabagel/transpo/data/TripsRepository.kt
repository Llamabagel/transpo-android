/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.data.api.TripsService
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.models.trips.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val apiService: TripsService
) {
    val cachedResult = ConflatedBroadcastChannel<ApiResponse>()

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
            cachedResult.offer(apiService.getTrips(stopCode).await())
            Result.Success(Unit)
        } catch (e: IOException) {
            Result.Error(e)
        }
    }
}
/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.data.api.TripsService
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.models.trips.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TripsRepository @Inject constructor(
    private val database: TransitDatabase,
    private val apiService: TripsService
) {

    suspend fun getStop(stopId: String): Stop? = withContext(Dispatchers.IO) {
        database.stopQueries.getStopById(stopId).executeAsOneOrNull()
    }

    suspend fun getTrips(stopCode: String): Result<ApiResponse> = withContext(Dispatchers.IO) {
        try {
            Result.Success(apiService.getTrips(stopCode))
        } catch (e: IOException) {
            Result.Error(e)
        }
    }
}
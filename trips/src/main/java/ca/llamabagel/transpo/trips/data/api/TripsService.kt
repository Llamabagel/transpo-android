/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.data.api

import ca.llamabagel.transpo.models.trips.ApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.POST
import retrofit2.http.Path

interface TripsService {

    @POST("/trips/{code}")
    fun getTrips(@Path("code") stopCode: String): Deferred<ApiResponse>
}
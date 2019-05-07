/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.api

import ca.llamabagel.transpo.models.trips.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("trips/{code}/")
    suspend fun getTrips(@Path("code") stopCode: String): ApiResponse
}
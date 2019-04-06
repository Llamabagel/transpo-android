/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.api

import ca.llamabagel.transpo.models.app.DataPackage
import ca.llamabagel.transpo.models.trips.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("trips")
    suspend fun getTrips(@Query("stop") stopCode: String): ApiResponse

    @GET("data")
    suspend fun getData(@Query("current") currentVersion: String): DataPackage

}
/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.api

import ca.llamabagel.transpo.models.app.AppMetadata
import ca.llamabagel.transpo.models.app.DataPackage
import ca.llamabagel.transpo.models.app.MetadataRequest
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.models.updates.LiveUpdate
import retrofit2.http.*

interface ApiService {
    @GET("trips/{code}")
    suspend fun getTrips(@Path("code") stopCode: String): ApiResponse

    @GET("data/android/1/")
    suspend fun getDataPackage(): DataPackage

    @POST("data/metadata/")
    suspend fun getMetadata(@Body request: MetadataRequest): AppMetadata

    @GET("feed")
    suspend fun getLiveUpdates(@Query("lang") language: String = "en"): List<LiveUpdate>
}
/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.api

import ca.llamabagel.transpo.models.app.AppMetadata
import ca.llamabagel.transpo.models.app.DataPackage
import ca.llamabagel.transpo.models.app.MetadataRequest
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DataService {
    @GET("data/android/1/")
    fun getDataPackage(): Deferred<DataPackage>

    @POST("data/metadata/")
    fun getMetadata(@Body request: MetadataRequest): Deferred<AppMetadata>
}
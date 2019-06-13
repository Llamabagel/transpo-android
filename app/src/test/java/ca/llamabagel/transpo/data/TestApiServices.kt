/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.data.api.TripsService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit

fun createTestTripsService(mockWebServer: MockWebServer): TripsService = Retrofit.Builder()
    .baseUrl(mockWebServer.url("/"))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
    .build()
    .create(TripsService::class.java)
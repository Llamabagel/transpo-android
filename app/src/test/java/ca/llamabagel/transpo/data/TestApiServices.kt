/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.data.api.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit

fun createTestTripsService(mockWebServer: MockWebServer): ApiService = Retrofit.Builder()
    .baseUrl(mockWebServer.url("/"))
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .build()
    .create(ApiService::class.java)
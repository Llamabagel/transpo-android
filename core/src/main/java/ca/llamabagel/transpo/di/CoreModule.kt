/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Converter
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    fun provideCallAdapterFactory(): CoroutineCallAdapterFactory = CoroutineCallAdapterFactory()

    @Provides
    @Singleton
    fun provideSerializationConverterFactory(): Converter.Factory = Json.asConverterFactory(MediaType.get("application/json"))

}
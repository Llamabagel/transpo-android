/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.Context
import androidx.annotation.StringRes
import ca.llamabagel.transpo.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    fun provideCallAdapterFactory(): CoroutineCallAdapterFactory = CoroutineCallAdapterFactory()

    @Provides
    @Singleton
    fun provideSerializationConverterFactory(): Converter.Factory =
        Json.asConverterFactory(MediaType.get("application/json"))

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    @Singleton
    fun provideStringsProvider(context: Context): StringsGen = object : StringsGen {
        override fun get(@StringRes strResId: Int) = context.getString(strResId)
    }

    @Provides
    fun geocoderProvider(): GeocodingWrapper = object : GeocodingWrapper {
        override fun getAutocompleteResults(
            query: String,
            minLongitude: Double,
            minLatitude: Double,
            maxLongitude: Double,
            maxLatitude: Double
        ): List<CarmenFeature> = MapboxGeocoding.builder()
            .accessToken(BuildConfig.MAPBOX_KEY)
            .query(query)
            .bbox(minLongitude, minLatitude, maxLongitude, maxLatitude)
            .build()
            .executeCall()
            .body()
            ?.features()
            .orEmpty()
    }
}
/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.Context
import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.annotation.StringRes
import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.data.api.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    @Singleton
    fun provideSerializationConverterFactory(): Converter.Factory =
        Json.asConverterFactory("application/json".toMediaType())

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
    fun provideStringsProvider(context: Context): StringUtils = object : StringUtils {
        override fun get(@StringRes strResId: Int) = context.getString(strResId)

        override fun bold(str: String, matching: String): CharSequence {
            val matchStartIndex = str.indexOf(matching, ignoreCase = true)
            val matchEndIndex = matchStartIndex + matching.length

            return SpannableStringBuilder(str).apply {
                if (matchStartIndex != -1 && matching.isNotEmpty()) {
                    setSpan(StyleSpan(BOLD), matchStartIndex, matchEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }

    @Provides
    fun provideGeocoder(): GeocodingWrapper = object : GeocodingWrapper {
        override fun getAutocompleteResults(
            query: String,
            minLongitude: Double,
            minLatitude: Double,
            maxLongitude: Double,
            maxLatitude: Double,
            limit: Int
        ): List<CarmenFeature> = MapboxGeocoding.builder()
            .accessToken(BuildConfig.MAPBOX_KEY)
            .query(query)
            .bbox(minLongitude, minLatitude, maxLongitude, maxLatitude)
            .limit(limit)
            .build()
            .executeCall()
            .body()
            ?.features()
            .orEmpty()
    }

    @Provides
    @Singleton
    fun provideTripsService(
        converter: Converter.Factory,
        okHttpClient: OkHttpClient
    ): ApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.API_ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(converter)
        .build().create(ApiService::class.java)
}
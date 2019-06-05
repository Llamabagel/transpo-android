/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.data.TripsRepository
import ca.llamabagel.transpo.data.api.TripsService
import ca.llamabagel.transpo.data.db.TransitDatabase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

@Module
abstract class TripsModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideTripsService(
            converter: Converter.Factory,
            okHttpClient: OkHttpClient
        ): TripsService = Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(converter)
            .build().create(TripsService::class.java)

        @Provides
        @JvmStatic
        fun provideTripsRepository(transitDatabase: TransitDatabase, tripsService: TripsService) =
            TripsRepository(transitDatabase, tripsService)
    }
}
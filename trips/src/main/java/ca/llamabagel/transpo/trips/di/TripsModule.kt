/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.di

import ca.llamabagel.transpo.core.BuildConfig
import ca.llamabagel.transpo.trips.data.TripsRepository
import ca.llamabagel.transpo.trips.data.api.TripsService
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.di.scope.FeatureScope
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit

@Module
class TripsModule {

    @Provides
    @FeatureScope
    fun provideTripsService(adapter: CoroutineCallAdapterFactory, converter: Converter.Factory) = Retrofit.Builder()
        .baseUrl(BuildConfig.API_ENDPOINT)
        .addCallAdapterFactory(adapter)
        .addConverterFactory(converter)
        .build().create(TripsService::class.java)

    @Provides
    @FeatureScope
    fun provideTripsRepository(transitDatabase: TransitDatabase, tripsService: TripsService) =
        TripsRepository(transitDatabase, tripsService)

}
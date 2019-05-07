/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.SharedPreferences
import ca.llamabagel.transpo.core.BuildConfig
import ca.llamabagel.transpo.di.scope.FeatureScope
import ca.llamabagel.transpo.data.DataRepository
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.data.api.DataService
import ca.llamabagel.transpo.data.db.TransitDatabase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit

@Module
class DataModule {

    @Provides
    @FeatureScope
    fun provideDataRepository(
        dataService: DataService,
        database: TransitDatabase,
        localMetadataSource: LocalMetadataSource
    ) = DataRepository(dataService, database, localMetadataSource)

    @Provides
    @FeatureScope
    fun provideDataService(adapter: CoroutineCallAdapterFactory, converter: Converter.Factory): DataService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addCallAdapterFactory(adapter)
            .addConverterFactory(converter)
            .build()
            .create(DataService::class.java)
    }

    @Provides
    @FeatureScope
    fun provideLocalMetadataSource(sharedPreferences: SharedPreferences)
        = LocalMetadataSource(sharedPreferences)

}
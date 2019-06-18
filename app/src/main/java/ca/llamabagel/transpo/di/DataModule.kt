/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.SharedPreferences
import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.data.DataRepository
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.data.api.DataService
import ca.llamabagel.transpo.data.db.TransitDatabase
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit

@Module
class DataModule {

    @Provides
    fun provideDataRepository(
        dataService: DataService,
        database: TransitDatabase,
        localMetadataSource: LocalMetadataSource
    ) = DataRepository(dataService, database, localMetadataSource)

    @Provides
    fun provideDataService(converter: Converter.Factory): DataService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(converter)
            .build()
            .create(DataService::class.java)
    }

    @Provides
    fun provideLocalMetadataSource(sharedPreferences: SharedPreferences) =
        LocalMetadataSource(sharedPreferences)
}
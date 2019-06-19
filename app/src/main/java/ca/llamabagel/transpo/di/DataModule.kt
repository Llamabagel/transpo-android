/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.SharedPreferences
import ca.llamabagel.transpo.transit.data.DataRepository
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.data.api.ApiService
import ca.llamabagel.transpo.data.db.TransitDatabase
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideDataRepository(
        apiService: ApiService,
        database: TransitDatabase,
        localMetadataSource: LocalMetadataSource
    ) = DataRepository(apiService, database, localMetadataSource)

    @Provides
    fun provideLocalMetadataSource(sharedPreferences: SharedPreferences) =
        LocalMetadataSource(sharedPreferences)
}
/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.Context
import android.content.SharedPreferences
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.settings.data.AppSettingsProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
open class SharedPreferencesModule {
    @Provides
    @Singleton
    @Named(LocalMetadataSource.METADATA_PREF)
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(LocalMetadataSource.METADATA_PREF, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @Named(AppSettingsProvider.SETTINGS_PREF)
    fun provideSettingsSharedPreferences(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(AppSettingsProvider.SETTINGS_PREF, Context.MODE_PRIVATE)
}
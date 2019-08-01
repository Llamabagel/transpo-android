/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.Context
import android.content.SharedPreferences
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.settings.data.AppSettings
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
    @Named(AppSettings.SETTINGS_PREF)
    fun provideSettingsSharedPreferences(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(AppSettings.SETTINGS_PREF, Context.MODE_PRIVATE)
}
/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.Context
import android.content.SharedPreferences
import ca.llamabagel.transpo.di.scope.FeatureScope
import dagger.Module
import dagger.Provides

@Module
open class SharedPreferencesModule(val context: Context, val name: String) {

    @Provides
    @FeatureScope
    fun provideSharedPreferences(): SharedPreferences {
        return context.applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

}
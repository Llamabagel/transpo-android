/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import androidx.work.WorkManager
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.di.*
import javax.inject.Inject

class TranspoApplication : Application(), DaggerComponentProvider {

    override val component: CoreComponent by lazy {
        DaggerCoreComponent.builder()
            .applicationContext(applicationContext)
            .sharedPreferenceModule(SharedPreferencesModule(LocalMetadataSource.METADATA_PREF))
            .build()
    }

    @Inject
    lateinit var workerFactory: InjectionWorkerFactory

    @Suppress("MagicNumber")
    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        // val workerFactory = DaggerApplicationComponent.create()
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(workerFactory).build())

        if (android.os.Build.VERSION.SDK_INT >= 29) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }
    }
}
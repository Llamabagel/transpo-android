/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo

import android.app.Activity
import android.app.Application
import android.content.Context
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

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        //val workerFactory = DaggerApplicationComponent.create()
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(workerFactory).build())
    }
}

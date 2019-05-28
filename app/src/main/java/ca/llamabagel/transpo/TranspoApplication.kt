/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.di.DaggerComponentProvider
import ca.llamabagel.transpo.di.CoreComponent
import ca.llamabagel.transpo.di.DaggerCoreComponent
import ca.llamabagel.transpo.di.SharedPreferencesModule
import ca.llamabagel.transpo.di.InjectionWorkerFactory
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
        // val workerFactory = DaggerApplicationComponent.create()
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(workerFactory).build())
    }
}

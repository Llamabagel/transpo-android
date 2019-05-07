/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import ca.llamabagel.transpo.di.CoreComponent
import ca.llamabagel.transpo.di.DaggerCoreComponent
import ca.llamabagel.transpo.di.InjectionWorkerFactory
import ca.llamabagel.transpo.di.inject
import javax.inject.Inject

class TranspoApplication : Application() {

    @Inject
    lateinit var workerFactory: InjectionWorkerFactory

    val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.create()
    }

    companion object {
        @JvmStatic
        fun coreComponent(context: Context) = (context.applicationContext as TranspoApplication).coreComponent
    }

    override fun onCreate() {
        super.onCreate()
        inject(this)
        //val workerFactory = DaggerApplicationComponent.create()
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(workerFactory).build())
    }
}

fun Activity.coreComponent() = TranspoApplication.coreComponent(this)
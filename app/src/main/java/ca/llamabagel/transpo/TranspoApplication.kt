/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo

import android.app.Activity
import android.app.Application
import android.content.Context
import ca.llamabagel.transpo.core.di.CoreComponent
import ca.llamabagel.transpo.core.di.DaggerCoreComponent

class TranspoApplication : Application() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.create()
    }

    companion object {
        @JvmStatic
        fun coreComponent(context: Context) = (context.applicationContext as TranspoApplication).coreComponent
    }

}

fun Activity.coreComponent() = TranspoApplication.coreComponent(this)
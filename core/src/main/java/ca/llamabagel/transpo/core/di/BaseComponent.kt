/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.core.di

import android.app.Activity
import android.app.Service

interface BaseComponent<T> {
    fun inject(target: T)
}

interface BaseActivityComponent<T : Activity> : BaseComponent<T>

interface BaseServiceComponent<T : Service> : BaseComponent<T>
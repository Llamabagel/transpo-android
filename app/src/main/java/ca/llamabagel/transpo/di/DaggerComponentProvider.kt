/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.app.Activity

interface DaggerComponentProvider {
    val component: CoreComponent
}

val Activity.injector get() = (application as DaggerComponentProvider).component

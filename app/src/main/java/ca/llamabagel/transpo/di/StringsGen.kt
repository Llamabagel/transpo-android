/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import androidx.annotation.StringRes

interface StringsGen {
    fun get(@StringRes strResId: Int): String
}
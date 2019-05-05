/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Activity.startActivity(context: Context) {
    startActivity(Intent(context, T::class.java))
}

inline val Any.TAG: String get() = this::class.java.simpleName

object Actions {
    fun openTripsIntent(context: Context, id: String): Intent =
            makeIntent(context, "ca.llamabagel.transpo.actions.trips.open")
                .putExtra("stop_id", id)

    private fun makeIntent(context: Context, action: String)
        = Intent(action).setPackage(context.packageName)
}
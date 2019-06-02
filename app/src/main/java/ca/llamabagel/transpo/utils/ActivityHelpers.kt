/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import ca.llamabagel.transpo.BuildConfig

inline fun <reified T : Activity> Activity.startActivity(context: Context, extras: Intent.() -> Unit = {}) {
    val intent = Intent(context, T::class.java).apply(extras)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivityForResult(
    context: Context,
    requestCode: Int,
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(context, T::class.java).apply(extras)
    startActivityForResult(intent, requestCode)
}

inline val Any.TAG: String get() = this::class.java.simpleName

private const val PACKAGE_NAME = "ca.llamabagel.transpo"

fun intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
        if (BuildConfig.DEBUG) "$PACKAGE_NAME.debug" else PACKAGE_NAME,
        addressableActivity.className
    )
}

/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {

    /**
     * The activity class name.
     */
    val className: String
}

object Activities {

    // TripsActivity
    object Trips : AddressableActivity {
        override val className: String = "$PACKAGE_NAME.trips.ui.TripsActivity"

        const val EXTRA_STOP_ID = "EXTRA_STOP_ID"
    }
}
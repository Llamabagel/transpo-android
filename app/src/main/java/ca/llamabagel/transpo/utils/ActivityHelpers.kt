/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

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

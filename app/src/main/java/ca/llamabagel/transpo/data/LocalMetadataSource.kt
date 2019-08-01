/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Named

class LocalMetadataSource @Inject constructor(@Named(METADATA_PREF) private val prefs: SharedPreferences) {

    private var _dataVersion: String? = prefs.getString(KEY_DATA_VERSION, null)

    var dataVersion: String? = _dataVersion
        set(value) {
            prefs.edit { putString(KEY_DATA_VERSION, value) }
            field = value
        }

    companion object {
        const val METADATA_PREF = "metadata"
        private const val KEY_DATA_VERSION = "KEY_DATA_VERSION"
    }
}
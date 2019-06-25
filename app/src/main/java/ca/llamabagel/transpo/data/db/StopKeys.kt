/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.db

inline class StopId(val value: String) {
    companion object {
        // A kind of workaround due "lateinit" being disallowed on inline classes
        val DEFAULT = StopId("")
    }
}
inline class StopCode(val value: String)
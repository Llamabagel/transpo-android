/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import ca.llamabagel.transpo.di.StringUtils

class FakeStringUtils : StringUtils {
    override fun get(strResId: Int): String = strResId.toString()
    override fun bold(str: String, matching: String): CharSequence = str
}
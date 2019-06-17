/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import ca.llamabagel.transpo.di.StringsGen


class FakeStringsGen : StringsGen {
    override fun get(strResId: Int): String = strResId.toString()
}
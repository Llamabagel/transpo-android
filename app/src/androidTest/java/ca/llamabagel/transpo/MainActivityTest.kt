/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ca.llamabagel.transpo.utils.findViewById
import ca.llamabagel.transpo.utils.verifyDisplayed
import ca.llamabagel.transpo.ui.home.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun basicTest() {
        findViewById(R.id.openButton).verifyDisplayed()
    }
}
/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ca.llamabagel.transpo.search.ui.SearchActivity
import ca.llamabagel.transpo.utils.findViewById
import ca.llamabagel.transpo.utils.verifyDisplayed
import ca.llamabagel.transpo.utils.verifyFocused
import ca.llamabagel.transpo.utils.verifyNotFocused
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule(SearchActivity::class.java)

    @Test
    fun basicTest() {
        findViewById(R.id.search_bar).verifyDisplayed()
    }

    @Test
    fun keyboard_shown_on_activity_launch() {
        findViewById(R.id.search_bar).verifyFocused()
    }

    @Test
    fun press_back_loses_search_bar_focus() {
        Espresso.pressBack()

        findViewById(R.id.search_bar).verifyNotFocused()
    }
}
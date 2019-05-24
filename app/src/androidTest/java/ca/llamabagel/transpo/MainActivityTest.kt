/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ca.llamabagel.transpo.utils.findViewById
import ca.llamabagel.transpo.utils.verifyDisplayed
import ca.llamabagel.transpo.ui.home.MainActivity
import ca.llamabagel.transpo.utils.click
import ca.llamabagel.transpo.utils.findViewByText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val SAVED = "SavedFragment"
private const val MAP = "MapFragment"
private const val PLANNER = "PlannerFragment"

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var rule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun home_menu_is_displayed() {
        findViewById(R.id.home).verifyDisplayed()
    }

    @Test
    fun saved_menu_is_displayed() {
        findViewById(R.id.saved).verifyDisplayed()
    }

    @Test
    fun map_menu_is_displayed() {
        findViewById(R.id.map).verifyDisplayed()
    }

    @Test
    fun planner_menu_is_displayed() {
        findViewById(R.id.planner).verifyDisplayed()
    }

    @Test
    fun home_fragment_is_displayed() {
        findViewById(R.id.home).click()
        findViewById(R.id.openButton).verifyDisplayed()
    }

    @Test
    fun saved_fragment_is_displayed() {
        findViewById(R.id.saved).click()
        findViewByText(SAVED).verifyDisplayed()
    }

    @Test
    fun map_fragment_is_displayed() {
        findViewById(R.id.map).click()
        findViewByText(MAP).verifyDisplayed()
    }

    @Test
    fun planner_fragment_is_displayed() {
        findViewById(R.id.planner).click()
        findViewByText(PLANNER).verifyDisplayed()
    }
}
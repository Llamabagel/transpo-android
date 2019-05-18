/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ca.llamabagel.transpo.test_shared.utils.findViewById
import ca.llamabagel.transpo.test_shared.utils.verifyDisplayed
import ca.llambagel.transpo.search.R
import ca.llambagel.transpo.search.ui.SearchActivity
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
        findViewById(R.id.searchTextView).verifyDisplayed()
    }
}
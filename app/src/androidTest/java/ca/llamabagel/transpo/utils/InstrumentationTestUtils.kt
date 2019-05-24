/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.not

fun findViewById(id: Int): ViewInteraction = Espresso.onView(ViewMatchers.withId(id))
fun findViewByText(text: String) = Espresso.onView(ViewMatchers.withText(text))
fun findViewByText(strRes: Int) = Espresso.onView(ViewMatchers.withText(strRes))

fun ViewInteraction.click() = perform(ViewActions.click())
fun ViewInteraction.typeText(textToBeTyped: String) = perform(ViewActions.typeText(textToBeTyped))

fun ViewInteraction.verifyDisplayed(): ViewInteraction = check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
fun ViewInteraction.verifyFocused(): ViewInteraction = check(ViewAssertions.matches(ViewMatchers.hasFocus()))
fun ViewInteraction.verifyNotFocused(): ViewInteraction = check(ViewAssertions.matches(not(ViewMatchers.hasFocus())))
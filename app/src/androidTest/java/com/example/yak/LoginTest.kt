package com.example.yak

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test


class LoginTest {
    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)
    private val validUsername = "testname"
    private val validPassword = "thisisatest"

    @Test
    fun user_can_type_username() {
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText(validUsername))
        Espresso.pressBack()
        Espresso.onView(withId(R.id.username)).check(matches(
            ViewMatchers.withText(
                Matchers.containsString(validUsername)
            )
        ))
    }

    @Test
    fun user_can_type_password() {
        Espresso.onView(withId(R.id.password)).perform(ViewActions.typeText(validPassword))
        Espresso.pressBack()
        Espresso.onView(withId(R.id.password)).check(matches(
            ViewMatchers.withText(
                Matchers.containsString(validPassword)
            )
        ))
    }

    @Test
    fun user_can_click_register() {
        Espresso.onView(withId(R.id.signIn)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.signIn)).perform(click())
    }
}
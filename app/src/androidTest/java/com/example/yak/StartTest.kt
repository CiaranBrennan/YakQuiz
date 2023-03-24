package com.example.yak

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


class StartTest {
    @get:Rule
    val activityRule = ActivityTestRule(StartActivity::class.java)

    @Test
    fun user_can_click_login() {
        Espresso.onView(withId(R.id.login)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.login)).perform(click())
    }

    @Test
    fun user_can_click_register() {
        Espresso.onView(withId(R.id.register)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.register)).perform(click())
    }

    @Test
    fun user_can_click_skip() {
        Espresso.onView(withId(R.id.skip)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.skip)).perform(click())
    }
}
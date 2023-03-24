package com.example.yak

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


class LeaderboardTest {
    @get:Rule
    val activityRule = ActivityTestRule(LeaderboardActivity::class.java)

    @Test
    fun user_can_click_back() {
        Espresso.onView(withId(R.id.backButton)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.backButton)).perform(click())
    }
}
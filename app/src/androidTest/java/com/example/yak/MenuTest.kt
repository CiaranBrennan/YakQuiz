package com.example.yak

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


class MenuTest {
    @get:Rule
    val activityRule = ActivityTestRule(MenuActivity::class.java)

    @Test
    fun user_can_click_choose_quiz() {
        Espresso.onView(withId(R.id.chooseQuiz)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.chooseQuiz)).perform(click())
    }

    @Test
    fun user_can_click_leaderboard() {
        Espresso.onView(withId(R.id.leaderboard)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.leaderboard)).perform(click())
    }

    @Test
    fun user_can_click_quit() {
        Espresso.onView(withId(R.id.quit)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.quit)).perform(click())
    }
}
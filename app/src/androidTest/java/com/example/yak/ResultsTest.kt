package com.example.yak

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class ResultsTest {
    val testScore: Int = 10
    val testDictionary: HashMap<String, HashMap<String, Int>> = hashMapOf()

    @get:Rule
    val activityRule: ActivityTestRule<ResultsActivity> = object : ActivityTestRule<ResultsActivity>(ResultsActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext: Context = ApplicationProvider.getApplicationContext()
            val intent = Intent(targetContext, ResultsActivity::class.java)
            intent.putExtra("score", testScore)
            intent.putExtra("questionScores", testDictionary)
            return intent
        }
    }

    @Test
    fun user_can_click_play_again() {
        Espresso.onView(withId(R.id.replay)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.replay)).perform(click())
    }

    @Test
    fun user_can_click_main_menu() {
        Espresso.onView(withId(R.id.menu)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.menu)).perform(click())
    }

    @Test
    fun user_can_click_share() {
        Espresso.onView(withId(R.id.share)).check(
            matches(
                isClickable()
            )
        )
        Espresso.onView(withId(R.id.share)).perform(click())
    }

    @Test
    fun score_updated_to_correct_value() {
        Espresso.onView(withId(R.id.scoreText)).perform(click())
        Espresso.onView(withId(R.id.scoreText)).check(matches(withText(testScore.toString())))
    }
}
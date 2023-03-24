package com.example.yak

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class QuizSelectionTest {
    @get:Rule
    val activityRule = ActivityTestRule(QuizSelectionActivity::class.java)
    private val categoryString: String = "General Knowledge"
    private val difficultyString: String = "Medium"
    private val typeString: String = "True/False"

    @Test
    fun user_can_enter_number_of_questions() {
        Espresso.onView(withId(R.id.numberOfQuestionsInput)).perform(typeText("50"))
        Espresso.pressBack()
        Espresso.onView(withId(R.id.numberOfQuestionsInput)).check(matches(withText(containsString("50"))))
    }

    @Test
    fun user_can_select_category() {
        Espresso.onView(withId(R.id.categorySpinnerSelector)).perform(click())
        Espresso.onData(allOf(`is`(instanceOf(String::class.java)), `is`(categoryString))).perform(click())
        Espresso.onView(withId(R.id.categorySpinnerSelector)).check(matches(withSpinnerText(containsString(categoryString))))
    }

    @Test
    fun user_can_select_difficulty() {
        Espresso.onView(withId(R.id.difficultySpinnerSelector)).perform(click())
        Espresso.onData(allOf(`is`(instanceOf(String::class.java)), `is`(difficultyString))).perform(click())
        Espresso.onView(withId(R.id.difficultySpinnerSelector)).check(matches(withSpinnerText(containsString(difficultyString))))
    }

    @Test
    fun user_can_select_answer_type() {
        Espresso.onView(withId(R.id.answerTypeSpinnerSelector)).perform(click())
        Espresso.onData(allOf(`is`(instanceOf(String::class.java)), `is`(typeString))).perform(click())
        Espresso.onView(withId(R.id.answerTypeSpinnerSelector)).check(matches(withSpinnerText(containsString(typeString))))
    }

    @Test
    fun user_cannot_click_start_with_blank_number_of_questions() {
        Espresso.onView(withId(R.id.numberOfQuestionsInput)).perform(typeText(""))
//        Espresso.onView(ViewMatchers.withId(R.id.startQuizButton)).check(matches(isClickable()))
        Espresso.onView(withId(R.id.startQuizButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun user_can_click_start_with_valid_config() {
        Espresso.onView(withId(R.id.numberOfQuestionsInput)).perform(typeText("1"))
        Espresso.pressBack()
        Espresso.onView(withId(R.id.startQuizButton)).check(matches(isEnabled()))
        Espresso.onView(withId(R.id.startQuizButton)).perform(click())
    }

    @Test
    fun test_full_config_selection() {
        // Enter number of questions
        Espresso.onView(withId(R.id.numberOfQuestionsInput)).perform(typeText("50"))
        Espresso.pressBack()
        // Select category
        Espresso.onView(withId(R.id.categorySpinnerSelector)).perform(click())
        Espresso.onData(allOf(`is`(instanceOf(String::class.java)), `is`(categoryString))).perform(click())
        // Select difficulty
        Espresso.onView(withId(R.id.difficultySpinnerSelector)).perform(click())
        Espresso.onData(allOf(`is`(instanceOf(String::class.java)), `is`(difficultyString))).perform(click())
        // Select answer type
        Espresso.onView(withId(R.id.answerTypeSpinnerSelector)).perform(click())
        Espresso.onData(allOf(`is`(instanceOf(String::class.java)), `is`(typeString))).perform(click())
        // Click start quiz button
        Espresso.onView(withId(R.id.startQuizButton)).perform(click())
    }


}

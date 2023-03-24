package com.example.yak

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


class QuestionTest {
    val questionArrayString: String = "[{\"category\":\"General Knowledge\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"Area 51 is located in which US state?\",\"correct_answer\":\"Nevada\",\"incorrect_answers\":[\"Arizona\",\"New Mexico\",\"Utah\"]},{\"category\":\"General Knowledge\",\"type\":\"multiple\",\"difficulty\":\"hard\",\"question\":\"According to the 2014-2015 Australian Bureau of Statistics, what percentage of Australians were born overseas?\",\"correct_answer\":\"28%\",\"incorrect_answers\":[\"13%\",\"20%\",\"7%\"]}]"

    @get:Rule
    val activityRule: ActivityTestRule<QuestionActivity> = object : ActivityTestRule<QuestionActivity>(QuestionActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext: Context = ApplicationProvider.getApplicationContext()
            val intent = Intent(targetContext, QuestionActivity::class.java)
            intent.putExtra("questionArrayString", questionArrayString)
            return intent
        }
    }

    @Test
    fun user_can_click_option_one() {
        Espresso.onView(withId(R.id.optionOneButton)).perform(click())
    }

    @Test
    fun user_can_click_option_two() {
        Espresso.onView(withId(R.id.optionTwoButton)).perform(click())
    }

    @Test
    fun user_can_click_option_three() {
        Espresso.onView(withId(R.id.optionThreeButton)).perform(click())
    }

    @Test
    fun user_can_click_option_four() {
        Espresso.onView(withId(R.id.optionFourButton)).perform(click())
    }
}
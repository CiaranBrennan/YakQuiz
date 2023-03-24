package com.example.yak

// Used to unescape the json data (e.g. &quot; converts to ") for updating text

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_question.*
import org.apache.commons.text.StringEscapeUtils.unescapeHtml4
import org.json.JSONArray
import org.json.JSONObject


class QuestionActivity : AppCompatActivity(),  View.OnClickListener {
    private lateinit var questionsArray: JSONArray
    private lateinit var buttonArray: Array<Button>
    private lateinit var questionTimer: CountDownTimer
    private var questionEnded: Boolean = true
    private lateinit var buttonClicked: Button

    private val delayDefault: Long = 3000
    private val durationDefault: Long = 15000

    private var buttonClickedString: String = ""
    private var timeLeft: Long = durationDefault
    private var delayLeft: Long = delayDefault
    private lateinit var answerList: ArrayList<String>

    private lateinit var questionText: TextView
    private lateinit var categoryText: TextView
    private lateinit var optionOneButton: Button
    private lateinit var optionTwoButton: Button
    private lateinit var optionThreeButton: Button
    private lateinit var optionFourButton: Button

    private lateinit var correctAnswer: String
    private lateinit var currentQuestionObject: JSONObject

    private var numberOfQuestions: Int = 0
    private var questionDurationMilli: Long = durationDefault
    private var nextQuestionDelayMilli: Long = delayDefault
    private val timerInterval: Long = 1000
    private var questionCounter: Int = 0
    private var scoreCount: Int = 0

    private var scoreDictionary: HashMap<String, MutableMap<String, Int>> = hashMapOf()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        questionsArray = JSONArray(intent.getStringExtra("questionArrayString"))

        questionText = findViewById(R.id.questionTextView)
        categoryText = findViewById(R.id.categoryTextView)
        optionOneButton = findViewById(R.id.optionOneButton)
        optionOneButton.setOnClickListener(this)
        optionTwoButton = findViewById(R.id.optionTwoButton)
        optionTwoButton.setOnClickListener(this)
        optionThreeButton = findViewById(R.id.optionThreeButton)
        optionThreeButton.setOnClickListener(this)
        optionFourButton = findViewById(R.id.optionFourButton)
        optionFourButton.setOnClickListener(this)

        buttonArray = arrayOf(optionOneButton, optionTwoButton, optionThreeButton, optionFourButton)

        numberOfQuestions = questionsArray.length()
        questionCounter = 0

        if (savedInstanceState != null) {
            questionCounter = savedInstanceState.getInt("currentQuestion")
            questionDurationMilli = savedInstanceState.getLong("timeLeft")
            timeLeft = questionDurationMilli
            questionEnded = savedInstanceState.getBoolean("questionEnded")
            scoreCount = savedInstanceState.getInt("scoreCount")
            buttonClickedString = savedInstanceState.getString("clickedButtonString").toString()
            @Suppress("UNCHECKED_CAST")
            scoreDictionary =
                savedInstanceState.getSerializable("scoreDictionary") as HashMap<String, MutableMap<String, Int>>
            nextQuestionDelayMilli = savedInstanceState.getLong("delayLeft")
            answerList = savedInstanceState.getStringArrayList("answerList") as ArrayList<String>

            scoreValueText.text = scoreCount.toString()
            updateTimer(questionDurationMilli)
        } else {
            questionDurationMilli = durationDefault
            nextQuestionDelayMilli = delayDefault
            delayLeft = delayDefault
            questionEnded = false
        }

        loadQuestion()
    }

    // Load and display next question stored in JSONArray
    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadQuestion() {
        // If questions remain, load next question
        if (questionCounter < numberOfQuestions) {
            // Hide buttons to only display buttons for number of answers (e.g. boolean is 2 buttons)
            hideButtons()
            updateTimer(questionDurationMilli)

            // Get the current question object
            currentQuestionObject = questionsArray.getJSONObject(questionCounter)
            displayQuestion()

            if (!questionEnded) {
                // Extract incorrect answer array and correct answer string, unescape the correct answer string to eliminate html escaped characters
                val incorrectAnswers = currentQuestionObject.getJSONArray("incorrect_answers")
                answerList = arrayListOf()

                // Get answers, parse incorrect answer array and add to answer list, shuffle for randomness
                for (i in 0 until incorrectAnswers.length()) {
                    // Add unescaped incorrect answer strings to list
                    answerList.add(unescapeHtml4(incorrectAnswers.getString(i)))
                }

                answerList.add(correctAnswer)
                answerList.shuffle()
            }

            // Add answer choices to buttons and make visible for number of answers (e.g. 2 buttons for boolean)
            for (i in 0 until answerList.size) {
                buttonArray[i].text = answerList[i]
                buttonArray[i].visibility = View.VISIBLE
            }

            // Debug to display answer list
//            Toast.makeText(applicationContext, answerList.toString(), Toast.LENGTH_SHORT).show()
            if (questionEnded) {
                if (buttonClickedString != "") {
                    for (button in buttonArray) {
                        if (button.text.toString() == buttonClickedString) {
                            showIncorrectAnswer(button)
                        }
                    }
                }
                showCorrectAnswer()
                nextQuestionDelay()
            } else {
                startQuestionCountdown()
            }
        } else {
            // Questions complete, end quiz
            showScore()
        }
    }

    // Start question duration timer
    private fun startQuestionCountdown() {
        questionTimer = object : CountDownTimer(questionDurationMilli, timerInterval) {
            val questionTimerView: TextView = findViewById(R.id.questionTimerView)
            override fun onFinish() {
                questionEnded = true
                questionTimerView.text = "0"
                showCorrectAnswer()
                nextQuestionDelayMilli = delayDefault
                nextQuestionDelay()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTick(millisUntilFinished: Long) {
                // Update text view with time remaining
                timeLeft = millisUntilFinished
                // Once time reaches 5 seconds, change text colour to red for urgency
                updateTimer(timeLeft)
            }
        }.start()
    }

    // At the beginning of every question, hide the buttons until they are updated with new data
    private fun hideButtons() {
        for (button: Button in buttonArray) {
            button.setBackgroundResource(R.drawable.answer_button)
            button.visibility = View.GONE
        }
        questionTimerView.setTextColor(Color.BLACK)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun displayQuestion() {
        val categoryString: String = unescapeHtml4(currentQuestionObject.getString("category"))
        // Get reference to the relevant colour to the given category string dynamically
        val categoryColourReference: Int = resources.getIdentifier(
            categoryString.filter { it.isLetterOrDigit() },
            "color",
            packageName
        )

        questionText.text = getString(R.string.question, questionCounter + 1, numberOfQuestions)
        questionText.append(unescapeHtml4(currentQuestionObject.getString("question")))

        // Trim the string to remove prefixes such as "Entertainment" and "Science"
        categoryText.text = categoryString.substring(categoryString.indexOf(":") + 1).trimStart()

        /* Post category drawable to set the colour scheme after the layout has been constructed
        This is necessary due to the constraint height not being available until after the layout
        is built */
        categoryConstraint.post {
            val categoryTheme = ShapeDrawable(RectShape())
            categoryTheme.paint.shader = LinearGradient(
                0f,
                0f,
                0f,
                categoryConstraint.height.toFloat(),
                Color.parseColor(resources.getString(categoryColourReference)),
                Color.parseColor("#FFFFFF"),
                Shader.TileMode.REPEAT
            )
            categoryConstraint.background = categoryTheme
        }

        correctAnswer = unescapeHtml4(currentQuestionObject.getString("correct_answer"))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateTimer(timeRemaining: Long) {
        if ((timeRemaining / 1000).toInt() <= 5) {
            questionTimerView.setTextColor(getColor(R.color.incorrectColor))
        }
        questionTimerView.text = (timeRemaining / 1000).toString()
    }

    // Handle answer button click
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View) {
        // Check that the timer hasn't ran out or that the user hasn't clicked an answer already
        if (!questionEnded) {
            // Set to true to prevent answer button spamming
            questionEnded = true
            // Stop question timer
            questionTimer.cancel()

            buttonClicked = findViewById(v.id)
            buttonClickedString = buttonClicked.text.toString()

            showCorrectAnswer()


            if (buttonClicked.text == correctAnswer) {
                // Correct answer given, update score
                updateScore()
            } else {
                // Incorrect answer given, set clicked button to incorrect colours
                showIncorrectAnswer(buttonClicked)
            }

            nextQuestionDelay()
        }
    }

    // Start delay until next question is shown
    private fun nextQuestionDelay() {
        val delayTimer: CountDownTimer =
            object : CountDownTimer(nextQuestionDelayMilli, timerInterval) {
                @RequiresApi(Build.VERSION_CODES.M)
                override fun onFinish() {
                    questionEnded = false
                    questionCounter += 1
                    questionDurationMilli = durationDefault
                    nextQuestionDelayMilli = delayDefault
                    delayLeft = delayDefault
                    loadQuestion()
                }

                override fun onTick(millisUntilFinished: Long) {
                    delayLeft = millisUntilFinished

                }
            }
        delayTimer.start()
    }

    // Change the colour scheme of the clicked button to reflect the correct answer
    private fun showCorrectAnswer() {
        // Set correct answer button colour to drawable green
        for (button: Button in buttonArray) {
            if (button.text == correctAnswer) {
                button.setBackgroundResource(R.drawable.correct_button)
            }
        }
    }

    // Change the colour scheme of the clicked button to reflect the incorrect answer
    private fun showIncorrectAnswer(buttonClicked: Button) {
        buttonClicked.setBackgroundResource(R.drawable.incorrect_button)
    }

    // Correct answer given, update dictionary/mutable map containing scores for current quiz
    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateScore() {
        val questionCategory: String = currentQuestionObject.getString("category")
        val trimmedCategory: String = questionCategory.substring(questionCategory.indexOf(":") + 1).trimStart()
        val questionDifficulty: String = currentQuestionObject.getString("difficulty")
        // Update score count, update score text view
        scoreCount += 1
        scoreValueText.text = scoreCount.toString()

        // Update score dictionary pertaining to category and difficulty of question for updating leaderboard
        // If the current question category already exists, update the values map, else add category to map with first category score
        if (scoreDictionary.containsKey(trimmedCategory)) {
            // If no entries for current question difficulty exist, create entry with score of 1, else increment
            val difficultyMap = scoreDictionary[trimmedCategory]
            difficultyMap?.set(
                questionDifficulty,
                difficultyMap.getOrDefault(questionDifficulty, 0) + 1
            )
        } else {
            // Initialise category in scoreDictionary with entry for current question difficulty and score of 1
            scoreDictionary[trimmedCategory] = mutableMapOf(questionDifficulty to 1)
        }
    }

    // Quiz finished, show the user their score in new activity
    private fun showScore() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, ResultsActivity::class.java)
            // send score data to next activity
            intent.putExtra("score", scoreCount)
            intent.putExtra("questionScores", scoreDictionary)

            finish()
            startActivity(intent)
        }, nextQuestionDelayMilli)
    }

    // Handle orientation changes by saving current relevant data to the quiz, reload on layout change
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentQuestion", questionCounter)
        outState.putLong("timeLeft", timeLeft)
        outState.putBoolean("questionEnded", questionEnded)
        outState.putInt("scoreCount", scoreCount)
        outState.putString("clickedButtonString", buttonClickedString)
        outState.putSerializable("scoreDictionary", scoreDictionary)
        outState.putLong("delayLeft", delayLeft)
        outState.putStringArrayList("answerList", answerList)
    }

    // If the back button is pressed, cancel the timer and finish the activity
    override fun onBackPressed() {
        questionTimer.cancel()
        finish()
        super.onBackPressed()
    }

    // When the focus resumes on the app, hide the nav bar
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNavigationBar()
        }
    }

    // Hides the navigation bar (and whole system UI) for full screen during the quiz
    // Users can swipe up/down at the bottom/top of the screen to see the UI
    private fun hideNavigationBar() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    // Every time the app is resumed (including orientation changes) the nav bar is hidden again
    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }
}

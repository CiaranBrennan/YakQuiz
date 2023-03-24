package com.example.yak

import android.content.Intent
import android.net.Uri.Builder
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_quiz_selection.*
import org.json.JSONArray
import org.json.JSONException


class QuizSelectionActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_selection)

        requestQueue = Volley.newRequestQueue(this)

        // Get number of text field and set filter for numbers between 1-50 only
        val numberOfQuestionsField: EditText = findViewById(R.id.numberOfQuestionsInput)
        numberOfQuestionsField.filters = arrayOf<InputFilter>(MinMaxBounds(1, 50))

        startQuizButton.isEnabled = numberOfQuestionsField.text.toString().trim().isNotEmpty()

        numberOfQuestionsField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                startQuizButton.isEnabled = numberOfQuestionsField.text.toString().trim().isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        val category: Spinner = findViewById(R.id.categorySpinnerSelector)
        var chosenCategoryIndex = 0

        val difficulty: Spinner = findViewById(R.id.difficultySpinnerSelector)
        var chosenDifficultyIndex = 0

        val answerType: Spinner = findViewById(R.id.answerTypeSpinnerSelector)
        var chosenAnswerTypeIndex = 0

        val startQuizButton: Button = findViewById(R.id.startQuizButton)

        // Category choices available to user in spinner
        val categoryChoices = arrayOf(
            "Any Category",
            "General Knowledge",
            "Books",
            "Film",
            "Music",
            "Musicals & Theatre",
            "Television",
            "Video Games",
            "Board Games",
            "Science & Nature",
            "Computers",
            "Mathematics",
            "Mythology",
            "Sports",
            "Geography",
            "History",
            "Politics",
            "Art",
            "Celebrities",
            "Animals",
            "Vehicles",
            "Comics",
            "Gadgets",
            "Japanese Anime & Manga",
            "Cartoon & Animations"
        )

        // Difficulty options available to user in spinner
        val difficultyChoices = arrayOf("Any Difficulty", "Easy", "Medium", "Hard")
        // Answer type options available to user in spinner
        val answerTypeChoices = arrayOf("Any Type", "Multiple Choice", "True/False")

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryChoices)
        val difficultyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultyChoices)
        val answerTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, answerTypeChoices)

        category.adapter = categoryAdapter
        difficulty.adapter = difficultyAdapter
        answerType.adapter = answerTypeAdapter

        // Listen for category selection
        category.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                chosenCategoryIndex = 0
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenCategoryIndex = position
            }
        }

        // Listen for difficulty selection
        difficulty.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                chosenDifficultyIndex = 0
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenDifficultyIndex = position
            }
        }

        // Listen for answer type selection
        answerType.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                chosenAnswerTypeIndex = 0
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenAnswerTypeIndex = position
            }
        }

        // Handle start quiz request
        startQuizButton.setOnClickListener {
            // If the number of questions field empty, prompt user to provide number
            if(numberOfQuestionsField.text.trim().isEmpty() || numberOfQuestionsField.text.toString() == "0") {
                Toast.makeText(applicationContext, "Enter number of questions (1-50)", Toast.LENGTH_SHORT).show()
            }
            else {
                // Build URL and request quiz data from API
                val url = buildURL(numberOfQuestionsField.text.toString(), chosenCategoryIndex, chosenDifficultyIndex, chosenAnswerTypeIndex)
                requestQuizData(url)
            }
        }
    }

    // Automatically build API URL from user choices
    private fun buildURL(numQuestions: String, categoryIndex: Int, difficultyIndex: Int, answerIndex: Int): String {
        val urlBuilder = Builder()
        // Build base URL for all requests
        urlBuilder.scheme("https")
            .authority("opentdb.com")
            .appendPath("api.php")
            .appendQueryParameter("amount", numQuestions)

        // Specific category selected, add to URL request
        if(categoryIndex != 0) {
            val categoryString = (categoryIndex + 8).toString()
            urlBuilder.appendQueryParameter("category", categoryString)
        }

        // Specific difficulty selected, add to URL request
        when(difficultyIndex) {
            1 -> urlBuilder.appendQueryParameter("difficulty", "easy")
            2 -> urlBuilder.appendQueryParameter("difficulty", "medium")
            3 -> urlBuilder.appendQueryParameter("difficulty", "hard")
        }

        // Specific answer type selected, add to URL request
        when(answerIndex) {
            1 -> urlBuilder.appendQueryParameter("type", "multiple")
            2 -> urlBuilder.appendQueryParameter("type", "boolean")
        }

        return urlBuilder.toString()
    }

    // Send volley request to OpenTriviaDB with constructed URL to receive JSONData
    private fun requestQuizData(url: String) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            // Listen for request response
            Response.Listener { response ->
                try {
                    val responseCode: Int = response.get("response_code") as Int
                    // If response code is 0, request successful
                    if(responseCode == 0) {
                        val jsonQuestionsArray: JSONArray = response.getJSONArray("results")
                        startQuiz(jsonQuestionsArray)
                    }
                    else {
                        // Unsuccessful request
                        Toast.makeText(applicationContext, "Response code $responseCode, couldn't contact server, URL: $url", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        )

        // Add request to queue to fetch data
        requestQueue.add(jsonObjectRequest)
    }

    // Launch question activity and pass question data
    private fun startQuiz(questionsArray: JSONArray) {
        finish()
        intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("questionArrayString", questionsArray.toString())
        startActivity(intent)
    }
}

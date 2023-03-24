package com.example.yak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_results.*

class ResultsActivity : AppCompatActivity() {

    private var score: Int = 0
    private lateinit var questionScores: HashMap<String, HashMap<String, Int>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        displayScore()
        score = intent.getIntExtra("score", 0)

        //Get the currently logged in user if there is one
        val user =
            applicationContext.getSharedPreferences("CurrentUser", 0) // 0 - for private mode

        //Get the value of the key "username"
        val username = user.getString("username", null)
//        questionScores = intent.getSerializableExtra("questionScores") as HashMap<String, HashMap<String, Int>>

        // Check if orientation changed
        @Suppress("UNCHECKED_CAST")
        questionScores = if (savedInstanceState != null) {
            savedInstanceState.getSerializable("questionScores") as HashMap<String, HashMap<String, Int>>
        } else {
            intent.getSerializableExtra("questionScores") as HashMap<String, HashMap<String, Int>>
        }


        //Send the user's scores to the server if they're logged in
        if (username != null) {
            // Checks that there are scores to update the database with
            if (questionScores.isNotEmpty()) {
                questionScores.forEach { (category, questions) ->
                    questions.forEach { (difficulty, score) ->
                        Fuel.post("http://cjbrennan.pythonanywhere.com/addscore/")
                            .jsonBody("{\"username\":\"$username\",\"category\":\" $category\",\"difficulty\":\"$difficulty\",\"score\":\"$score\"}")
                            .response { result ->
                                when (result) {
                                    is Result.Failure -> {
                                        val ex = result.getException()
                                        println(ex)
                                    }
                                    is Result.Success -> {
                                        val data = result.get()[0].toChar()
                                        if (data == '1' || data == '2') {
                                            Toast.makeText(
                                                applicationContext,
                                                "Score added to leaderboard!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        } else {
            Toast.makeText(applicationContext, "Score not saved. Log in to save scores.", Toast.LENGTH_SHORT).show()
        }
        // Clear scores object to prevent sending scores to server repeatedly on orientation changes
        questionScores.clear()
    }

    // start another quiz
    fun playQuiz(view: View) {
        val intent = Intent(this, QuizSelectionActivity::class.java)
        startActivity(intent)
        finish()
    }

    // start a menu activity
    fun returnToMenu(view: View) {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    // get score from previous activity
    private fun displayScore() {
        score = intent.getIntExtra("score", 0)
        scoreText.text = score.toString()
    }

    // share score with implicit intent
    fun shareScore(view: View) {

        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, "My Yak Quiz Score")
            putExtra(Intent.EXTRA_TEXT, "My new score on Yak Quiz is $score!")
            type = "text/plain"
        }

        // choose which app to share with
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    // Handle orientation changes by saving current relevant data to the quiz, reload on layout change
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("questionScores", questionScores)
    }
}

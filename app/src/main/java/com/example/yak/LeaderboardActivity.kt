package com.example.yak

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import org.json.JSONArray


class LeaderboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val tl = findViewById<TableLayout>(R.id.main_table)
        //Retrieve a list of all scores from the server
        Fuel.post("http://cjbrennan.pythonanywhere.com/getscores/")
            .response { result ->
                when(result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        //Convert it into a JSON array
                        var data = ""
                        val rawData = result.get()
                        for (intChar in rawData) {
                            val character = intChar.toChar()
                            data += character
                        }
                        val scores = JSONArray(data)

                        //Loop through each JSON object and get the data from them
                        for (i in 0 until scores.length()) {
                            val score = scores.getJSONObject(i)
                            val row = TableRow(this)
                            val category = TextView(this)
                            val difficulty = TextView(this)
                            val scoredBy = TextView(this)
                            val userScore = TextView(this)
                            category.text = score["category"].toString()
                            difficulty.text = score["difficulty"].toString()
                            scoredBy.text = score["user"].toString()
                            userScore.text = score["score"].toString()
                            //Adjust some attributes of the TextViews
                            category.textSize = 16F
                            difficulty.textSize = 16F
                            scoredBy.textSize = 16F
                            userScore.textSize = 16F
                            category.width = 50
                            category.setPadding(0, 0, 0, 8)
                            //Add the newly made row to the TableLayout
                            row.addView(category)
                            row.addView(difficulty)
                            row.addView(scoredBy)
                            row.addView(userScore)
                            tl.addView(row, i + 1)
                        }
                    }
                }
            }
    }

    fun goBack(view: View) {
        //Return to the main menu
        finish()
    }
}

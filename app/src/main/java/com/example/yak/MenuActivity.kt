package com.example.yak

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

// shows the main menu
class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    // start the quiz selection activity
    fun chooseQuiz(view: View) {
        val intent = Intent(this, QuizSelectionActivity::class.java)
        startActivity(intent)
    }

    // start the leaderboard activity
    fun viewLeaderboard(view: View) {
        val intent = Intent(this, LeaderboardActivity::class.java)
        startActivity(intent)
    }

    fun quitQuiz(view: View) {
        //Log the current user out by deleting their username from the shared preferences
        val pref = applicationContext.getSharedPreferences(
            "CurrentUser",
            0
        )
        val editor = pref.edit()
        editor.remove("username")
        editor.apply()

        // finish the menu activity and go to the start activity
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }
}

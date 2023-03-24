package com.example.yak

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val body = "{\"username\":\"" + username.text.toString() + "\",\"password\":\"" + password.text.toString() + "\"}"
        Fuel.post("http://cjbrennan.pythonanywhere.com/login/")
            .jsonBody(body)
            .response { result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()[0].toChar()
                        if (data == '1') {
                            val pref = applicationContext.getSharedPreferences(
                                "CurrentUser",
                                0
                            )
                            val editor = pref.edit()
                            editor.putString("username", username.text.toString())
                            editor.apply()
                            Toast.makeText(applicationContext, "Logged in", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MenuActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "Couldn't login, please check your credentials are correct and try again", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }
}

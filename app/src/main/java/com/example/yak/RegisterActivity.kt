package com.example.yak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(view: View) {
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val body = "{\"username\":\"" + username.text.toString() + "\",\"password\":\"" + password.text.toString() + "\"}"
        Fuel.post("http://cjbrennan.pythonanywhere.com/register/")
            .jsonBody(body)
            .response { result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()[0].toChar()
                        //If the user is successfully registered, log them in and go to the main menu
                        if (data == '1') {
                            Toast.makeText(applicationContext, "Successfully registered!", Toast.LENGTH_SHORT).show()
                            val pref = applicationContext.getSharedPreferences(
                                "CurrentUser",
                                0
                            )
                            val editor = pref.edit()
                            editor.putString("username", username.text.toString())
                            editor.apply()
                            val intent = Intent(this, MenuActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "That username is taken. Please try another or log in", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }
}

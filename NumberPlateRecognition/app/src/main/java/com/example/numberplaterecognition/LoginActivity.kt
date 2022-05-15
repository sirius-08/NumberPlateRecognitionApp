package com.example.numberplaterecognition

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var signUpButton: Button
    lateinit  var signInButton: Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences =
            getSharedPreferences(getString(R.string.login_preference_file), MODE_PRIVATE)

        initViews()

        signInButton.setOnClickListener {
            if (valid()) {
                val intent = Intent(this@LoginActivity, LoadImageActivity::class.java)
                startActivity(intent)
            }
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, NewUserActivity::class.java)
            startActivity(intent)
        }

    }

    fun valid(): Boolean {
        val username = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this@LoginActivity, "Username can't be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@LoginActivity, "Password can't be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        return if (TextUtils.equals(password, sharedPreferences.getString(username, ""))) {
            true
        } else {
            Toast.makeText(
                this@LoginActivity,
                "Either username or password is incorrect",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    fun initViews() {
        signInButton = findViewById<View>(R.id.signin_button) as Button
        signUpButton = findViewById<View>(R.id.signup_button) as Button
        emailEditText = findViewById<View>(R.id.email_editText) as EditText
        passwordEditText = findViewById<View>(R.id.password_editText) as EditText
    }
}
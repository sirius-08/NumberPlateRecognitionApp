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

class NewUserActivity : AppCompatActivity() {
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var rePasswordEditText: EditText
    lateinit var sharedPreferences: SharedPreferences
    lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        sharedPreferences =
            getSharedPreferences(getString(R.string.login_preference_file), MODE_PRIVATE)
        initViews()

        registerButton.setOnClickListener {
            if (valid()) {
                val intent = Intent(this@NewUserActivity, LoadImageActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun valid(): Boolean {
        val username = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val rePassword = rePasswordEditText.text.toString()
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this@NewUserActivity, "Username can't be empty", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@NewUserActivity, "Password can't be empty", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (TextUtils.equals(password, rePassword)) {
            saveData(username, password)
            return true
        } else {
            Toast.makeText(this@NewUserActivity, "Password doesn't match", Toast.LENGTH_SHORT)
                .show()
            return false
        }
    }

    private fun saveData(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(username, password)
        editor.apply()
    }

    private fun initViews() {
        emailEditText = findViewById<View>(R.id.newEmailEditText) as EditText
        passwordEditText = findViewById<View>(R.id.newPasswordEditText) as EditText
        rePasswordEditText = findViewById<View>(R.id.rePasswordEditText) as EditText
        registerButton = findViewById<View>(R.id.registerButton) as Button
    }
}
package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class Login : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var usernameTextEdit: EditText
    private lateinit var passwordTextEdit: EditText

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginButton)
        usernameTextEdit = findViewById(R.id.usernameText)
        passwordTextEdit = findViewById(R.id.passwordText)

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        loginButton.setOnClickListener {
            val username = usernameTextEdit.text.toString()
            // need to hash
            val password = passwordTextEdit.text.toString()

            val prefsEditor = sharedPreferences.edit()
            prefsEditor.putString("USERNAME", username)
            prefsEditor.putString("PASSWORD", password)
            prefsEditor.commit()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
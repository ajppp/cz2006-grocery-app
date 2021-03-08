package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {

    private companion object{
        val TAG = "LOGIN"
    }

    private lateinit var signUpButton: Button
    private lateinit var loginButton: Button
    private lateinit var usernameTextEdit: EditText
    private lateinit var passwordTextEdit: EditText
    private lateinit var loginClRoot: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = DataBaseHelper(this)

        loginClRoot = findViewById(R.id.loginClRoot)
        loginButton = findViewById(R.id.loginButton)
        usernameTextEdit = findViewById(R.id.usernameText)
        passwordTextEdit = findViewById(R.id.passwordText)
        signUpButton = findViewById(R.id.loginPageSignUpButton)

        // login button
        loginButton.setOnClickListener {
            val username = usernameTextEdit.text.toString()
            // need to hash
            val password = passwordTextEdit.text.toString()
            // verification
            if(db.verifyUserExists(username)){
                if (db.verifyUserPassword(username, password)){
                    val intent = Intent(this, MainActivity::class.java)
                    // allows us to get the user data only in the main activity
                    intent.putExtra("USERNAME", username)
                    intent.putExtra("PASSWORD", password)
                    startActivity(intent)

                } else { Snackbar.make(loginClRoot, "Wrong Password", Snackbar.LENGTH_LONG).show() }
            } else { Snackbar.make(loginClRoot, "A user with that username does not exist", Snackbar.LENGTH_LONG).show() }
        }

        // sign up button
        signUpButton.setOnClickListener { startActivity(Intent(this, SignUp::class.java)) }
    }
}
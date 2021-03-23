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
import java.math.BigInteger
import java.security.MessageDigest

/**
 * This class is an activity class which
 * implements the Login activity. It is
 * the first page that the user sees when opening
 * the app
 *
 * @author jethro
 * @author claudia
 */
class Login : AppCompatActivity() {

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
            val password = hash(passwordTextEdit.text.toString())
            // verification
            if(db.verifyUserExists(username)){
                if (db.verifyUserPassword(username, password)){
                    // allows us to get the user data only in the main activity
                    getSharedPreferences("USER_REF", Context.MODE_PRIVATE).edit().putString("USERNAME", username).commit()
                    startActivity(Intent(this, MainActivity::class.java))

                } else { Snackbar.make(loginClRoot, "Wrong Password", Snackbar.LENGTH_LONG).show() }
            } else { Snackbar.make(loginClRoot, "A user with that username does not exist", Snackbar.LENGTH_LONG).show() }
        }

        // sign up button
        signUpButton.setOnClickListener { startActivity(Intent(this, SignUp::class.java)) }
    }

    private fun hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}
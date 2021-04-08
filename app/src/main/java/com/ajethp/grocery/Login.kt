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
            // get inserted username and password
            val username = usernameTextEdit.text.toString()
            val password = hash(passwordTextEdit.text.toString())

            if (checkValidUser(username, password)) {
                getSharedPreferences("USER_REF", Context.MODE_PRIVATE).edit().putString("USERNAME", username).commit()
                // start the main activity
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        // sign up button
        signUpButton.setOnClickListener { startActivity(Intent(this, SignUp::class.java)) }
    }

    // function to hash the password inserted using MD5
    private fun hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun checkValidUser(username: String, password: String): Boolean {
        val db = DataBaseHelper(this)
        // check if a user with that username exists
        if(db.verifyUserExists(username)){
            // check if that username has thart password attached to it
            if (db.verifyUserPassword(username, password)){
                // store the inserted username in the shared preferences which
                // allows us to get the user data only in the main activity and etc.
                return true
            } else {
                Snackbar.make(loginClRoot, "Wrong Password", Snackbar.LENGTH_LONG).show()
                return false}
        } else {
            Snackbar.make(loginClRoot, "A user with that username does not exist", Snackbar.LENGTH_LONG).show()
            return false
        }

    }
}
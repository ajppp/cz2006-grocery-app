package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlin.math.sign

class SignUp : AppCompatActivity() {

    private lateinit var emailTextEdit: EditText
    private lateinit var usernameTextEdit: EditText
    private lateinit var passwordTextEdit: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var signUpDoneButton: Button
    private lateinit var signUpClRoot: ConstraintLayout

    private lateinit var userSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        userSharedPreferences = getSharedPreferences("USER_REF", Context.MODE_PRIVATE)

        val db = DataBaseHelper(this)

        emailTextEdit = findViewById(R.id.signUpEmailText)
        usernameTextEdit = findViewById(R.id.signUpUsernameText)
        passwordTextEdit = findViewById(R.id.signUpPasswordText)
        confirmPassword = findViewById(R.id.signUpConfirmText)
        signUpDoneButton = findViewById(R.id.signUpButton)
        signUpClRoot = findViewById(R.id.signUpClRoot)

        // TODO("check if such a user already exists in the database")
        // TODO("check if the password and confirm passwords are the same")

        // insert to the database


        signUpDoneButton.setOnClickListener{
            if (confirmPassword.text.toString() != passwordTextEdit.text.toString()){
                Snackbar.make(it, "Passwords are not the same", Snackbar.LENGTH_LONG).show()
            } else {
                val username = usernameTextEdit.text.toString()
                val password = passwordTextEdit.text.toString()

                // check for existing user
                if (db.verifyUserExists(username)) {
                    Snackbar.make(signUpClRoot, "A user with that username already exists", Snackbar.LENGTH_LONG).show()
                } else {
                    val newUser = User(username, password)

                    val jsonString = Gson().toJson(newUser)
                    val userEditor = userSharedPreferences.edit()
                    userEditor.putString("USER", jsonString)
                    userEditor.apply()

                    db.insertUserData(newUser)

                    startActivity(Intent(this, MainActivity::class.java))
                }

            }
        }
    }
}
package com.ajethp.grocery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import kotlin.math.sign

class SignUp : AppCompatActivity() {

    private lateinit var emailTextEdit: EditText
    private lateinit var usernameTextEdit: EditText
    private lateinit var passwordTextEdit: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var signUpDoneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailTextEdit = findViewById(R.id.signUpEmailText)
        usernameTextEdit = findViewById(R.id.signUpUsernameText)
        passwordTextEdit = findViewById(R.id.signUpPasswordText)
        confirmPassword = findViewById(R.id.signUpConfirmText)
        signUpDoneButton = findViewById(R.id.signUpButton)

        // TODO("check if such a user already exists in the database")
        // TODO("check if the password and confirm passwords are the same")

        // insert to the database

        signUpDoneButton.setOnClickListener{
            if (confirmPassword.text.toString() != passwordTextEdit.text.toString()){
                Snackbar.make(it, "Passwords are not the same", Snackbar.LENGTH_LONG).show()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.android.material.snackbar.Snackbar
import java.math.BigInteger
import java.security.MessageDigest
import java.util.regex.Pattern

/**
 * This class is an activity class which
 * implements the Sign Up activity. It is used
 * when the user signs for a new account
 *
 * @author jethro
 */
class SignUp : AppCompatActivity() {

    companion object {
        private val EMAIL_ADDRESS_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e\\x7f])+)\\])")
    }

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



        emailTextEdit = findViewById(R.id.signUpEmailText)
        usernameTextEdit = findViewById(R.id.signUpUsernameText)
        passwordTextEdit = findViewById(R.id.signUpPasswordText)
        confirmPassword = findViewById(R.id.signUpConfirmText)
        signUpDoneButton = findViewById(R.id.signUpButton)
        signUpClRoot = findViewById(R.id.signUpClRoot)


        var checkEmail: Boolean = false
        var checkUsernameNotEmpty: Boolean = false
        var checkPasswordNotEmpty: Boolean = false
        var passwordSimilar: Boolean = false
        if (EMAIL_ADDRESS_PATTERN.matcher(emailTextEdit.text.toString()).matches()) { checkEmail = true }
        if (usernameTextEdit.text.toString().isNotEmpty()) {checkUsernameNotEmpty = true }
        if (passwordTextEdit.text.toString().isNotEmpty()) { checkPasswordNotEmpty = true }
        if (confirmPassword.text.toString() == passwordTextEdit.text.toString()) { passwordSimilar = true }

        val email = emailTextEdit.text.toString()
        val username = usernameTextEdit.text.toString()
        val password = hash(passwordTextEdit.text.toString())

        if (checkValidLoginDetails(email, username, password, checkEmail, checkUsernameNotEmpty, checkPasswordNotEmpty, passwordSimilar)) {

        }


    }

    private fun hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun checkValidLoginDetails (email: String, username: String, password: String, checkEmail: Boolean, checkUsernameNotEmpty: Boolean, checkPasswordNotEmpty: Boolean, passwordSimilar: Boolean): Boolean {
        val db = DataBaseHelper(this)
        signUpDoneButton.setOnClickListener {
            if (checkEmail) {
                if (checkUsernameNotEmpty) {
                    if (checkPasswordNotEmpty) {
                        if (passwordSimilar) {
                            // check for existing user
                            if (db.verifyUserExists(username)) {
                                Snackbar.make(signUpClRoot, "A user with that username already exists", Snackbar.LENGTH_LONG).show()
                                // i tried adding a return here but they said not allowed what the fuck
                            } else {
                                // everything is correct
                                val newUser = User(email, username, password)
                                db.insertUserData(newUser)
                                getSharedPreferences("USER_REF", Context.MODE_PRIVATE).edit().putString("USERNAME", username).commit()

                                startActivity(Intent(this, MainActivity::class.java))
                            }
                        } else { Snackbar.make(it, "Passwords are not the same", Snackbar.LENGTH_LONG).show() }
                    } else { Snackbar.make(signUpClRoot, "Password is empty! Please insert a password", Snackbar.LENGTH_LONG).show() }
                } else { Snackbar.make(signUpClRoot, "Username is empty! Please insert a Username", Snackbar.LENGTH_LONG).show() }
            } else { Snackbar.make(signUpClRoot, "Invalid Email Address", Snackbar.LENGTH_LONG).show() }
        }
    }
}

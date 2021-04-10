package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import java.math.BigInteger
import java.security.MessageDigest

class CreateFamily : AppCompatActivity() {
    private lateinit var currentUser: User

    private lateinit var createFamilyId: EditText
    private lateinit var createFamilyPassword: EditText
    private lateinit var createFamilyConfirmPassword: EditText
    private lateinit var createFamilyClRoot: ConstraintLayout
    private lateinit var createFamilyDoneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_family)

        currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)

        createFamilyId = findViewById(R.id.createFamilyId)
        createFamilyPassword = findViewById(R.id.createFamilyPassword)
        createFamilyConfirmPassword = findViewById(R.id.createFamilyConfirmPassword)
        createFamilyDoneButton = findViewById(R.id.createFamilyDoneButton)
        createFamilyClRoot = findViewById(R.id.createFamilyClRoot)

        createFamilyDoneButton.setOnClickListener {
            val familyId = createFamilyId.text.toString()
            val password = hash(createFamilyPassword.text.toString())
            val confirmPassword = hash(createFamilyConfirmPassword.text.toString())
            when (checkCreateFamilyDetails(familyId, password, confirmPassword)) {
                1 -> Snackbar.make(createFamilyClRoot, "Empty Family ID!", Snackbar.LENGTH_LONG).show()
                2 -> Snackbar.make(createFamilyClRoot, "Empty Password", Snackbar.LENGTH_LONG).show()
                3 -> Snackbar.make(createFamilyClRoot, "Passwords are not the same", Snackbar.LENGTH_LONG).show()
                4 -> Snackbar.make(createFamilyClRoot, "A family with that ID already exists", Snackbar.LENGTH_LONG).show()
                5 -> {
                    // TODO("make a new family and insert it into the database")
                    DataBaseHelper(this).insertFamilyData(familyId, password)
                    startActivity(Intent(this, Settings::class.java))
                }
            }
        }
    }

    private fun checkCreateFamilyDetails(familyId: String, password: String, confirmPassword: String): Int {
        return if (familyId.isNotEmpty()) {
            if (password.isNotEmpty()) {
                if (password == confirmPassword) {
                    if (DataBaseHelper(this).verifyFamilyExists(familyId)) {
                        4
                    } else { 5 }
                } else { 3 }
            } else { 2 }
        } else { 1 }

    }

    private fun hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}
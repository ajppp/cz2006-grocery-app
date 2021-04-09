package com.ajethp.grocery

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.ajethp.grocery.classes.Family
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper

class CreateFamily : AppCompatActivity() {
    private lateinit var currentUser: User

    private lateinit var createFamilyId: EditText
    private lateinit var createFamilyPassword: EditText
    private lateinit var createFamilyConfirmPassword: EditText
    private lateinit var createFamilyDoneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_family)

        currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)

        createFamilyId = findViewById(R.id.createFamilyId)
        createFamilyPassword = findViewById(R.id.createFamilyPassword)
        createFamilyConfirmPassword = findViewById(R.id.createFamilyConfirmPassword)
        createFamilyDoneButton = findViewById(R.id.createFamilyDoneButton)
    }
}
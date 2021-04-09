package com.ajethp.grocery

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper

class JoinFamilyActivity : AppCompatActivity() {

    private lateinit var currentUser: User

    private lateinit var joinFamilyId: EditText
    private lateinit var joinFamilyPassword: EditText
    private lateinit var joinFamilyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_family)

        currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)

        joinFamilyId = findViewById(R.id.familyIdInput)
        joinFamilyPassword = findViewById(R.id.familyPasswordInput)
        joinFamilyButton = findViewById(R.id.joinFamilyButton)
    }
}
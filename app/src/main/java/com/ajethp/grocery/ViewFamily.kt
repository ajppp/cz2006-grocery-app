package com.ajethp.grocery

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper

class ViewFamily : AppCompatActivity() {

    private lateinit var currentUser: User

    private lateinit var familyIdText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_family)

        currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)
        familyIdText = findViewById(R.id.familyIDText)
        familyIdText.text = currentUser.familyId
    }
}
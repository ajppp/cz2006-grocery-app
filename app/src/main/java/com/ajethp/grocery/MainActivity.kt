package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import com.ajethp.grocery.classes.Food
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.gson.Gson
import java.time.LocalDate
import java.time.Month

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MAIN"
    }

    private lateinit var inventoryButton: Button
    private lateinit var recipeButton: Button
    private lateinit var groceryButton: Button
    private lateinit var settingsButton: Button

    private lateinit var userSharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inventoryButton = findViewById(R.id.inventoryButton)
        recipeButton = findViewById(R.id.recipeButton)
        groceryButton = findViewById(R.id.groceryButton)
        settingsButton = findViewById(R.id.settingsButton)

    }

    override fun onResume() {
        super.onResume()

        val db = DataBaseHelper(this)
        val username = getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")
        val currentUser : User = db.readUserData(username!!)
        currentUser.inventoryList.sortBy { it.expiryDate }
        Log.i(TAG, currentUser.toString())

        // val intent = Intent(this, Grocery::class.java)
        // intent.putExtra("USERNAME", currentUser.username)

        // val jsonString = Gson().toJson(currentUser)
        // val userEditor = userSharedPreferences.edit()
        // userEditor.putString("USER", jsonString)
        // userEditor.apply()

        inventoryButton.setOnClickListener { startActivity(Intent(this, Inventory::class.java)) }
        recipeButton.setOnClickListener { startActivity(Intent(this, Recipe::class.java)) }
        groceryButton.setOnClickListener {startActivity(Intent(this, Grocery::class.java))}
        settingsButton.setOnClickListener { startActivity(Intent(this, Settings::class.java)) }
    }
}
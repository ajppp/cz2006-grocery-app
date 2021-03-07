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
import com.google.gson.Gson
import java.time.LocalDate
import java.time.Month

class MainActivity : AppCompatActivity() {

    private lateinit var inventoryButton: Button
    private lateinit var recipeButton: Button
    private lateinit var groceryButton: Button
    private lateinit var settingsButton: Button

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



        inventoryButton.setOnClickListener { startActivity(Intent(this, Inventory::class.java)) }
        recipeButton.setOnClickListener { startActivity(Intent(this, Recipe::class.java)) }
        groceryButton.setOnClickListener { startActivity(Intent(this, Grocery::class.java)) }
        settingsButton.setOnClickListener { startActivity(Intent(this, Settings::class.java)) }
    }
}
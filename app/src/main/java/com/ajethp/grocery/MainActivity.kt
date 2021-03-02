package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.ajethp.grocery.classes.User

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }


    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var inventoryButton: Button
    private lateinit var recipeButton: Button
    private lateinit var groceryButton: Button
    private lateinit var settingsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openLogin()

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        inventoryButton = findViewById(R.id.inventoryButton)
        recipeButton = findViewById(R.id.recipeButton)
        groceryButton = findViewById(R.id.groceryButton)
        settingsButton = findViewById(R.id.settingsButton)

        // TODO("sign in")

        val username = sharedPreferences.getString("USERNAME", "")
        Log.i(TAG, "username is $username")

        inventoryButton.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "clicked on inventory button")
            openInventory()
        } )

        recipeButton.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "clicked on recipe button")
            openRecipe()
        } )

        groceryButton.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "clicked on grocery button")
            openGrocery()
        } )

        settingsButton.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "clicked on setting button")
            openSettings()
        } )

    }

    // open the different activity pages
    private fun openLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    private fun openInventory() {
        val intent = Intent(this, Inventory::class.java)
        startActivity(intent)
    }

    private fun openRecipe() {
        val intent = Intent(this, Recipe::class.java)
        startActivity(intent)
    }

    private fun openGrocery() {
        val intent = Intent(this, Grocery::class.java)
        startActivity(intent)
    }

    private fun openSettings() {
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }


}
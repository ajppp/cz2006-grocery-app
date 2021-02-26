package com.ajethp.grocery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var inventoryButton: Button
    private lateinit var recipeButton: Button
    private lateinit var groceryButton: Button
    private lateinit var settingsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inventoryButton = findViewById(R.id.inventoryButton)
        recipeButton = findViewById(R.id.recipeButton)
        groceryButton = findViewById(R.id.groceryButton)
        settingsButton = findViewById(R.id.settingsButton)

        // testing out the click listener for every button

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
        val intent = Intent(this, Inventory::class.java)
        startActivity(intent)
    }


}
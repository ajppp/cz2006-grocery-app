package com.ajethp.grocery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var inventoryButton: Button
    private lateinit var recipeButton: Button
    private lateinit var groceryButton: Button
    private lateinit var settingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inventoryButton = findViewById(R.id.inventoryButton)
        recipeButton = findViewById(R.id.recipeButton)
        groceryButton = findViewById(R.id.groceryButton)
        settingButton = findViewById(R.id.settingButton)

        inventoryButton.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "clicked on inventory button")
            openInventory()
        } )

        recipeButton.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "clicked on recipe button")
        } )

        groceryButton.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "clicked on grocery button")
        } )

        settingButton.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "clicked on setting button")
        } )

    }

    private fun openInventory() {
        val intent = Intent(this, Inventory::class.java)
        startActivity(intent)
    }

}
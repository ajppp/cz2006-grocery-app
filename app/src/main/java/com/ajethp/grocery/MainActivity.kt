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

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var currentUser: User

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userSharedPreferences: SharedPreferences

    private lateinit var inventoryButton: Button
    private lateinit var recipeButton: Button
    private lateinit var groceryButton: Button
    private lateinit var settingsButton: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "currently on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        userSharedPreferences = getSharedPreferences("USER_REF", Context.MODE_PRIVATE)

        inventoryButton = findViewById(R.id.inventoryButton)
        recipeButton = findViewById(R.id.recipeButton)
        groceryButton = findViewById(R.id.groceryButton)
        settingsButton = findViewById(R.id.settingsButton)

        // TODO("sign in")

        val username = sharedPreferences.getString("USERNAME", "")
        Log.i(TAG, "username is $username")

        // TEMPORARY CODE TO CREATE USER ON FIRST LOGIN TO TRY TO IMPLEMENT APP LOGIC
        if (savedInstanceState == null) {
            val avocadoDate = LocalDate.of(2021, Month.APRIL, 12).toString()
            var avocado = Food("avocado", avocadoDate,2)
            val bananaDate = LocalDate.of(2021, Month.APRIL, 17).toString()
            var banana = Food("banana", bananaDate,5)
            val orangeDate = LocalDate.of(2021, Month.APRIL, 21).toString()
            val orange = Food("orange", orangeDate, 19)
            val chickenDate = LocalDate.of(2021, Month.MARCH, 12).toString()
            val chicken = Food("chicken", chickenDate, 2)
            var kiwi = Food("kiwi", null, 3)
            var peach = Food("peach", null, 7)

            currentUser = User(username!!)
            currentUser.inventoryList.add(avocado)
            currentUser.inventoryList.add(banana)
            currentUser.inventoryList.add(chicken)
            currentUser.inventoryList.add(orange)
            currentUser.shoppingList.add(kiwi)
            currentUser.shoppingList.add(peach)

            val jsonString = Gson().toJson(currentUser)
            Log.i(TAG, jsonString)
            val userEditor = userSharedPreferences.edit()
            userEditor.putString("USER", jsonString)
            userEditor.apply()
        }
    }

    override fun onResume() {
        super.onResume()

        inventoryButton.setOnClickListener { startActivity(Intent(this, Inventory::class.java)) }
        recipeButton.setOnClickListener { startActivity(Intent(this, Recipe::class.java)) }
        groceryButton.setOnClickListener { startActivity(Intent(this, Grocery::class.java)) }
        settingsButton.setOnClickListener { startActivity(Intent(this, Settings::class.java)) }
    }
}
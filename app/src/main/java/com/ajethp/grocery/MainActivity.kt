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

        if (savedInstanceState == null)
            openLogin()

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        userSharedPreferences = getSharedPreferences("USER_REF", Context.MODE_PRIVATE)

        inventoryButton = findViewById(R.id.inventoryButton)
        recipeButton = findViewById(R.id.recipeButton)
        groceryButton = findViewById(R.id.groceryButton)
        settingsButton = findViewById(R.id.settingsButton)

        // TODO("sign in")

        val username = sharedPreferences.getString("USERNAME", "")
        Log.i(TAG, "username is $username")

        // TEMPORARY CODE TO CREATE USER TO TRY TO IMPLEMENT APP LOGIC
        // !! -> throws NullPointerException if null
        if (savedInstanceState == null) {
            val avocadoDate = LocalDate.of(2021, Month.APRIL, 12)
            var avocado = Food("avocado", avocadoDate,2)

            val bananaDate = LocalDate.of(2021, Month.APRIL, 17)
            var banana = Food("banana", bananaDate,5)

            val orangeDate = LocalDate.of(2021, Month.APRIL, 21)
            var orange = Food("orange", orangeDate,19)

            var kiwi = Food("kiwi", null, 3)
            var peach = Food("peach", null, 7)

            currentUser = User(username!!)
            currentUser!!.inventoryList.add(avocado)
            currentUser!!.inventoryList.add(banana)
            currentUser!!.inventoryList.add(orange)

            currentUser!!.shoppingList.add(kiwi)
            currentUser!!.shoppingList.add(peach)

            val jsonString = Gson().toJson(currentUser)
            val userEditor = userSharedPreferences.edit()
            userEditor.putString("USER", jsonString)
            userEditor.apply()
        }
    }

    override fun onResume() {
        super.onResume()

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
        startActivityForResult(intent, 2)
    }

    private fun openInventory() {
        val intent = Intent(this, Inventory::class.java)
        intent.putExtra("CURRENT USER", currentUser)
        startActivity(intent)
    }

    private fun openRecipe() {
        val intent = Intent(this, Recipe::class.java)
        intent.putExtra("CURRENT USER", currentUser)
        startActivity(intent)
    }

    private fun openGrocery() {
        val intent = Intent(this, Grocery::class.java)
        intent.putExtra("CURRENT USER", currentUser)
        startActivity(intent)
    }

    private fun openSettings() {
        val intent = Intent(this, Settings::class.java)
        intent.putExtra("CURRENT USER", currentUser)
        startActivity(intent)
    }


}
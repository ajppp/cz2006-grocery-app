package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.ajethp.grocery.classes.Food
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.gson.Gson

class AddNewShoppingItem : AppCompatActivity() {

    private lateinit var newShoppingItemName: String
    private var newShoppingItemQuantity: Int = 0

    private lateinit var enterShoppingItem: EditText
    private lateinit var enterShoppingQuantity: EditText
    private lateinit var addShoppingItemDoneButton: Button

    private lateinit var currentUser: User
    private lateinit var userSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_shopping_item)

        userSharedPreferences = getSharedPreferences("USER_REF", Context.MODE_PRIVATE)
        val userJsonString = userSharedPreferences.getString("USER", "")
        currentUser = Gson().fromJson(userJsonString, User::class.java)

        enterShoppingItem = findViewById(R.id.enterShoppingItemText)
        enterShoppingQuantity = findViewById(R.id.enterShoppingItemQuantity)
        addShoppingItemDoneButton = findViewById(R.id.addNewShoppingItemDoneButton)

        addShoppingItemDoneButton.setOnClickListener {
            newShoppingItemName = enterShoppingItem.text.toString()
            newShoppingItemQuantity = enterShoppingQuantity.text.toString().toInt()
            var newShoppingFood = Food(newShoppingItemName, null, newShoppingItemQuantity)
            currentUser.shoppingList.add(newShoppingFood)

            val db = DataBaseHelper(this)
            db.insertShoppingData(newShoppingFood, currentUser.username!!)

            val jsonString = Gson().toJson(currentUser)
            val userEditor = userSharedPreferences.edit()
            userEditor.putString("USER", jsonString)
            userEditor.apply()

            startActivity(Intent(this, Grocery::class.java))
        }
    }

}
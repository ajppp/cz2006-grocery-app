package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.classes.User
import com.google.gson.Gson

class Grocery : AppCompatActivity() {

    companion object {
        private const val TAG = "Grocery"
    }

    var currentUser: User? = null

    private lateinit var userSharedPreferences: SharedPreferences

    private lateinit var shoppingListRvBoard: RecyclerView
    private lateinit var purchasedListRvBoard: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery)

        shoppingListRvBoard = findViewById(R.id.shoppingListRvBoard)
        purchasedListRvBoard = findViewById(R.id.purchasedListRvBoard)

        userSharedPreferences = getSharedPreferences("USER_REF", Context.MODE_PRIVATE)
        val userJsonString = userSharedPreferences.getString("USER", "")
        Log.i(TAG, "test $userJsonString")
        currentUser = Gson().fromJson(userJsonString, User::class.java)

        // currentUser = intent.getParcelableExtra<User>("CURRENT USER")
        var userShoppingList = currentUser!!.shoppingList
        var userPurchasedList = currentUser!!.purchasedList

        shoppingListRvBoard.adapter = ShoppingListAdapter(this, userShoppingList) {
            currentUser!!.purchasedList.add(currentUser!!.shoppingList[it])
            currentUser!!.shoppingList.removeAt(it)
        }
        purchasedListRvBoard.adapter = PurchasedListAdapter(this, userPurchasedList)

        shoppingListRvBoard.setHasFixedSize(true)
        purchasedListRvBoard.setHasFixedSize(true)
        shoppingListRvBoard.layoutManager = GridLayoutManager(this, 1)
        purchasedListRvBoard.layoutManager = GridLayoutManager(this, 1)
    }

    override fun onStop() {
        super.onStop()
        val intent = Intent(this, MainActivity::class.java)

        val jsonString = Gson().toJson(currentUser)
        val userEditor = userSharedPreferences.edit()
        userEditor.putString("USER", jsonString)
        userEditor.apply()

        Log.i(TAG, "stopped grocery, starting main activity")
        startActivity(intent)
    }

}
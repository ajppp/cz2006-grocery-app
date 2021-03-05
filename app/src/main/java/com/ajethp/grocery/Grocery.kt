package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.classes.User
import com.google.gson.Gson

class Grocery : AppCompatActivity() {

    companion object {
        private const val TAG = "Grocery"
    }

    private lateinit var shoppingItemName: String
    private var shoppingItemQuantity = 0

    private lateinit var currentUser: User
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
        currentUser = Gson().fromJson(userJsonString, User::class.java)

        // currentUser = intent.getParcelableExtra<User>("CURRENT USER")
        val userShoppingList = currentUser.shoppingList
        val userPurchasedList = currentUser.purchasedList

        shoppingListRvBoard.adapter = ShoppingListAdapter(this, userShoppingList) {
            // remove item from shopping list and add it to purchased list
            currentUser.purchasedList.add(currentUser.shoppingList[it])
            currentUser.shoppingList.removeAt(it)
            purchasedListRvBoard.adapter?.notifyDataSetChanged()
            val jsonString = Gson().toJson(currentUser)
            val userEditor = userSharedPreferences.edit()
            userEditor.putString("USER", jsonString)
            userEditor.apply()
        }
        purchasedListRvBoard.adapter = PurchasedListAdapter(this, userPurchasedList) {
            var movedFoodItem = currentUser.purchasedList[it]
            currentUser.inventoryList.add(movedFoodItem)
            currentUser.purchasedList.removeAt(it)
            purchasedListRvBoard.adapter?.notifyDataSetChanged()
            val jsonString = Gson().toJson(currentUser)
            val userEditor = userSharedPreferences.edit()
            userEditor.putString("USER", jsonString)
            userEditor.apply()
            startActivity(Intent(this, MovePurchasedItemToInventory::class.java))
        }

        shoppingListRvBoard.setHasFixedSize(true)
        purchasedListRvBoard.setHasFixedSize(true)
        shoppingListRvBoard.layoutManager = GridLayoutManager(this, 1)
        purchasedListRvBoard.layoutManager = GridLayoutManager(this, 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inventory_add_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.mi_add_inventory -> { startActivity(Intent(this, AddNewShoppingItem::class.java)) }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        val jsonString = Gson().toJson(currentUser)
        val userEditor = userSharedPreferences.edit()
        userEditor.putString("USER", jsonString)
        userEditor.apply()
    }

}
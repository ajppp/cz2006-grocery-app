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
import com.ajethp.grocery.helper.DataBaseHelper
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

        val db = DataBaseHelper(this)
        val username = getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")
        val currentUser : User = db.readUserData(username!!)
        val userShoppingList = currentUser.shoppingList
        val userPurchasedList = currentUser.purchasedList

        shoppingListRvBoard.adapter = ShoppingListAdapter(this, userShoppingList) {
            // remove item from shopping list and add it to purchased list
            db.deleteShoppingData(currentUser.shoppingList[it], username)
            db.insertPurchasedData(currentUser.shoppingList[it], username)
            currentUser.purchasedList.add(currentUser.shoppingList[it])
            currentUser.shoppingList.removeAt(it)
            purchasedListRvBoard.adapter?.notifyDataSetChanged()

        }
        purchasedListRvBoard.adapter = PurchasedListAdapter(this, userPurchasedList) {
            val movedFoodItem = currentUser.purchasedList[it]
            val intent = Intent(this, MovePurchasedItemToInventory::class.java)
            intent.putExtra("NAME", movedFoodItem.foodName)
            intent.putExtra("QUANTITY", movedFoodItem.quantity)
            startActivity(intent)
        }

        shoppingListRvBoard.setHasFixedSize(true)
        purchasedListRvBoard.setHasFixedSize(true)
        shoppingListRvBoard.layoutManager = GridLayoutManager(this, 2)
        purchasedListRvBoard.layoutManager = GridLayoutManager(this, 2)
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

}
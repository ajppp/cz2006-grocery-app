package com.ajethp.grocery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.classes.User

class Grocery : AppCompatActivity() {

    companion object {
        private const val TAG = "Grocery"
    }

    private lateinit var shoppingListRvBoard: RecyclerView
    private lateinit var purchasedListRvBoard: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery)

        shoppingListRvBoard = findViewById(R.id.shoppingListRvBoard)
        purchasedListRvBoard = findViewById(R.id.purchasedListRvBoard)

        var currentUser = intent.getParcelableExtra<User>("CURRENT USER")
        var userShoppingList = currentUser!!.shoppingList
        var userPurchasedList = currentUser!!.purchasedList

        // TODO("replace 8 with the actual number of items in user's list")
        // since we are not creating two separate classes, perhaps add a Boolean to
        // indicate whether or not it is purchased, this will also make things easier
        // to be reversed if needed. Hence, numShoppingItems + numPurchasedItems = Grocery.size()

        // actually the above idea sounds good in theory but it's a pain in the ass to code
        // change the idea

        shoppingListRvBoard.adapter = ShoppingListAdapter(this, userShoppingList)
        purchasedListRvBoard.adapter = PurchasedListAdapter(this, 2)

        shoppingListRvBoard.setHasFixedSize(true)
        purchasedListRvBoard.setHasFixedSize(true)
        shoppingListRvBoard.layoutManager = GridLayoutManager(this, 1)
        purchasedListRvBoard.layoutManager = GridLayoutManager(this, 1)
    }
}
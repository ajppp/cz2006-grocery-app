package com.ajethp.grocery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        // TODO("replace 8 with the actual number of items in user's list")
        // since we are not creating two separate classes, perhaps add a Boolean to
        // indicate whether or not it is purchased, this will also make things easier
        // to be reversed if needed. Hence, numShoppingItems + numPurchasedItems = Grocery.size()
        shoppingListRvBoard.adapter = ShoppingListAdapter(this, 5)
        purchasedListRvBoard.adapter = PurchasedListAdapter(this, 2)

        shoppingListRvBoard.setHasFixedSize(true)
        purchasedListRvBoard.setHasFixedSize(true)
        shoppingListRvBoard.layoutManager = GridLayoutManager(this, 1)
        purchasedListRvBoard.layoutManager = GridLayoutManager(this, 1)
    }
}
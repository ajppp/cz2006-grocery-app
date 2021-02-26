package com.ajethp.grocery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Inventory : AppCompatActivity() {
    companion object{
        private const val TAG = "Inventory"
    }

    private lateinit var inventoryRvBoard: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        inventoryRvBoard = findViewById(R.id.inventoryRvBoard)

        // TODO("replace 8 with the actual number of items in user's inventory")
        // Recycler View is scrollable so there's no need to change anything
        inventoryRvBoard.adapter = InventoryAdapter(this, 8)
        inventoryRvBoard.setHasFixedSize(true)
        // spanCount is the number of columns
        inventoryRvBoard.layoutManager = GridLayoutManager(this, 1)
    }

}
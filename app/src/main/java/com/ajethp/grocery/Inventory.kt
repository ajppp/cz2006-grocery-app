package com.ajethp.grocery

import android.app.DatePickerDialog
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.classes.User
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.util.*

class Inventory : AppCompatActivity() {
    companion object{
        private const val TAG = "Inventory"
    }

    private lateinit var currentUser: User
    private lateinit var userSharedPreferences: SharedPreferences

    private lateinit var inventoryRvBoard: RecyclerView
    private lateinit var inventoryClRoot: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        inventoryRvBoard = findViewById(R.id.inventoryRvBoard)
        inventoryClRoot = findViewById(R.id.inventoryClRoot)
        userSharedPreferences = getSharedPreferences("USER_REF", Context.MODE_PRIVATE)
        val userJsonString = userSharedPreferences.getString("USER", "")
        currentUser = Gson().fromJson(userJsonString, User::class.java)

        var userInventory = currentUser.inventoryList

        inventoryRvBoard.adapter = InventoryAdapter(this, userInventory)
        inventoryRvBoard.setHasFixedSize(true)
        inventoryRvBoard.layoutManager = GridLayoutManager(this, 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inventory_add_item, menu)
        return true
    }

    // method to add new inventory item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.mi_add_inventory -> { startActivity(Intent(this, AddNewInventoryItem::class.java)) }
        }

        return super.onOptionsItemSelected(item)
    }
}
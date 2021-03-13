package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.gson.Gson

class Inventory : AppCompatActivity() {
    companion object {
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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inventory_add_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_add_inventory -> {
                startActivity(Intent(this, AddNewInventoryItem::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        val currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)
        currentUser.inventoryList.sortBy { it.expiryDate }
        inventoryRvBoard.adapter = InventoryAdapter(this, currentUser.inventoryList) {
            val inputNewQuantity = EditText(this)
            inputNewQuantity.setInputType(InputType.TYPE_CLASS_NUMBER)
            AlertDialog.Builder(this)
                    .setTitle("Enter new quantity")
                    .setView(inputNewQuantity)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK") {_, _ ->
                        val editedQuantity = inputNewQuantity.text.toString().toInt()
                        DataBaseHelper(this).modifyInventoryData(currentUser.inventoryList[it], editedQuantity, currentUser.username!!)
                        currentUser.inventoryList[it].quantity = editedQuantity
                        inventoryRvBoard.adapter?.notifyDataSetChanged()
                    }
                    .show()
        }
        inventoryRvBoard.setHasFixedSize(true)
        inventoryRvBoard.layoutManager = GridLayoutManager(this, 1)
    }
}
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

/**
 * This class is an activity class which
 * implements the Inventory. It is the
 * Inventory page of our app
 *
 * @author jethro
 * @author claudia
 */
class Inventory : AppCompatActivity() {
    companion object {
        private const val TAG = "Inventory"
    }

    private lateinit var inventoryRvBoard: RecyclerView
    private lateinit var inventoryClRoot: ConstraintLayout

    // a function that is called when first creating the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        inventoryRvBoard = findViewById(R.id.inventoryRvBoard)
        inventoryClRoot = findViewById(R.id.inventoryClRoot)
    }

    // function to handle the creation of the option menu - in our case the plus button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inventory_add_item, menu)
        return true
    }

    // handle what happens when the menu item is selected
    // cloud, you can add more menu buttons and place them here to handle the logic
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_add_inventory -> {
                startActivity(Intent(this, AddNewInventoryItem::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // handle what happens after the activity is started after creation
    override fun onStart() {
        super.onStart()

        // create a currentUser instance by reading the database using the username that was inserted when logging in
        val currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)
        currentUser.sortInventory()
        // handle the display of the inventory items
        // the InventoryAdapter takes the context, the inventoryList and a function as its constructor parameters
        inventoryRvBoard.adapter = InventoryAdapter(this, currentUser.inventoryList) {
            val inputNewQuantity = EditText(this)
            inputNewQuantity.inputType = InputType.TYPE_CLASS_NUMBER
            // create an alert dialog to ask user to enter the new quantity
            // TODO("cloud, if you so wish, you can modify this such that you ask them for how much they used")
            AlertDialog.Builder(this)
                    .setTitle("Enter new quantity")
                    .setView(inputNewQuantity)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK") {_, _ ->
                        val editedQuantity = inputNewQuantity.text.toString().toInt()
                        if (editedQuantity < currentUser.inventoryList[it].quantity) {
                            if (editedQuantity == 0) {
                                DataBaseHelper(this).deleteInventoryData(currentUser.inventoryList[it], currentUser.username!!)
                                currentUser.inventoryList.removeAt(it)
                                inventoryRvBoard.adapter?.notifyDataSetChanged()
                                Snackbar.make(inventoryClRoot, "Removed item", Snackbar.LENGTH_LONG).show()
                            } else {
                                DataBaseHelper(this).modifyInventoryData(currentUser.inventoryList[it], editedQuantity, currentUser.username!!)
                                currentUser.inventoryList[it].quantity = editedQuantity
                                inventoryRvBoard.adapter?.notifyDataSetChanged()
                                Snackbar.make(inventoryClRoot, "Quantity reduction successful!", Snackbar.LENGTH_LONG).show()
                            }
                        } else {
                            Snackbar.make(inventoryClRoot, "Input quantity more than original quantity", Snackbar.LENGTH_LONG).show()
                        }
                    }
                    .show()
        }
        inventoryRvBoard.setHasFixedSize(true)
        inventoryRvBoard.layoutManager = LinearLayoutManager(this)
    }
}
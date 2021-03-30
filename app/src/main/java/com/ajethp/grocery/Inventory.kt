package com.ajethp.grocery

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
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
    private lateinit var sortBy: String

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
            R.id.mi_filter -> {
                showFilterDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // handle what happens after the activity is started after creation
    override fun onStart() {
        super.onStart()
        val currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)
        currentUser.sortInventory()
        sortBy = "DATE"
        setupInventory(currentUser)
    }

    private fun setupInventory(currentUser: User) {
        // create a currentUser instance by reading the database using the username that was inserted when logging in
        // handle the display of the inventory items
        // the InventoryAdapter takes the context, the inventoryList and a function as its constructor parameters
        inventoryRvBoard.adapter = InventoryAdapter(this, currentUser.inventoryList) {
            val inputNewQuantity = EditText(this)
            inputNewQuantity.inputType = InputType.TYPE_CLASS_NUMBER
            // create an alert dialog to ask user to enter the quantity used
            AlertDialog.Builder(this)
                .setTitle("Enter quantity used")
                .setView(inputNewQuantity)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK") {_, _ ->
                    val quantityUsed = inputNewQuantity.text.toString().toInt()
                    if (quantityUsed < currentUser.inventoryList[it].quantity) {
                        val newQuantity = currentUser.inventoryList[it].quantity - quantityUsed
                        DataBaseHelper(this).modifyInventoryData(currentUser.inventoryList[it], newQuantity, currentUser.username!!)
                        currentUser.inventoryList[it].quantity = newQuantity
                        inventoryRvBoard.adapter?.notifyDataSetChanged()
                        Snackbar.make(inventoryClRoot, "Quantity reduction successful!", Snackbar.LENGTH_LONG).show()
                    }
                    else if (quantityUsed > currentUser.inventoryList[it].quantity) {
                        Snackbar.make(inventoryClRoot, "Quantity used more than available quantity! Removing item...", Snackbar.LENGTH_LONG).show()
                        DataBaseHelper(this).deleteInventoryData(currentUser.inventoryList[it], currentUser.username!!)
                        currentUser.inventoryList.removeAt(it)
                        inventoryRvBoard.adapter?.notifyDataSetChanged()
                        Snackbar.make(inventoryClRoot, "Removed item", Snackbar.LENGTH_LONG).show()
                    }
                }
                .show()
        }
        inventoryRvBoard.setHasFixedSize(true)
        inventoryRvBoard.layoutManager = LinearLayoutManager(this)
    }

    private fun showFilterDialog() {
        val radioGroupView = LayoutInflater.from(this).inflate(R.layout.dialog_inventory_filter, null)
        val radioGroupSize = radioGroupView.findViewById<RadioGroup>(R.id.radioGroup)
        when (sortBy) {
            "DATE" -> radioGroupSize.check(R.id.rbDate)
            "NAME" -> radioGroupSize.check(R.id.rbName)
            "QUANTITY" -> radioGroupSize.check(R.id.rbQuantity)
        }
        val currentUser = DataBaseHelper(this).readUserData(getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")!!)
        showAlertDialog("Choose how to filter", radioGroupView) {
              when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbName -> {
                    currentUser.sortInventoryByName()
                    sortBy = "NAME"
                }
                R.id.rbQuantity -> {
                    currentUser.sortInventoryByQuantity()
                    sortBy = "QUANTITY"
                }
                R.id.rbDate -> {
                    currentUser.sortInventory()
                    sortBy = "DATE"
                }
            }
            setupInventory(currentUser)
        }
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("cancel", null)
            .setPositiveButton("OK") {_, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }
}

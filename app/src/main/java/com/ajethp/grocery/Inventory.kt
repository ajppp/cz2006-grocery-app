package com.ajethp.grocery

import android.app.DatePickerDialog
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
import java.util.*

class Inventory : AppCompatActivity() {
    companion object{
        private const val TAG = "Inventory"
    }

    private lateinit var inventoryRvBoard: RecyclerView
    private lateinit var inventoryClRoot: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        inventoryRvBoard = findViewById(R.id.inventoryRvBoard)
        inventoryClRoot = findViewById(R.id.inventoryClRoot)

        var currentUser = intent.getParcelableExtra<User>("CURRENT USER")
        var userInventory = currentUser!!.inventoryList

        // ("replace 8 with the actual number of items in user's inventory") -> done
        // Recycler View is scrollable so there's no need to change anything
        inventoryRvBoard.adapter = InventoryAdapter(this, userInventory.size, userInventory)
        inventoryRvBoard.setHasFixedSize(true)
        // spanCount is the number of columns
        inventoryRvBoard.layoutManager = GridLayoutManager(this, 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inventory_add_item, menu)
        return true
    }

    // TODO("rethink the entire method... perhaps using a new activity is better")
    // method to add new inventory item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.mi_add_inventory -> {
                val c = Calendar.getInstance()
                var year = c.get(Calendar.YEAR)
                var month = c.get(Calendar.YEAR)
                var day = c.get(Calendar.YEAR)
                val inputInventoryName = EditText(this)
                showNameAlertDialog("Enter item name", inputInventoryName, View.OnClickListener {
                    val newInventoryName = inputInventoryName.text.toString()
                    Log.i(TAG, "entered $newInventoryName")
                    val inputInventoryQuantity = EditText(this)
                    showQuantityAlertDialog("Enter the quantity of $newInventoryName", inputInventoryQuantity, View.OnClickListener {
                        // TODO("add error checking if it is not an int")
                        val newInventoryQuantity = inputInventoryQuantity.text.toString().toInt()
                        Log.i(TAG, "entered $newInventoryQuantity")
                        // TODO("can only select future date")
                        showHasExpiryAlertDialog("Do you see an expiry date?", null, {
                                     Log.i(TAG, "entered the negative button")
                            //no expiry date code here
                            // access database
                            // firebase or sqlite?
                        },
                                {
                                    Log.i(TAG, "entered positive button")
                                    // sees expiry date
                                    var timeSet : String? = null
                                    val dateSetListener = DatePickerDialog.OnDateSetListener { _, cyear, monthOfYear, dayOfMonth ->
                                            c.set(Calendar.YEAR, cyear)
                                            c.set(Calendar.MONTH, monthOfYear)
                                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                        }

                                    val dialog = DatePickerDialog(this, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                                    dialog.datePicker.minDate = c.timeInMillis
                                    dialog.show()
                                    // STILL NOT WORKING
                                    Snackbar.make(inventoryClRoot, "You have successfully added $newInventoryQuantity of $newInventoryName", Snackbar.LENGTH_LONG).show()
                                    val enteredMonth = c.get(Calendar.MONTH)
                                    Log.i(TAG, "entered $enteredMonth")
                                    // TODO("add item with info to the user db")
                                })
                    })
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showHasExpiryAlertDialog(title: String, view: View?, negativeClickListener: View.OnClickListener, positiveClickListener: View.OnClickListener ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setView(view)
        builder.setNegativeButton("No") {_, _ ->
            // TODO("access database")
            negativeClickListener.onClick(null)
        }
        builder.setPositiveButton("Yes") {_, _ ->
            positiveClickListener.onClick(null)
        }
        builder.show()

    }

    private fun showQuantityAlertDialog(title: String, view:View, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Enter quantity") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun showNameAlertDialog(title: String, view: View, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Enter quantity") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

}
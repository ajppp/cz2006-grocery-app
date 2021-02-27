package com.ajethp.grocery

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inventory_add_item, menu)
        return true
    }

    // method to add new inventory item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.mi_add_inventory -> {
                val inputInventoryName = EditText(this)
                showNameAlertDialog("Enter item name", inputInventoryName, View.OnClickListener {
                    val newInventoryName = inputInventoryName.text.toString()
                    Log.i(TAG, "entered $newInventoryName")
                    val inputInventoryQuantity = EditText(this)
                    showQuantityAlertDialog("Enter the quantity of $newInventoryName", inputInventoryQuantity, View.OnClickListener {
                        // TODO("add error checking if it is not an int")
                        val newInventoryQuantity = inputInventoryQuantity.text.toString().toInt()
                        Log.i(TAG, "entered $newInventoryQuantity")
                        val inputInventoryExpiryDate = DatePicker(this)
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
                                    showExpiryDateDialog("Enter the expiry date you see for $newInventoryName", inputInventoryExpiryDate, View.OnClickListener {
                                        // NOT YET WORKING
                                        // HOW TO
                                        val newInventoryExpiryDate = inputInventoryExpiryDate
                                        Log.i(TAG, "entered date $inputInventoryExpiryDate")
                                    })
                                })
                    })
                } )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // METHOD NOT YET WORKING
    // HOW TO RETURN DATE?
    private fun showExpiryDateDialog(title: String, view: View, onClickListener: View.OnClickListener) {
         AlertDialog.Builder(this)
                .setTitle(title)
        .setView(view)
        .setNegativeButton("Cancel", null)
                .setPositiveButton("Enter date") { _, _ ->
                   onClickListener.onClick(null)
          }.show()
        // val c = Calendar.getInstance()
        // val year = c.get(Calendar.YEAR)
        // val month = c.get(Calendar.MONTH)
        // val day = c.get(Calendar.DAY_OF_MONTH)
        // DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth -> }, year, month, day).show()
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
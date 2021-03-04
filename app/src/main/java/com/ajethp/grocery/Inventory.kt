package com.ajethp.grocery

import android.app.DatePickerDialog
import android.content.Context
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

        // ("replace 8 with the actual number of items in user's inventory") -> done
        // Recycler View is scrollable so there's no need to change anything
        inventoryRvBoard.adapter = InventoryAdapter(this, userInventory)
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

        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        var newInventoryDate: String = ""
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
                        // TODO("can only select future date")
                        showHasExpiryAlertDialog("Do you see an expiry date?", null, {
                                     Log.i(TAG, "entered the negative button")
                            //no expiry date code here
                        },
                                {
                                    Log.i(TAG, "entered positive button")
                                    // sees expiry date
                                    val dateSetListener = DatePickerDialog.OnDateSetListener { view, cyear, monthOfYear, dayOfMonth ->
                                            newInventoryDate.plus(cyear).plus(monthOfYear).plus(dayOfMonth)
                                            c.set(Calendar.YEAR, cyear)
                                            c.set(Calendar.MONTH, monthOfYear)
                                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                        }

                                    val dialog = DatePickerDialog(this, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                                    dialog.datePicker.minDate = c.timeInMillis
                                    dateSetListener.onDateSet(dialog.datePicker, year , month, day)
                                    dialog.show()
                                    // STILL NOT WORKING
                                    // the code below runs even before the ok button is clicked
                                    Snackbar.make(inventoryClRoot, "You have successfully added $newInventoryQuantity of $newInventoryName", Snackbar.LENGTH_LONG).show()
                                    Log.i(TAG, "entered $month button")
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
package com.ajethp.grocery

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.ajethp.grocery.classes.Food
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.gson.Gson
import java.util.*

class AddNewInventoryItem : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    companion object {
        private const val TAG = "AddNewInventoryItem"
    }

    private var day = 0
    private var month = 0
    private var year = 0

    private lateinit var newInventoryItemName: String
    private var newInventoryItemQuantity: Int = 0
    private lateinit var expiryDate: String

    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0

    private lateinit var enterFoodItem: EditText
    private lateinit var enterFoodQuantity: EditText
    private lateinit var expiryDateSwitch: Switch
    private lateinit var addNewInventoryNextButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_inventory_item)

        enterFoodItem = findViewById(R.id.enterFoodItemText)
        enterFoodQuantity = findViewById(R.id.enterFoodQuantityText)
        expiryDateSwitch = findViewById(R.id.expiryDateSwitch)
        addNewInventoryNextButton = findViewById(R.id.addNewInventoryNextButton)

        var hasExpiryDate: Boolean = false
        expiryDateSwitch.setOnCheckedChangeListener { _, isChecked -> hasExpiryDate = isChecked }

        addNewInventoryNextButton.setOnClickListener {
            newInventoryItemName = enterFoodItem.text.toString()
            newInventoryItemQuantity = enterFoodQuantity.text.toString().toInt()
            Log.i(TAG,"$newInventoryItemQuantity of $newInventoryItemName, $hasExpiryDate" )
            if (hasExpiryDate){
                pickDate()
            } else {
                // use database
            }
        }

    }

    private fun getDateCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickDate() {
        getDateCalendar()
        val dpd = DatePickerDialog(this, this, year, month, day)
        dpd.datePicker.minDate = Calendar.getInstance().timeInMillis
        dpd.show()
    }


    /**
     * @param view the picker associated with the dialog
     * @param year the selected year
     * @param month the selected month (0-11 for compatibility with
     * [Calendar.MONTH])
     * @param dayOfMonth the selected day of the month (1-31, depending on
     * month)
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year
        val dayOfMonthString: String = if(savedDay < 10) {
            "0$savedDay"
        } else {
            savedDay.toString()
        }

        val monthString: String = if (savedMonth < 10) {
            "0$savedMonth"
        } else {
            savedMonth.toString()
        }

        expiryDate = "$savedYear-$monthString-$dayOfMonthString"

        val newInventoryFood = Food(newInventoryItemName, expiryDate, newInventoryItemQuantity)
        val username = getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")
        DataBaseHelper(this).insertInventoryData(newInventoryFood, username!!)

        startActivity(Intent(this, Inventory::class.java))
    }
}
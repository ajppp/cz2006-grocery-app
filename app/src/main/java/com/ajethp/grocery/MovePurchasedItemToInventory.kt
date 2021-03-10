package com.ajethp.grocery

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import com.ajethp.grocery.classes.Food
import com.ajethp.grocery.classes.User
import com.ajethp.grocery.helper.DataBaseHelper
import com.google.gson.Gson
import java.util.*

class MovePurchasedItemToInventory : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    companion object{
        private const val TAG = "MoveItem"
    }
    var day = 0
    var month = 0
    var year = 0
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    private lateinit var expiryDate: String

    private lateinit var userSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_purchased_item_to_inventory)
        pickDate()
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
        dpd.setOnCancelListener {
            startActivity(Intent(this, Grocery::class.java))
        }
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
        val foodName = intent.getStringExtra("NAME")
        val foodQuantity= intent.getIntExtra("QUANTITY", 0)
        val db = DataBaseHelper(this)
        val username = getSharedPreferences("USER_REF", Context.MODE_PRIVATE).getString("USERNAME", "")
        val movedFoodItem = Food(foodName!!, expiryDate, foodQuantity)
        db.deletePurchasedData(movedFoodItem, username!!)
        db.insertInventoryData(movedFoodItem, username)
        startActivity(Intent(this, Grocery::class.java))
    }
}
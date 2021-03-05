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

    private lateinit var currentUser: User
    private lateinit var userSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_purchased_item_to_inventory)

        userSharedPreferences = getSharedPreferences("USER_REF", Context.MODE_PRIVATE)
        val userJsonString = userSharedPreferences.getString("USER", "")
        currentUser = Gson().fromJson(userJsonString, User::class.java)

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

        expiryDate = "$savedDay-$savedMonth-$savedYear"

        val size = currentUser.inventoryList.size
        var movedFoodItem: Food = currentUser.inventoryList.elementAt(size - 1)
        movedFoodItem.expiryDate = expiryDate
        val jsonString = Gson().toJson(currentUser)
        Log.i(TAG, jsonString)
        val userEditor = userSharedPreferences.edit()
        userEditor.putString("USER", jsonString)
        userEditor.apply()
        startActivity(Intent(this, Grocery::class.java))
    }
}
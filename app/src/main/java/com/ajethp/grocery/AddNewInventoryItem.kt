package com.ajethp.grocery

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import com.ajethp.grocery.classes.Food
import com.google.android.material.switchmaterial.SwitchMaterial

class AddNewInventoryItem : AppCompatActivity() {

    companion object {
        private const val TAG = "AddNewInventoryItem"
    }

    private lateinit var foodSharedPreferences: SharedPreferences

    private lateinit var enterFoodItem: EditText
    private lateinit var enterFoodQuantity: EditText
    private lateinit var expiryDateSwitch: Switch
    private lateinit var addNewInventoryNextButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_inventory_item)

        foodSharedPreferences = getSharedPreferences("FOOD", Context.MODE_PRIVATE)

        enterFoodItem = findViewById(R.id.enterFoodItemText)
        enterFoodQuantity = findViewById(R.id.enterFoodQuantityText)
        expiryDateSwitch = findViewById(R.id.expiryDateSwitch)
        addNewInventoryNextButton = findViewById(R.id.addNewInventoryNextButton)

        var hasExpiryDate: Boolean = false
        expiryDateSwitch.setOnCheckedChangeListener { _, isChecked -> hasExpiryDate = isChecked }

        addNewInventoryNextButton.setOnClickListener {
            val newInventoryItemName = enterFoodItem.text.toString()
            val newInventoryItemQuantity = enterFoodQuantity.text.toString().toInt()
            Log.i(TAG,"$newInventoryItemQuantity of $newInventoryItemName, $hasExpiryDate" )
            val newInventoryItem = Food(newInventoryItemName, null, newInventoryItemQuantity)

            if (hasExpiryDate){
                // Open alert dialog>
            } else {
                // use database
            }
        }


    }
}
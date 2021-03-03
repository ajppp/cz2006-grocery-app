package com.ajethp.grocery.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val username: String,
        var dietaryRestriction: MutableList<Boolean> = mutableListOf(false, false, false, false, false),
        var isInFamily : Boolean = false,
        var inventoryList:MutableList<Food> = ArrayList(),
        var shoppingList:MutableList<Food> = ArrayList(),
        var purchasedList:MutableList<Food> = ArrayList()
) : Parcelable {
    // use safe calls to check if null when accessing restrictions
        private fun addFoodToInventory(newInventoryFood: Food) = inventoryList.add(newInventoryFood)

}
package com.ajethp.grocery.classes

import java.sql.Types.NULL

/**
 * This class implements the Food entity with
 * the attributes user email, user name,
 * password, restriction, familyId,
 * isInFamily boolean, an inventory list of food,
 * a shopping list of food and a purchased list
 * of food
 *
 * @author jethro
 */
data class User(
        val userEmail: String?,
        val username: String?,
        val password: String?,
        var dietaryRestriction: MutableList<Boolean> = mutableListOf(false, false, false, false, false, false),
        // default value for no family is 0
        var familyId : String? = null,
        var isInFamily : Boolean = false,
        var inventoryList:MutableList<Food> = ArrayList(),
        var shoppingList:MutableList<Food> = ArrayList(),
        var purchasedList:MutableList<Food> = ArrayList()
){
    /**
     * This method sorts the inventory of the user
     * based on the expiry date. This allows us to
     * display the user's inventory based on the item
     * that is closest to expiry and use them in the
     * suggested recipe query
     */
    fun sortInventory() = this.inventoryList.sortBy {it.expiryDate}
    fun sortInventoryByName() = this.inventoryList.sortBy { it.foodName }
    fun sortInventoryByQuantity() = this.inventoryList.sortByDescending { it.quantity }
}


package com.ajethp.grocery.classes

class User(
        val username: String,
        var dietaryRestriction : String?,
        var isInFamily : Boolean = false,
        var inventoryList:MutableList<Food> = ArrayList(),
        var groceryList:MutableList<Food> = ArrayList()
) {
    // use safe calls to check if null when accessing restrictions
        private fun addFoodToInventory(newInventoryFood: Food) = inventoryList.add(newInventoryFood)

}

data class AppUser(
        val username: String,
        val hashedPassword: String
)

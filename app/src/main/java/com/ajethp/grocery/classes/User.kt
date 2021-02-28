package com.ajethp.grocery.classes

class User(
        val username: String,
        var dietaryRestriction : String?,
        var isInFamily : Boolean = false,
        var inventoryList:ArrayList<Food> = ArrayList(),
        var groceryList:ArrayList<Food> = ArrayList()
) {
    // use safe calls to check if null when accessing restrictions
}

data class AppUser(
        val username: String,
        val hashedPassword: String
)

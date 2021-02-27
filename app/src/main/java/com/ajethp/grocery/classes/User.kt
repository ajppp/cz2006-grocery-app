package com.ajethp.grocery.classes

class User(
        val username: String,
        var hashedPassword: String,
        var dietaryRestriction : String?,
        var isInFamily : Boolean = false,
        var inventoryList:ArrayList<Food> = ArrayList(),
        var groceryList:ArrayList<Food>
) {
    // use safe calls to check if null when accessing restrictions

}
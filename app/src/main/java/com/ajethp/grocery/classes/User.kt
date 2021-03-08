package com.ajethp.grocery.classes

data class User(
        val userEmail: String?,
        val username: String?,
        val password: String?,
        var dietaryRestriction: MutableList<Boolean> = mutableListOf(false, false, false, false, false, false),
        // default value for no family is 0
        var familyId : Int = 0,
        var isInFamily : Boolean = false,
        var inventoryList:MutableList<Food> = ArrayList(),
        var shoppingList:MutableList<Food> = ArrayList(),
        var purchasedList:MutableList<Food> = ArrayList()
)


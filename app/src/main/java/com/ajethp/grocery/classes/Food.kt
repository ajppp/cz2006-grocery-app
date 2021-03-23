package com.ajethp.grocery.classes

/**
 * This class implements the Food entity with
 * the attributes foodName, expiry date and
 * quantity
 *
 * @author jethro
 */
data class Food (
        val foodName: String,
        var expiryDate: String?,
        var quantity: Int,
)

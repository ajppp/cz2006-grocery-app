package com.ajethp.grocery.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class Food (
        val foodName: String,
        // make expiryDate nullable for food in shopping list?
        var expiryDate: String?,
        var quantity: Int,
) : Parcelable

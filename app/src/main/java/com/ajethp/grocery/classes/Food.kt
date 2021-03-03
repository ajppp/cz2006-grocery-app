package com.ajethp.grocery.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Food (
    val foodName: String,
    var expiryDate: Date,
    var quantity: Int
) : Parcelable

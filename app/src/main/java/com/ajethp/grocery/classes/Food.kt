package com.ajethp.grocery.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class Food (
        val foodName: String,
        var expiryDate: LocalDate,
        var quantity: Int
) : Parcelable

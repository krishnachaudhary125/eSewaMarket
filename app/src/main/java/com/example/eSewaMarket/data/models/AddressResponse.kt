package com.example.eSewaMarket.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressResponse (

    val id: Long,
    val fullName: String,
    val phone: String,
    val province: String,
    val district: String,
    val postalCode: String,
    val addressName: String,
    val isDefaultAddress: Boolean,
    val isBillingAddress: Boolean,
    val label: String?,
    val landmark: String?
): Parcelable
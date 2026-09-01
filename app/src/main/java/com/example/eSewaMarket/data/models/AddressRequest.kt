package com.example.eSewaMarket.data.models

data class AddressRequest(

    val fullName: String,
    val phone: String,
    val addressName: String,
    val formattedAddress: String?,
    val idDefaultAddress: Boolean,
    val isBillingAddress: Boolean,
    val label: String?
)

package com.example.eSewaMarket.data.models

data class AddressResponse (

    val id: Long,
    val fullName: String,
    val phone: String,
    val province: String,
    val district: String,
    val city: String,
    val postalCode: String,
    val addressName: String,
    val isDefaultAddress: Boolean,
    val isBillingAddress: Boolean,
    val label: String?
)
package com.example.eSewaMarket.data.local.entity

import androidx.room3.Entity

@Entity(
    tableName = "addresses",
    primaryKeys = ["id", "userId"]
)
data class AddressEntity(

    val id: Long,
    val userId: Long,
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

package com.example.eSewaMarket.data.local.entity

import androidx.room3.Entity

@Entity(
    tableName = "products",
    primaryKeys = ["productId"]
)
data class ProductEntity (
    val productId: Long,
    val title: String,
    val brand: String,
    val price: Double,
    val thumbnail: String,
    val discountPercentage: Double? = null
)
package com.example.eSewaMarket.data.local.entity

import androidx.room3.Entity

@Entity(
    tableName = "cart",
    primaryKeys = ["userId", "productId"]
)
data class CartEntity(
    val userId: Long,
    val productId: Long,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
)

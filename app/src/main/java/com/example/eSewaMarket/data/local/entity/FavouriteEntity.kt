package com.example.eSewaMarket.data.local.entity

import androidx.room3.Entity

@Entity(
    tableName = "favourite",
    primaryKeys = ["userId", "productId"]
)
data class FavouriteEntity (
    val userId: Long,
    val productId: Long
)
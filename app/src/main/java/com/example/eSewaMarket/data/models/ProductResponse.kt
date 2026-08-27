package com.example.eSewaMarket.data.models

data class ProductResponse(
    val productId: Long,
    val title: String,
    val thumbnail: String,
    val price: Double,
    val quantity: Int,
    val brand: String
)
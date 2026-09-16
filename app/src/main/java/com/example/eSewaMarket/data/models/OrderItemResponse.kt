package com.example.eSewaMarket.data.models

data class OrderItemResponse(
    val id: Long,
    val productId: Long,
    val productTitle: String,
    val productImage: String?,
    val price: Double,
    val quantity: Int,
    val subtotal: Double
)
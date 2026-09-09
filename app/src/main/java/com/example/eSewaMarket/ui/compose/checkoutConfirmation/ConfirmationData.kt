package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import com.example.eSewaMarket.data.models.ProductResponse

data class ConfirmationData(

    val checkoutProducts: List<ProductResponse>,
    val shippingAddress: String,
    val paymentOption: String,
    val totalAmount: Double,
    val taxAmount: Double,
    val deliveryCharge: Double,
    val grandTotal: Double
)
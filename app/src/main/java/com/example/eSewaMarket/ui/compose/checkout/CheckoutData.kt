package com.example.eSewaMarket.ui.compose.checkout

import com.example.eSewaMarket.data.models.ProductResponse

data class CheckoutData(

    val checkoutProducts: List<ProductResponse>,
    val productPrice: Double,
    val totalTax: Double,
    val shippingCharge: Double,
    val totalAmount: Double,
    val itemCount: Int,
    val shippingAddress: String,
    val addressExist: Boolean
)

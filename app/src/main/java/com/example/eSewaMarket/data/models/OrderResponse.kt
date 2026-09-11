package com.example.eSewaMarket.data.models

data class OrderResponse(
    val id: Long,
    val orderNumber: String,
    val orderStatus: String,
    val paymentStatus: String,
    val shippingAddressName: String,
    val shippingCharge: Double,
    val shippingDistrict: String,
    val shippingFullName: String,
    val shippingLandmark: String?,
    val shippingPhone: String,
    val shippingPostalCode: String,
    val shippingProvince: String,
    val subTotal: Double,
    val tax: Double,
    val totalAmount: Double
)
package com.example.eSewaMarket.data.models

data class OrderDetailResponse(
    val id: Long,
    val orderNumber: String,

    val subTotal: Double,
    val shippingCharge: Double,
    val tax: Double,
    val totalAmount: Double,

    val paymentOption: String,
    val paymentStatus: String,
    val orderStatus: String,

    val shippingFullName: String,
    val shippingPhone: String,
    val shippingProvince: String,
    val shippingDistrict: String,
    val shippingPostalCode: String,
    val shippingAddressName: String,
    val shippingLandmark: String?,

    val createdAt: String,

    val products: List<OrderItemResponse>
)
package com.example.eSewaMarket.data.models

data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val category: ProductCategory,
    val thumbnail: String,
    val stock: Int,
    val images: List<String>,
    val brand: String,
    val options: Map<String, List<String>>,
    val rating: Double,
    val reviewCount: Int,
    val discountPercentage: Double ? = null
)
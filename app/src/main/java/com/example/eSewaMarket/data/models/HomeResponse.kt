package com.example.eSewaMarket.data.models

data class HomeResponse(

    val featuredProducts: List<Product>,
    val hotDeals: List<Product>,
    val popularBrandProducts: List<Product>
)

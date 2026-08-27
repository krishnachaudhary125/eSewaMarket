package com.example.eSewaMarket.data.models

data class PageResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Int,
    val number: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int
)
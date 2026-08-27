package com.example.eSewaMarket.data.models

data class Faqs(
    val
    id: Int,
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)

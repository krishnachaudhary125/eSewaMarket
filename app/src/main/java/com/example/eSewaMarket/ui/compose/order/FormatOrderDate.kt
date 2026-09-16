package com.example.eSewaMarket.ui.compose.order

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatOrderDate(date: String): String {
    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern(
        "dd MMM yyyy, h:mm a",
        Locale.ENGLISH
    )

    return LocalDateTime.parse(date, inputFormatter)
        .format(outputFormatter)
}
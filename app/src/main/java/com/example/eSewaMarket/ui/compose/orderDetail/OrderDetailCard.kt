package com.example.eSewaMarket.ui.compose.orderDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun OrderDetailCard(
    orderNo: String,
    orderDate: String,
    orderStatus: String
) {
    Column{
        OrderDetailTop(
            orderNo = orderNo,
            orderDate = orderDate,
            orderStatus = orderStatus
        )
    }
}
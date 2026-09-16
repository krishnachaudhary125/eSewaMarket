package com.example.eSewaMarket.ui.compose.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OrderCard(
    onOrderItemClick: () -> Unit,
    orderNo: String,
    date: String,
    totalNoOfProducts: Int,
    status: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                onClick = onOrderItemClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.TopStart
    ) {

        OrderCardTop(
            orderNo = orderNo,
            date = date,
            totalNoOfProducts = totalNoOfProducts,
            status = status
        )
    }
}
package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun ConfirmationCard(
    modifier: Modifier,
    singleProductQuantity: Int
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Text(
                "Payment Details",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 1.sp,
                color = colorResource(R.color.text_dark_400)
            )

            LazyColumn(
                modifier = Modifier
                    .padding(top = 8.dp)
            ) {
                item {

                    ConfirmationProductDetails(
                        singleProductQuantity = singleProductQuantity
                    )
                }
            }
        }
    }
}
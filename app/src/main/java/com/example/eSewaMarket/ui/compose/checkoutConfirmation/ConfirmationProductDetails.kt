package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun ConfirmationProductDetails(
    singleProductQuantity: Int
) {
    Column(
        modifier = Modifier
    ) {

        Text(
            "Product items(${singleProductQuantity})",
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_dark_300)
        )
    }
}
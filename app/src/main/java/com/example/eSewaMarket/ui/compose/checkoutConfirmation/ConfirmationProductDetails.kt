package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun ConfirmationProductDetails(
    singleProductQuantity: Int,
    productName: String,
    productTotalPrice: Double
) {
    Column(
        modifier = Modifier
    ) {

        Text(
            "Product items(${singleProductQuantity})",
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text_dark_300),
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

       TextRow(
           firstText = "Name",
           secondText = productName
       )

        TextRow(
            firstText = "Price",
            secondText = "Rs. ${"%.2f".format(productTotalPrice)}"
        )


        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(id = R.color.text_dark_100),
            modifier = Modifier
                .padding(
                    bottom = 16.dp
                )
        )
    }
}
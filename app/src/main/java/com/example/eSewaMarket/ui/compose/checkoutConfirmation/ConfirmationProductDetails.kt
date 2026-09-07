package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
            color = colorResource(R.color.text_dark_300)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    bottom = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                "Name",
                fontSize = 14.sp,
                lineHeight = 16.sp,
                letterSpacing = 1.sp,
                color = colorResource(R.color.text_dark_300),
                modifier = Modifier
                    .width(80.dp)
            )

            Text(
                productName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = 1.sp,
                color = colorResource(R.color.text_dark_400)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                "Price",
                fontSize = 14.sp,
                lineHeight = 16.sp,
                letterSpacing = 1.sp,
                color = colorResource(R.color.text_dark_300)
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            Text(
                "Rs. ${"%.2f".format(productTotalPrice)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = 1.sp,
                color = colorResource(R.color.text_dark_400)
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(id = R.color.text_dark_100),
            modifier = Modifier
                .padding(vertical = 16.dp)
        )
    }
}
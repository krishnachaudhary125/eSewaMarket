package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.eSewaMarket.data.models.ProductResponse

@Composable
fun ConfirmationCard(
    modifier: Modifier = Modifier,
    checkoutProducts: List<ProductResponse>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Payment Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(R.color.text_dark_400)
                    )

                    Column(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        checkoutProducts.forEach { product ->

                            ConfirmationProductDetails(
                                singleProductQuantity = product.quantity,
                                productName = product.title,
                                productTotalPrice =
                                    product.price * product.quantity
                            )
                        }
                    }
                }
            }
        }
    }
}
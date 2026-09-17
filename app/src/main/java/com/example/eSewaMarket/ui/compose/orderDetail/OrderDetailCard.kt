package com.example.eSewaMarket.ui.compose.orderDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.OrderItemResponse

@Composable
fun OrderDetailCard(
    orderNo: String,
    orderDate: String,
    orderStatus: String,
    noOfItems: Int,
    products: List<OrderItemResponse>,
    subtotal: Double,
    totalTax: Double,
    shippingCharge: Double,
    totalPrice: Double
) {
    Column {

        OrderDetailTop(
            orderNo = orderNo,
            orderDate = orderDate,
            orderStatus = orderStatus
        )

        Column {

            Text(
                "Items ($noOfItems)",
                fontSize = 14.sp,
                letterSpacing = 1.5.sp,
                color = colorResource(R.color.text_dark_300),
                modifier = Modifier.padding(
                    start = 16.dp,
                    bottom = 8.dp
                )
            )

            products.forEach { product ->

                OrderDetailProducts(
                    image = {
                        AsyncImage(
                            model = product.productImage,
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = colorResource(R.color.image_bg_color)
                                ),
                            contentScale = ContentScale.Crop
                        )
                    },
                    title = product.productTitle,
                    brand = "",
                    price = product.price,
                    quantity = product.quantity
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Sub Total",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(id = R.color.text_dark_300)
                    )

                    Text(
                        text = "Rs. %,.2f".format(subtotal),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 2.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Tax",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(id = R.color.text_dark_300)
                    )

                    Text(
                        text = "Rs. %,.2f".format(totalTax),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 2.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Shipping Charge",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(id = R.color.text_dark_300)
                    )

                    Text(
                        text = "Rs. %,.2f".format(shippingCharge),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 2.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row{
                        Text(
                            "Grand Total ",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 1.sp,
                            color = colorResource(id = R.color.text_dark_400)
                        )

                        Text(
                            "*included TAX",
                            fontSize = 10.sp,
                            lineHeight = 16.sp,
                            letterSpacing = 1.sp,
                            color = colorResource(id = R.color.text_dark_200),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Row{
                        Text(
                            "Rs.",
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            letterSpacing = 1.sp,
                            color = colorResource(id = R.color.green),
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Text(
                            text = " %,.2f".format(totalPrice),
                            fontSize = 20.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.green)
                        )
                    }
                }
            }
        }
    }
}
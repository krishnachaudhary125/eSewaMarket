package com.example.eSewaMarket.ui.compose.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.OrderItemResponse

@Composable
fun OrderCard(
    onOrderItemClick: () -> Unit,
    orderNo: String,
    date: String,
    totalNoOfProducts: Int,
    status: Boolean,
    products: List<OrderItemResponse>,
    totalPrice: Double
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

        Column{
            OrderCardTop(
                orderNo = orderNo,
                date = date,
                totalNoOfProducts = totalNoOfProducts,
                status = status
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(id = R.color.text_dark_100),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
            )

            Column {
                products.forEach { product ->

                    OrderCardProduct(
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
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(id = R.color.text_dark_100),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total Price",
                    color = colorResource(R.color.text_dark_300),
                    letterSpacing = 2.sp,
                    lineHeight = 20.sp,
                    fontSize = 14.sp
                )

                Text(
                    text = "Rs. %,.2f".format(totalPrice),
                    color = colorResource(R.color.text_dark_300),
                    letterSpacing = 1.sp,
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600)
                )
            }
        }
    }
}
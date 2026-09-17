package com.example.eSewaMarket.ui.compose.orderDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun OrderDetailTop(
    orderNo: String,
    orderDate: String,
    orderStatus: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Text(
                "Order Details",
                fontWeight = FontWeight(600),
                fontSize = 14.sp,
                lineHeight = 24.sp,
                letterSpacing = 1.sp,
                color = colorResource(R.color.text_dark_400)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column{
                    Text(
                        "Order No.",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 2.sp,
                        color = colorResource(R.color.text_dark_200),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        orderNo,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 1.5.sp,
                        color = colorResource(R.color.text_dark_400)
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1F)
                )

                Column{
                    Text(
                        "Order Date.",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 2.sp,
                        color = colorResource(R.color.text_dark_200),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        orderDate,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(R.color.text_dark_400)
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(id = R.color.text_dark_100),
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            Text(
                "Shipment Status",
                fontWeight = FontWeight(600),
                fontSize = 14.sp,
                lineHeight = 24.sp,
                letterSpacing = 1.sp,
                color = colorResource(R.color.text_dark_400)
            )

            Icon(
                painter =
                    if (orderStatus == "COMPLETED") painterResource(R.drawable.ic_shipment_status_delivered)
                else painterResource(R.drawable.ic_shipment_status_ordered),
                contentDescription = "Shipment Status",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}
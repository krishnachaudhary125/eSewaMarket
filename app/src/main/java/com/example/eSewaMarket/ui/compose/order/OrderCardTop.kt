package com.example.eSewaMarket.ui.compose.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun OrderCardTop(
    orderNo: String,
    date: String,
    totalNoOfProducts: Int,
    status: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        color = colorResource(R.color.primary_green)
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_my_products),
                    contentDescription = "Location icon",
                    tint = colorResource(R.color.green),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    "Order No. $orderNo",
                    fontWeight = FontWeight(600),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 1.sp,
                    color = colorResource(R.color.text_dark_400)
                )

                Row(
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        "Placed on ",
                        fontWeight = FontWeight(400),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(R.color.text_dark_300)
                    )

                    Text(
                        date,
                        fontWeight = FontWeight(400),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(R.color.text_dark_200)
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "$totalNoOfProducts",
                        fontWeight = FontWeight(600),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(R.color.text_dark_400),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )

                    Text(
                        " item(s) purchased ",
                        fontWeight = FontWeight(400),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 1.sp,
                        color = colorResource(R.color.text_dark_200),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )

                    Text(
                        if (status) "COMPLETE" else "PENDING",
                        fontWeight = FontWeight(400),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 1.sp,
                        color = if (status) colorResource(R.color.green) else colorResource(R.color.esewa_status_yellow),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .background(
                                color = if (status) colorResource(R.color.primary_green) else colorResource(
                                    R.color.bg_yellow
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                    )
                }
            }

            Spacer(
                modifier = Modifier.weight(1F)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Top)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = "GoTo icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(180f)
                )
            }
        }
    }
}
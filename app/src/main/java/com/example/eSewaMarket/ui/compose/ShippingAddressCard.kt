package com.example.eSewaMarket.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun ShippingAddressCard(
    fullName: String,
    label: String,
    addressName: String,
    province: String
) {
    Box(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            )
            .fillMaxWidth()
            .heightIn(min = 96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 96.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
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
                    painter = painterResource(R.drawable.ic_location),
                    contentDescription = "Location icon",
                    tint = colorResource(R.color.green),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fullName,
                        maxLines = 1,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        lineHeight = 24.sp,
                        color = colorResource(id = R.color.text_dark_400),
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = label,
                        maxLines = 1,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        lineHeight = 24.sp,
                        color = if (label.lowercase() == "home") {
                            Color.White
                        } else {
                            colorResource(R.color.green)
                        },
                        modifier = Modifier
                            .background(
                                color = if (label.lowercase() == "home") {
                                    colorResource(R.color.green)
                                } else {
                                    colorResource(R.color.primary_green)
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(
                                vertical = 4.dp,
                                horizontal = 16.dp
                            )
                    )
                }

                Text(
                    text = addressName,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    lineHeight = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.text_dark_400),
                    modifier = Modifier
                        .padding(top = 4.dp)
                )

                Text(
                    text = province,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.text_dark_200),
                    modifier = Modifier
                        .padding(top = 4.dp)
                )
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_more_vertical),
            contentDescription = "More option",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 16.dp,
                    end = 16.dp
                )
        )
    }
}
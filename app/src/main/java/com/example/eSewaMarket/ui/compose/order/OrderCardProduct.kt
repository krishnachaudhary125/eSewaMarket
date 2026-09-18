package com.example.eSewaMarket.ui.compose.order

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R

@Composable
fun OrderCardProduct(
    image: @Composable () -> Unit,
    title: String,
    brand: String,
    price: Double,
    quantity: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 88.dp)
                .padding(
                    start = 16.dp,
                    end = 48.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                image()
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {

                Text(
                    text = title,
                    maxLines = 1,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    lineHeight = 24.sp,
                    color = colorResource(id = R.color.text_dark_400),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = brand.uppercase(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 2.sp,
                    color = colorResource(id = R.color.text_dark_200),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 2.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "Rs.",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.green),
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    Text(
                        text = "%.2f".format(price*quantity),
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = colorResource(id = R.color.green)
                    )
                }
            }
        }
        Text(
            text = "x$quantity",
            fontSize = 20.sp,
            letterSpacing = 1.sp,
            color = colorResource(id = R.color.text_dark_400),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp)
        )
    }
}
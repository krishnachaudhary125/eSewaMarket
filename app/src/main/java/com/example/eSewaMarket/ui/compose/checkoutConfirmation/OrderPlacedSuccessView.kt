package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.OrderResponse

@Composable
fun OrderPlacedSuccessView(
    modifier: Modifier = Modifier,
    order: OrderResponse,
    onViewOrderClick: () -> Unit,
    onGoToHomeClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Order Placed",
                tint = colorResource(id = R.color.green),
                modifier = Modifier.size(96.dp)
            )

            Text(
                text = "Order Placed!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.text_dark_400),
                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = "Order No: ${order.orderNumber}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.text_dark_400),
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "Rs. ${"%.2f".format(order.totalAmount)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.text_dark_400),
                modifier = Modifier.padding(top = 4.dp)
            )

            Button(
                onClick = onViewOrderClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {
                Text(
                    "VIEW ORDER",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = onGoToHomeClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.text_dark_300),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    "GO TO HOME",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
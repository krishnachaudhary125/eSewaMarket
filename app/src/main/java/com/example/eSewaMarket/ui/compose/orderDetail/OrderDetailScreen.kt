package com.example.eSewaMarket.ui.compose.orderDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import com.example.eSewaMarket.ui.compose.AppToolBar
import com.example.eSewaMarket.ui.compose.component.CommonError
import com.example.eSewaMarket.ui.compose.component.CommonLoading
import com.example.eSewaMarket.ui.compose.order.formatOrderDate

@Composable
fun OrderDetailScreen(
    uiState: OrderDetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        containerColor = colorResource(R.color.background),

        topBar = {
            AppToolBar(
                modifier = Modifier
                    .statusBarsPadding(),
                title = {
                    Text(
                        "Thank you for your order",
                        fontSize = 16.sp,
                        color = colorResource(R.color.text_dark_400)
                    )
                },
                onBackClick = onBackClick
            )
        },

        bottomBar = {
            if (uiState is OrderDetailUiState.Success) {

                val order = uiState.orderDetail
                val address = listOf(
                    order.shippingAddressName,
                    order.shippingDistrict,
                    order.shippingPostalCode
                ).joinToString(", ")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(104.dp)
                        .padding(16.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    color = colorResource(R.color.primary_green),
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_location),
                                contentDescription = "Location Icon",
                                tint = colorResource(R.color.green),
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }

                        Column(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text(
                                "Delivery Address",
                                fontSize = 12.sp,
                                fontWeight = FontWeight(600),
                                lineHeight = 16.sp,
                                letterSpacing = 2.sp,
                                color = colorResource(R.color.text_dark_200)
                            )

                            Spacer(
                                modifier = Modifier.weight(1F)
                            )

                            Text(
                                address,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(600),
                                lineHeight = 24.sp,
                                letterSpacing = 1.sp,
                                color = colorResource(R.color.text_dark_400),
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

    ) { innerPadding ->

        when (uiState) {

            OrderDetailUiState.Loading -> {
                CommonLoading()
            }

            is OrderDetailUiState.Success -> {

                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {

                    item {

                        OrderDetailCard(
                            orderNo = uiState.orderDetail.orderNumber,
                            orderDate = formatOrderDate(
                                uiState.orderDetail.createdAt
                            ),
                            orderStatus = uiState.orderDetail.orderStatus,
                            noOfItems = uiState.orderDetail.products.size,
                            products = uiState.orderDetail.products,
                            subtotal = uiState.orderDetail.subtotal,
                            totalTax = uiState.orderDetail.tax,
                            shippingCharge = uiState.orderDetail.shippingCharge,
                            totalPrice = uiState.orderDetail.totalAmount
                        )
                    }
                }
            }

            is OrderDetailUiState.Error -> {
                CommonError(
                    message = uiState.message,
                    onRetry = onRetry
                )
            }
        }
    }
}
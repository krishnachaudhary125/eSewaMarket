package com.example.eSewaMarket.ui.compose.orderDetail

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
                        color = colorResource(id = R.color.text_dark_400)
                    )
                },
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->

        when (uiState) {

            OrderDetailUiState.Loading -> {
                CommonLoading()
            }

            is OrderDetailUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ){
                    item {
                        OrderDetailCard(
                            orderNo = uiState.orderDetail.orderNumber,
                            orderDate = formatOrderDate(uiState.orderDetail.createdAt),
                            orderStatus = uiState.orderDetail.orderStatus
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
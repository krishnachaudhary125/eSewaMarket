package com.example.eSewaMarket.ui.compose.order

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.eSewaMarket.R
import com.example.eSewaMarket.ui.compose.AppToolBar
import com.example.eSewaMarket.ui.compose.component.CommonError
import com.example.eSewaMarket.ui.compose.component.CommonLoading
import com.example.eSewaMarket.utils.minimumLoadingTime

@Composable
fun OrderScreen(
    uiState: OrderUiState,
    onBackClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    onRetry: () -> Unit,
    continueShopping: () -> Unit
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    var isTabSwitching by remember { mutableStateOf(false) }

    LaunchedEffect(selectedTabIndex) {
        isTabSwitching = true
        minimumLoadingTime{}
        isTabSwitching = false
    }

    val tabs = listOf(
        "All",
        "PENDING",
        "COMPLETE"
    )

    Scaffold(
        containerColor = colorResource(R.color.background),

        topBar = {
            AppToolBar(
                modifier = Modifier
                    .statusBarsPadding(),
                onBackClick = onBackClick,
                title = {
                    Text(
                        "My Order",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_dark_400)
                    )
                },
                bottomContent = {
                    TabCard(
                        tabs = tabs,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { index ->
                            selectedTabIndex = index
                        }
                    )
                }
            )
        }

    ) { innerPadding ->

        when (uiState) {

            OrderUiState.Loading -> {
                CommonLoading()
            }

            is OrderUiState.Success -> {

                if (isTabSwitching) {
                    CommonLoading()
                } else {
                    val orders = when (selectedTabIndex) {
                        0 -> uiState.orders
                        1 -> uiState.orders.filter { it.orderStatus == "PENDING" }
                        2 -> uiState.orders.filter { it.orderStatus == "COMPLETED" }
                        else -> uiState.orders
                    }

                    if (orders.isEmpty()) {
                        OrderEmpty(
                            modifier = Modifier
                                .padding(innerPadding),
                            continueShopping = continueShopping
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            items(
                                items = orders,
                                key = { order -> order.id }
                            ) { order ->
                                OrderCard(
                                    onOrderItemClick = {
                                        onItemClick(order.id)
                                    },
                                    orderNo = order.orderNumber,
                                    date = formatOrderDate(order.createdAt),
                                    totalNoOfProducts = order.products.size,
                                    status = order.orderStatus == "COMPLETED",
                                    products = order.products,
                                    totalPrice = order.totalAmount
                                )
                            }
                        }
                    }
                }
            }

            is OrderUiState.Error -> {
                CommonError(
                    message = uiState.message,
                    onRetry = onRetry
                )
            }
        }
    }
}
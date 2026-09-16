package com.example.eSewaMarket.ui.compose.order

import com.example.eSewaMarket.data.models.OrderDetailResponse

sealed interface OrderUiState {

    data object Loading: OrderUiState

    data class Success(
        val orders: List<OrderDetailResponse>
    ): OrderUiState

    data class Error(
        val message: String
    ): OrderUiState
}
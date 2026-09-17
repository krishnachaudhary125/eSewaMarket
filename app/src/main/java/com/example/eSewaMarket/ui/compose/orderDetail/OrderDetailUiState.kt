package com.example.eSewaMarket.ui.compose.orderDetail

import com.example.eSewaMarket.data.models.OrderDetailResponse

sealed interface OrderDetailUiState {

    data object Loading : OrderDetailUiState

    data class Success(
        val orderDetail: OrderDetailResponse
    ): OrderDetailUiState

    data class Error(
        val message: String
    ): OrderDetailUiState
}
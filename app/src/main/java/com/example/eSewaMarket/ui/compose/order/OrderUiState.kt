package com.example.eSewaMarket.ui.compose.order

sealed interface OrderUiState {

    data object Loading: OrderUiState

    data object Success: OrderUiState

//    data class Success(
//        val data: OrderData
//    ): OrderUiState

    data class Error(
        val message: String
    ): OrderUiState
}
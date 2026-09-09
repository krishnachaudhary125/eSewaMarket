package com.example.eSewaMarket.ui.compose.checkout

sealed interface CheckoutUiState {

    data object Loading : CheckoutUiState

    data class Success(
        val checkoutData: CheckoutData
    ) : CheckoutUiState

    data class Error(
        val message: String
    ) : CheckoutUiState
}
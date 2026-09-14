package com.example.eSewaMarket.ui.compose.checkoutConfirmation

sealed interface ConfirmationUiState {

    data object Loading : ConfirmationUiState

    data class Success(
        val confirmationData: ConfirmationData
    ) : ConfirmationUiState

    data class Error(
        val message: String
    ) : ConfirmationUiState
}
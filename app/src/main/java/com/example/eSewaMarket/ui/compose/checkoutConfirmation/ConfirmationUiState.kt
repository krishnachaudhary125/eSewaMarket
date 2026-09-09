package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import com.example.eSewaMarket.data.models.ProductResponse

sealed interface ConfirmationUiState {

    data object Loading : ConfirmationUiState

    data class Success(
        val confirmationData: ConfirmationData
    ) : ConfirmationUiState

    data class Error(

        val message: String
    ) : ConfirmationUiState
}
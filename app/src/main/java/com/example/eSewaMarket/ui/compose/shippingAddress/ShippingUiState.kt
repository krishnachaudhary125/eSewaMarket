package com.example.eSewaMarket.ui.compose.shippingAddress

import com.example.eSewaMarket.data.models.AddressResponse

sealed interface ShippingUiState {

    data object Loading: ShippingUiState

    data class Success(
        val shippingData: List<AddressResponse>
    ): ShippingUiState

    data class Error(
        val message: String
    ): ShippingUiState
}
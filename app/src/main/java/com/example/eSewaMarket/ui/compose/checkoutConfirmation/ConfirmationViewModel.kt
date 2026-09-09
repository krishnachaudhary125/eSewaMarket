package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.utils.minimumLoadingTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfirmationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ConfirmationUiState>(
        ConfirmationUiState.Loading
    )

    val uiState: StateFlow<ConfirmationUiState> = _uiState.asStateFlow()

    private var products: List<ProductResponse> = emptyList()
    private var confirmationData: ConfirmationData? = null

    fun loadConfirmation(
        products: List<ProductResponse>,
        confirmationData: ConfirmationData
    ) {
        this.products = products
        this.confirmationData = confirmationData

        viewModelScope.launch {

            _uiState.value = ConfirmationUiState.Loading

            minimumLoadingTime {
                Unit
            }

            _uiState.value = ConfirmationUiState.Success(
                confirmationData = confirmationData
            )
        }
    }

    fun retry() {
        val data = confirmationData ?: return

        loadConfirmation(
            products = products,
            confirmationData = data
        )
    }

    fun showError(message: String) {
        _uiState.value = ConfirmationUiState.Error(message)
    }
}
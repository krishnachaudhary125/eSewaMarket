package com.example.eSewaMarket.ui.compose.order

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<OrderUiState>(
        OrderUiState.Success
    )
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()


}
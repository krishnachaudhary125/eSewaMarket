package com.example.eSewaMarket.ui.compose.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.repository.OrderRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderUiState>(
        OrderUiState.Loading
    )

    val uiState: StateFlow<OrderUiState> = _uiState

    fun getAllOrders() {

        viewModelScope.launch {
            _uiState.value = OrderUiState.Loading

            try {
                val orders = orderRepository.getAllOrders()

                _uiState.value = OrderUiState.Success(
                    orders = orders
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = OrderUiState.Error(
                    message = e.message ?: "Failed to load orders"
                )
            }
        }
    }

    fun retry(){
        getAllOrders()
    }
}

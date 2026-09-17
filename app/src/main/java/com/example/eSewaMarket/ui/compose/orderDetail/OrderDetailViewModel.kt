package com.example.eSewaMarket.ui.compose.orderDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.repository.OrderRepository
import com.example.eSewaMarket.utils.minimumLoadingTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val repository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderDetailUiState>(
        OrderDetailUiState.Loading
    )

    val uiState: StateFlow<OrderDetailUiState> =
        _uiState.asStateFlow()

    private var orderId: Long? = null

    fun loadOrderDetail(id: Long) {

        orderId = id

        _uiState.value = OrderDetailUiState.Loading

        viewModelScope.launch {
            try {

                val orderDetail = minimumLoadingTime {
                    repository.getOrderDetail(id)
                }

                _uiState.value = OrderDetailUiState.Success(
                    orderDetail = orderDetail
                )

            } catch (e: Exception) {

                _uiState.value = OrderDetailUiState.Error(
                    message = e.message ?: "Failed to load order details"
                )
            }
        }
    }

    fun retry() {

        orderId?.let {
            loadOrderDetail(it)
        }
    }
}
package com.example.eSewaMarket.ui.compose.checkoutConfirmation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.OrderResponse
import com.example.eSewaMarket.data.models.PaymentOptions
import com.example.eSewaMarket.data.repository.CartRepository
import com.example.eSewaMarket.data.repository.OrderRepository
import com.example.eSewaMarket.utils.minimumLoadingTime
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfirmationViewModel(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConfirmationUiState>(ConfirmationUiState.Loading)
    val uiState: StateFlow<ConfirmationUiState> = _uiState.asStateFlow()

    private val _placedOrder = MutableStateFlow<OrderResponse?>(null)
    val placedOrder: StateFlow<OrderResponse?> = _placedOrder.asStateFlow()

    private val _isPlacingOrder = MutableStateFlow(false)
    val isPlacingOrder: StateFlow<Boolean> = _isPlacingOrder.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ConfirmationNavigationEvent>()
    val navigationEvent: SharedFlow<ConfirmationNavigationEvent> = _navigationEvent.asSharedFlow()

    private var confirmationData: ConfirmationData? = null
    private var pendingEsewaOrder: OrderResponse? = null

    fun loadConfirmation(confirmationData: ConfirmationData) {
        this.confirmationData = confirmationData
        viewModelScope.launch {
            _uiState.value = ConfirmationUiState.Loading
            minimumLoadingTime { Unit }
            _uiState.value = ConfirmationUiState.Success(confirmationData = confirmationData)
        }
    }

    fun createOrder() {
        val data = confirmationData ?: return

        viewModelScope.launch {
            _isPlacingOrder.value = true

            val result = minimumLoadingTime {
                orderRepository.createOrder(
                    shippingAddressId = data.addressId,
                    paymentOption = data.paymentOption.name
                )
            }

            _isPlacingOrder.value = false

            result.onSuccess { order ->
                when (data.paymentOption) {
                    PaymentOptions.CASH_ON_DELIVERY -> {
                        _placedOrder.value = order
                        syncCartQuietly()
                    }

                    PaymentOptions.ESEWA -> {
                        pendingEsewaOrder = order
                        _navigationEvent.emit(
                            ConfirmationNavigationEvent.StartEsewaPayment(order)
                        )
                        // cart NOT cleared here — only after payment is confirmed, below
                    }
                }
            }.onFailure { exception ->
                showError(exception.message ?: "Failed to create order.")
            }
        }
    }

    fun onEsewaPaymentResult(success: Boolean) {
        val order = pendingEsewaOrder ?: return
        pendingEsewaOrder = null

        if (success) {
            _placedOrder.value = order
            viewModelScope.launch { syncCartQuietly() }
        } else {
            showError("Payment was not completed. Please try again.")
        }
    }

    private suspend fun syncCartQuietly() {
        try {
            cartRepository.syncCartWithServer()
        } catch (e: Exception) {
            Log.e("cart_sync", "Failed to sync cart.", e)
        }
    }

    fun retry() {
        val data = confirmationData ?: return
        loadConfirmation(confirmationData = data)
    }

    fun showError(message: String) {
        _uiState.value = ConfirmationUiState.Error(message)
    }
}

sealed class ConfirmationNavigationEvent {
    data class StartEsewaPayment(val order: OrderResponse) : ConfirmationNavigationEvent()
}
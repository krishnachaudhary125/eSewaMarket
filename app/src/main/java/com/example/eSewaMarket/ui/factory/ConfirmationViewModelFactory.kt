package com.example.eSewaMarket.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eSewaMarket.data.repository.CartRepository
import com.example.eSewaMarket.data.repository.OrderRepository
import com.example.eSewaMarket.ui.compose.checkoutConfirmation.ConfirmationViewModel

class ConfirmationViewModelFactory(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ConfirmationViewModel(orderRepository, cartRepository) as T
    }
}
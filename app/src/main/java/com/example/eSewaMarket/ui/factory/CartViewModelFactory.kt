package com.example.eSewaMarket.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eSewaMarket.data.repository.CartRepository
import com.example.eSewaMarket.ui.viewmodel.CartViewModel

class CartViewModelFactory(private val repository: CartRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CartViewModel(repository) as T
    }
}
package com.example.eSewaMarket.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eSewaMarket.data.repository.AddressRepository
import com.example.eSewaMarket.ui.viewmodel.AddressViewModel

class AddressViewModelFactory(private val repository: AddressRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AddressViewModel(repository) as T
    }
}
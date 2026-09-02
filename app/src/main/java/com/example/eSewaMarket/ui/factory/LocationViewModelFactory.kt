package com.example.eSewaMarket.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eSewaMarket.data.repository.LocationRepository
import com.example.eSewaMarket.ui.viewmodel.LocationViewModel

class LocationViewModelFactory(private val repository: LocationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LocationViewModel(repository) as T
    }
}
package com.example.eSewaMarket.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eSewaMarket.data.repository.FavouriteRepository
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel

class FavouriteViewModelFactory(private val repository: FavouriteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FavouriteViewModel(repository) as T
    }
}
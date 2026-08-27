package com.example.eSewaMarket.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eSewaMarket.ui.viewmodel.SectionProductViewModel

class SectionProductViewModelFactory(
    private val type: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SectionProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SectionProductViewModel(type) as T
        }

        throw IllegalArgumentException("Unknown viewmodel class.")
    }
}
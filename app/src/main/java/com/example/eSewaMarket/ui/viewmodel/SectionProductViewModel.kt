package com.example.eSewaMarket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SectionProductViewModel(
    private val type: String
) : ViewModel() {

    private val productRepository = ProductRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow(false)
    val error = _error.asStateFlow()

    private var currentPage = 0
    private val pageSize = 10

    private var isLastPage = false

    init {
        loadNextPage()
    }

    fun loadNextPage() {

        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {

            try {
                _isLoading.value = true
                _error.value = false

                delay(1000)

                val response =
                    productRepository.fetchProductsBySection(
                        type = type,
                        page = currentPage,
                        size = pageSize
                    )

                _products.update { currentProducts ->
                    currentProducts + response.content
                }

                isLastPage = response.last

                if (!response.last) {
                    currentPage++
                }

            } catch (e: Exception) {

                Log.e("FEATURED", "Error loading products", e)

                _error.value = true

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun retry(){
        if(_isLoading.value)
            return

        _error.value = false
        loadNextPage()
    }
}
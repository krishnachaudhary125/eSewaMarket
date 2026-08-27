package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel: ViewModel() {
    private val productRepository = ProductRepository()

    val selectedProduct: LiveData<Product> = productRepository.selectedProduct
    val similarProducts = productRepository.products


    fun loadProduct(id: Long) {
        viewModelScope.launch {
            productRepository.fetchProductById(id)
        }
    }

    fun loadSimilarProducts(){
        viewModelScope.launch {
            productRepository.fetchProducts()
        }
    }
}
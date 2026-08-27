package com.example.eSewaMarket.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.models.HomeResponse
import com.example.eSewaMarket.data.models.PageResponse
import com.example.eSewaMarket.data.models.Product

class ProductRepository {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _home = MutableLiveData<HomeResponse>()
    val home: LiveData<HomeResponse> = _home

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> = _selectedProduct

    suspend fun fetchProducts() {
        try {
            val response = RetrofitInstance.api.getProduct()
            _products.postValue(response)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", e.toString(), e)
            throw e
        }
    }

    suspend fun fetchProductById(id: Long) {
        try {
            val response = RetrofitInstance.api.getProductById(id)
            _selectedProduct.postValue(response)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", e.toString(), e)
            throw e
        }
    }

    suspend fun fetchRecommendedProducts(page: Int): PageResponse<Product> {
        return RetrofitInstance.api.getRecommendedProducts(page)
    }

    suspend fun fetchHome() {
        try {
            val response = RetrofitInstance.api.getHome()
            _home.postValue(response)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", e.toString(), e)
            throw e
        }
    }

    suspend fun fetchProductsBySection(
        type: String,
        page: Int,
        size: Int = 10
    ): PageResponse<Product> {
        return RetrofitInstance.api.getProductsBySection(
            type = type,
            page = page,
            size = size
        )
    }
}
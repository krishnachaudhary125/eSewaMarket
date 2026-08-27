package com.example.eSewaMarket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.orEmpty
import kotlin.collections.plus

class RecommendedProductViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    private val _recommendedProducts = MutableLiveData<List<Product>>(emptyList())
    val recommendedProducts: LiveData<List<Product>> = _recommendedProducts
    private val _recommendedLoading = MutableLiveData(false)
    val recommendedLoading: LiveData<Boolean> = _recommendedLoading
    private var recommendedPage = 0
    private var isLoadingRecommended = false
    private var isLastRecommendedPage = false

    init {
        loadAllData()
    }

    private fun loadAllData() {
        loadMoreRecommended()
    }

    fun loadMoreRecommended() {
        if (isLoadingRecommended || isLastRecommendedPage) return

        isLoadingRecommended = true
        _recommendedLoading.value = true

        viewModelScope.launch {
            try {
                val response = productRepository.fetchRecommendedProducts(
                    page = recommendedPage
                )

                delay(1000)

                val current = _recommendedProducts.value.orEmpty()
                _recommendedProducts.value = current + response.content

                isLastRecommendedPage = response.last
                recommendedPage++

            } catch (e: Exception) {
                Log.e("API_ERROR", "loadMoreRecommended failed", e)
            } finally {
                isLoadingRecommended = false
                _recommendedLoading.value = false
            }
        }
    }
}
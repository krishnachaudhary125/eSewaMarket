package com.example.eSewaMarket.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.models.HotDeal

class HotDealCategoryRepository {

    private val _hotDealCategories = MutableLiveData<List<HotDeal>>()
    val hotDealCategories: LiveData<List<HotDeal>> = _hotDealCategories

    suspend fun fetchHotDealCategories() {
        try {
            val categories = RetrofitInstance.api.getHotDealCategories()
            _hotDealCategories.postValue(categories.take(7))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
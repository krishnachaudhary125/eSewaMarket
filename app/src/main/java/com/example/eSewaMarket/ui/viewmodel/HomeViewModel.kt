package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.Banner
import com.example.eSewaMarket.data.models.Category
import com.example.eSewaMarket.data.models.HotDeal
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.BannerRepository
import com.example.eSewaMarket.data.repository.CategoryRepository
import com.example.eSewaMarket.data.repository.HotDealCategoryRepository
import com.example.eSewaMarket.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val bannerRepository = BannerRepository()
    private val categoryRepository = CategoryRepository()
    private val hotDealCategoryRepository = HotDealCategoryRepository()
    private val productRepository = ProductRepository()

    val banners: LiveData<List<Banner>> = bannerRepository.banners
    val category: LiveData<List<Category>> = categoryRepository.category
    val hotDealCategories: LiveData<List<HotDeal>> = hotDealCategoryRepository.hotDealCategories
    val products: LiveData<List<Product>> = productRepository.products
    val home = productRepository.home
    private val _homeError = MutableStateFlow(false)
    val homeError: StateFlow<Boolean> = _homeError.asStateFlow()


    init {
        loadAllData()
    }

    private fun loadAllData() {
        _homeError.value = false
        loadBanners()
        loadCategory()
        loadHotDealCategories()
        loadHome()
    }

    fun loadBanners() {
        viewModelScope.launch {
            try {
                bannerRepository.fetchBanners()
                _homeError.value = false
            }catch (e: Exception){
                _homeError.value = true
            }
        }
    }

    fun loadCategory() {
        viewModelScope.launch {
            try {
                categoryRepository.fetchCategory()
                _homeError.value = false
            }catch (e: Exception){
                _homeError.value = true
            }
        }
    }

    fun loadHotDealCategories() {
        viewModelScope.launch {
            try {
                hotDealCategoryRepository.fetchHotDealCategories()
                _homeError.value = false
            }catch (e: Exception){
                _homeError.value = true
            }
        }
    }

    fun loadHome(){
        viewModelScope.launch {
            try {
                productRepository.fetchHome()
                _homeError.value = false
            }catch (e: Exception){
                _homeError.value = true
            }
        }
    }

    fun retry(){
        loadAllData()
    }
}
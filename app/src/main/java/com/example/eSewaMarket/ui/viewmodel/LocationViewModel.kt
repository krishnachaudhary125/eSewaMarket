package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.DistrictResponse
import com.example.eSewaMarket.data.models.ProvinceResponse
import com.example.eSewaMarket.data.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _provinces = MutableStateFlow<List<ProvinceResponse>>(emptyList())
    val provinces: StateFlow<List<ProvinceResponse>> = _provinces.asStateFlow()

    private val _districts = MutableStateFlow<List<DistrictResponse>>(emptyList())
    val districts: StateFlow<List<DistrictResponse>> = _districts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadProvinces() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getProvinces()
                .onSuccess {
                    _provinces.value = it
                }
                .onFailure {
                    _error.value = it.message ?: "Failed to load provinces"
                }

            _isLoading.value = false
        }
    }

    fun loadDistricts(provinceId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            _districts.value = emptyList()

            repository.getDistricts(provinceId)
                .onSuccess {
                    _districts.value = it
                }
                .onFailure {
                    _error.value = it.message ?: "Failed to load districts"
                }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
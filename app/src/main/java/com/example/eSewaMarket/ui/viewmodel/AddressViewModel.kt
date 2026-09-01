package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.AddressRequest
import com.example.eSewaMarket.data.models.AddressResponse
import com.example.eSewaMarket.data.repository.AddressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressViewModel(
    private val repository: AddressRepository
) : ViewModel() {

    private val _addresses = MutableStateFlow<List<AddressResponse>>(emptyList())
    val addresses: StateFlow<List<AddressResponse>> = _addresses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun getAddresses() {
        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            repository.getAddresses()
                .onSuccess { response ->
                    _addresses.value = response
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun createAddress(
        request: AddressRequest,
        onSuccess: (AddressResponse) -> Unit = {}
    ) {
        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            repository.createAddress(request)
                .onSuccess { response ->

                    _addresses.value += response

                    onSuccess(response)
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun getAddress(
        id: Long,
        onSuccess: (AddressResponse) -> Unit = {}
    ) {
        viewModelScope.launch {

            repository.getAddress(id)
                .onSuccess { response ->
                    onSuccess(response)
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
        }
    }


    fun clearError() {
        _error.value = null
    }
}
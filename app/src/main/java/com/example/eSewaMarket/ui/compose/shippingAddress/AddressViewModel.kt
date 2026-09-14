package com.example.eSewaMarket.ui.compose.shippingAddress

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.AddressRequest
import com.example.eSewaMarket.data.models.AddressResponse
import com.example.eSewaMarket.data.repository.AddressRepository
import com.example.eSewaMarket.utils.minimumLoadingTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AddressViewModel(
    private val repository: AddressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShippingUiState>(
        ShippingUiState.Loading
    )
    val uiState: StateFlow<ShippingUiState> = _uiState.asStateFlow()

    private var addresses: List<AddressResponse> = emptyList()

    fun getAddresses() {
        viewModelScope.launch {

            _uiState.value = ShippingUiState.Loading

            try {
                val response = minimumLoadingTime {
                    repository.getAddresses()
                        .getOrThrow()
                }

                _uiState.value = ShippingUiState.Success(
                    shippingData = response
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = ShippingUiState.Error(
                    message = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun createAddress(
        request: AddressRequest,
        onSuccess: (AddressResponse) -> Unit = {}
    ) {
        viewModelScope.launch {

            try {
                val response = repository.createAddress(request)
                    .getOrThrow()

                addresses = addresses + response

                _uiState.value = ShippingUiState.Success(
                    shippingData = addresses
                )

                onSuccess(response)

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.value = ShippingUiState.Error(
                    message = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun getAddress(
        id: Long,
        onSuccess: (AddressResponse) -> Unit = {}
    ) {
        viewModelScope.launch {

            try {
                val response = repository.getAddress(id)
                    .getOrThrow()

                onSuccess(response)

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                Log.e("GET_ADDRESS", "Failed to get address", e)
            }
        }
    }

    fun updateAddress(
        id: Long,
        request: AddressRequest,
        onSuccess: (AddressResponse) -> Unit = {}
    ) {
        viewModelScope.launch {

            try {

                _uiState.value = ShippingUiState.Loading

                val response = minimumLoadingTime {
                    repository
                        .updateAddress(
                            id = id,
                            request = request
                        )
                        .getOrThrow()
                }

                addresses = addresses.map { address ->

                    if (address.id == id) {
                        response
                    } else {
                        address
                    }
                }

                _uiState.value = ShippingUiState.Success(
                    shippingData = addresses
                )

                onSuccess(response)

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _uiState.value = ShippingUiState.Error(
                    message = e.message
                        ?: "Failed to update address"
                )
            }
        }
    }

    fun deleteAddress(
        id: Long,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = ShippingUiState.Loading

                minimumLoadingTime {
                    repository
                        .deleteAddress(id = id)
                        .getOrThrow()
                }

                addresses = addresses.filter {
                    it.id != id
                }

                _uiState.value = ShippingUiState.Success(
                    shippingData = addresses
                )

                onSuccess()

            } catch (e: CancellationException) {
                throw e

            }catch (e: Exception) {

                _uiState.value = ShippingUiState.Error(
                    message = e.message
                        ?: "Failed to delete address"
                )
            }
        }
    }

    fun syncAddressWithServer() {
        viewModelScope.launch {
            try {
                repository.syncAddressWithServer()

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                Log.e("SYNC_ADDRESS", "Address sync failed", e)
            }
        }
    }

    fun retry() {
        getAddresses()
    }
}
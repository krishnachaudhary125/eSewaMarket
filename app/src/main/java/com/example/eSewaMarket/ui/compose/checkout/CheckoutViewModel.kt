package com.example.eSewaMarket.ui.compose.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.utils.minimumLoadingTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CheckoutUiState>(
        CheckoutUiState.Loading
    )

    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private var products: List<ProductResponse> = emptyList()
    private var productPrice: Double = 0.0
    private var itemCount: Int = 0
    private var address: String = ""
    private var addressExist: Boolean = false

    fun loadCheckout(
        products: List<ProductResponse>,
        productPrice: Double,
        itemCount: Int,
        address: String,
        addressExist: Boolean
    ) {
        this.products = products
        this.productPrice = productPrice
        this.itemCount = itemCount
        this.address = address
        this.addressExist = addressExist

        viewModelScope.launch {

            _uiState.value = CheckoutUiState.Loading

            try {

                val checkoutData = minimumLoadingTime {
                    val totalTax = (productPrice * 13) / 100
                    val shippingCharge = 70.00
                    val totalPrice = productPrice + totalTax + shippingCharge

                    CheckoutData(
                        checkoutProducts = products,
                        totalAmount = totalPrice,
                        itemCount = itemCount,
                        productPrice = productPrice,
                        totalTax = totalTax,
                        shippingCharge = shippingCharge,
                        shippingAddress = address,
                        addressExist = addressExist
                    )
                }

                _uiState.value = CheckoutUiState.Success(
                    checkoutData = checkoutData
                )
            } catch (e: Exception) {
                _uiState.value = CheckoutUiState.Error(
                    message = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun retry() {
        loadCheckout(
            products = products,
            productPrice = productPrice,
            itemCount = itemCount,
            address = address,
            addressExist = addressExist
        )
    }
}
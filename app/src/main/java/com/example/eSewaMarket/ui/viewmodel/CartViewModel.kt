package com.example.eSewaMarket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.data.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    fun cartCount() = repository.totalQuantity()

    fun increaseQuantity(productId: Long) {
        viewModelScope.launch {
            repository.increaseQuantity(productId)
        }
    }

    fun addToCart(product: Product){
        viewModelScope.launch {
            repository.addToCart(product)
        }
    }

    fun addToCartFromFavourite(product: FavouriteResponse){
        viewModelScope.launch {
            repository.addToCartFromFavourite(product)
        }
    }

    fun removeOneFromCart(productId: Long) {
        viewModelScope.launch {
            repository.removeOneFromCart(productId)
        }
    }

    fun productQuantity(productId: Long) =
        repository.productQuantity(productId)

    fun cartProducts() : Flow<List<ProductResponse>>{
        return repository.cartProducts()
    }

    val totalPrice: StateFlow<Double?> = flow {
        emitAll(repository.totalPrice())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    val totalItem: StateFlow<Int> = flow{
        emitAll(repository.itemCount())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        0
    )

    fun syncCartWithServer() {
        viewModelScope.launch {
            try {
                repository.syncCartWithServer()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("SYNC_Cart", "Cart sync failed", e)
            }
        }
    }
}
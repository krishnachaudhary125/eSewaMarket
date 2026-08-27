package com.example.eSewaMarket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class FavouriteViewModel(
    private val repository: FavouriteRepository
) : ViewModel() {

    fun favouriteCount() = repository.totalQuantity()

    fun toggleFavourite(product: Product) {
        viewModelScope.launch {
            try {
                repository.toggleFavourite(product)
            } catch (e: SocketTimeoutException) {
                Log.e("Favourite", "Favourite request timed out", e)
            } catch (e: IOException) {
                Log.e("Favourite", "Network error", e)
            } catch (e: HttpException) {
                Log.e("Favourite", "HTTP ${e.code()}", e)
            }
        }
    }

    fun isFavourite(productId: Long): Flow<Boolean> {
        return repository.isFavourite(productId)
    }

    fun favouriteProducts() : Flow<List<FavouriteResponse>>{
        return repository.favouriteProducts()
    }

    suspend fun deleteAllFavourites() {
        repository.deleteFavourites()
    }

    fun removeOne(productId: Long) {
        viewModelScope.launch {
            try {
                repository.removeOneFromFavourite(productId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                Log.e("Favourite", "Remove favourite timed out", e)
            } catch (e: IOException) {
                Log.e("Favourite", "Network error", e)
            } catch (e: HttpException) {
                Log.e("Favourite", "HTTP ${e.code()}", e)
            }
        }
    }

    fun syncFavouritesWithServer() {
        viewModelScope.launch {
            try {
                repository.syncFavouritesWithServer()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("SYNC_Favourite", "Favourite sync failed", e)
            }
        }
    }

    fun restoreFavourite(favourite: FavouriteResponse) {
        viewModelScope.launch {
            repository.addFavourite(favourite)
        }
    }
}
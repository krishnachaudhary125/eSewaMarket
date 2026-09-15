package com.example.eSewaMarket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.FavouriteRepository
import com.example.eSewaMarket.ui.compose.favourite.FavouriteData
import com.example.eSewaMarket.ui.compose.favourite.FavouriteUiState
import com.example.eSewaMarket.utils.minimumLoadingTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class FavouriteViewModel(
    private val repository: FavouriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavouriteUiState>(
        FavouriteUiState.Loading
    )
    val uiState: StateFlow<FavouriteUiState> = _uiState.asStateFlow()
    private var favourites: List<FavouriteResponse> = emptyList()
    fun favouriteCount() = repository.totalQuantity()
    private var favouritesJob: Job? = null

    fun getFavourites() {

        if (favouritesJob?.isActive == true) {
            return
        }

        favouritesJob = viewModelScope.launch {
            _uiState.value = FavouriteUiState.Loading

            try {

                val response = minimumLoadingTime {
                    repository.favouriteProducts().first()
                }

                favourites = response

                _uiState.value =
                    FavouriteUiState.Success(
                        favouriteData = FavouriteData(
                            products = response
                        )
                    )

                repository.favouriteProducts()
                    .collect { products ->

                        favourites = products

                        _uiState.value =
                            FavouriteUiState.Success(
                                favouriteData = FavouriteData(
                                    products = products
                                )
                            )
                    }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _uiState.value =
                    FavouriteUiState.Error(
                        message = e.message
                            ?: "Something went wrong"
                    )
            }
        }
    }


    fun toggleFavourite(product: Product) {
        viewModelScope.launch {
            try {
                repository.toggleFavourite(product)

            } catch (e: CancellationException) {
                throw e

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

    fun favouriteProducts(): Flow<List<FavouriteResponse>> {
        return repository.favouriteProducts()
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

    fun retry() {
        getFavourites()
    }
}
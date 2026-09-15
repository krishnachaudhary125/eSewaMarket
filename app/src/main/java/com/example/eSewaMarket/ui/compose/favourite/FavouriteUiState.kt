package com.example.eSewaMarket.ui.compose.favourite

sealed interface FavouriteUiState {

    data object Loading: FavouriteUiState

    data class Success(
        val favouriteData: FavouriteData
    ): FavouriteUiState

    data class Error(
        val message: String
    ): FavouriteUiState
}
package com.example.eSewaMarket.data.repository

import android.util.Log
import coil3.network.HttpException
import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.local.dao.FavouriteDao
import com.example.eSewaMarket.data.local.dao.ProductDao
import com.example.eSewaMarket.data.local.entity.FavouriteEntity
import com.example.eSewaMarket.data.local.entity.ProductEntity
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.data.models.FavouriteToggles
import com.example.eSewaMarket.data.models.Product
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class FavouriteRepository(
    private val favouriteDao: FavouriteDao,
    private val productDao: ProductDao,
    private val userRepository: UserSessionRepository,
    private val apiService: ApiService
) {
    private suspend fun currentUserId(): Long {
        return userRepository.user.first().id
    }

    private suspend fun getAuthToken(): String {
        return FirebaseAuth.getInstance()
            .currentUser
            ?.getIdToken(false)
            ?.await()
            ?.token
            ?.let { "Bearer $it" }
            ?: throw IllegalStateException("User is not authenticated")
    }

    suspend fun toggleFavourite(product: Product) {
        val userId = currentUserId()
        val token = getAuthToken()

        val item = favouriteDao.getFavouriteItem(userId, product.id)

        if (item == null) {
            productDao.insertIntoProducts(
                listOf(
                    ProductEntity(
                        productId = product.id,
                        title = product.title,
                        thumbnail = product.thumbnail,
                        price = product.price,
                        brand = product.brand
                    )
                )
            )

            favouriteDao.insert(
                FavouriteEntity(
                    userId = userId,
                    productId = product.id
                )
            )
        } else {
            favouriteDao.removeFromFavourite(userId, product.id)
        }

        apiService.toggleFavourite(
            token,
            FavouriteToggles(productId = product.id)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun totalQuantity() = userRepository.user.flatMapLatest { user ->
        favouriteDao.getFavouriteCount(user.id)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun isFavourite(productId: Long): Flow<Boolean> {
        return userRepository.user.flatMapLatest { user ->
            favouriteDao.isFavourite(user.id, productId)
        }
    }

    suspend fun syncFavouritesWithServer() {
        val userId = currentUserId()
        val token = getAuthToken()

        val response = apiService.getFavourite(token)

        val favouriteItems = response.map {
            FavouriteEntity(
                userId = userId,
                productId = it.productId
            )
        }

        if (favouriteItems.isEmpty()) {

            favouriteDao.clearFavourites(userId)

        } else {

            val serverProductIds =
                favouriteItems.map { it.productId }

            favouriteDao.deleteNotInServer(
                userId = userId,
                serverProductIds = serverProductIds
            )

            favouriteDao.insertAll(favouriteItems)

            val products = response.map {
                ProductEntity(
                    productId = it.productId,
                    title = it.title,
                    brand = it.brand,
                    price = it.price,
                    thumbnail = it.thumbnail
                )
            }

            productDao.insertIntoProducts(products)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun favouriteProducts(): Flow<List<FavouriteResponse>> {
        return userRepository.user.flatMapLatest { user ->
            favouriteDao.getFavouriteProducts(user.id)
        }
    }

    suspend fun deleteFavourites() {
        val userId = currentUserId()
        val favourites = favouriteDao.getFavouriteIds(userId)
        val token = getAuthToken()

        favourites.forEach { productId ->
            apiService.toggleFavourite(
                token,
                FavouriteToggles(productId = productId)
            )
        }

        favouriteDao.clearFavourites(userId)
    }

    suspend fun removeOneFromFavourite(productId: Long) {
        val userId = currentUserId()
        val token = getAuthToken()

        try {
            apiService.toggleFavourite(
                token,
                FavouriteToggles(productId = productId)
            )
            favouriteDao.removeFromFavourite(userId, productId)
        }catch (e: HttpException){
            Log.e("Favourite_DELETE", "Failed to delete favourite product from the server due to http exception", e)
        }
        catch(e: Exception){
            Log.e("FAVOURITE_DELETE", "Failed to delete favourite product from the server.", e)
        }
    }

    suspend fun addFavourite(favourite: FavouriteResponse) {
        val userId = currentUserId()
        val token = getAuthToken()

        productDao.insertIntoProducts(
            listOf(
                ProductEntity(
                    productId = favourite.productId,
                    title = favourite.title,
                    thumbnail = favourite.thumbnail,
                    price = favourite.price,
                    brand = favourite.brand
                )
            )
        )

        favouriteDao.insert(
            FavouriteEntity(
                userId = userId,
                productId = favourite.productId
            )
        )
        apiService.toggleFavourite(
            token,
            FavouriteToggles(productId = favourite.productId)
        )
    }
}
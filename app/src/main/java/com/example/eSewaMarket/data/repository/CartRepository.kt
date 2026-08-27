package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.local.dao.CartDao
import com.example.eSewaMarket.data.local.dao.ProductDao
import com.example.eSewaMarket.data.local.entity.CartEntity
import com.example.eSewaMarket.data.local.entity.ProductEntity
import com.example.eSewaMarket.data.models.AddToCartRequest
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.models.ProductResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import kotlin.collections.map

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepository(
    private val cartDao: CartDao,
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

    suspend fun increaseQuantity(productId: Long) {
        val userId = currentUserId()
        val item = cartDao.getCartItem(userId, productId)

        val oldQuantity = item?.quantity?:0

        if (item == null) {
            cartDao.insert(
                CartEntity(userId, productId, quantity = 1)
            )
        } else {
            cartDao.updateQuantity(userId, productId, item.quantity + 1
            )
        }

        try {
            apiService.addToCart(
                getAuthToken(),
                AddToCartRequest(productId = productId)
            )
        }catch (e: Exception){
            if (oldQuantity == 0){
                cartDao.removeFromCart(userId, productId)
            }else{
                cartDao.updateQuantity(userId, productId, oldQuantity)
            }
            throw e
        }
    }

    suspend fun addToCart(product: Product) {

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

        increaseQuantity(product.id)
    }

    suspend fun addToCartFromFavourite(product: FavouriteResponse){
        productDao.insertIntoProducts(
            listOf(
                ProductEntity(
                    productId = product.productId,
                    title = product.title,
                    thumbnail = product.thumbnail,
                    price = product.price,
                    brand = product.brand
                )
            )
        )

        increaseQuantity(product.productId)
    }

    suspend fun removeOneFromCart(productId: Long) {

        val userId = currentUserId()
        val item = cartDao.getCartItem(userId, productId) ?: return
        val oldQuantity = item.quantity

        if (item.quantity == 1) {
            cartDao.removeFromCart(userId, productId)
        } else {
            cartDao.updateQuantity(userId, productId, item.quantity - 1)
        }

        try {
            apiService.removeOneFromCart(
                getAuthToken(),
                productId
            )
        }catch (e: Exception){
            if (oldQuantity == 1){
                cartDao.removeFromCart(userId, productId)
            }else{
                cartDao.updateQuantity(userId, productId, oldQuantity)
            }
            throw e
        }
    }

    fun totalQuantity() = userRepository.user.flatMapLatest { user ->
            cartDao.getTotalQuantity(user.id)
        }

    fun productQuantity(productId: Long) = userRepository.user.flatMapLatest { user ->
        cartDao.getProductQuantity(user.id, productId)
    }

    suspend fun syncCartWithServer() {
        val userId = currentUserId()
        val token = getAuthToken()
        val response = apiService.getCart(token)

        val cartItems = response.map {
            CartEntity(
                userId = userId,
                productId = it.productId,
                quantity = it.quantity
            )
        }

        val products = response.map {
            ProductEntity(
                productId = it.productId,
                title = it.title,
                thumbnail = it.thumbnail,
                price = it.price,
                brand = it.brand
            )
        }

        if (products.isNotEmpty()) {
            productDao.insertIntoProducts(products)
        }

        if (cartItems.isEmpty()) {
            cartDao.clearCart(userId)
        } else {
            val serverProductIds =
                cartItems.map { it.productId }

            cartDao.deleteNotInServer(
                userId = userId,
                serverProductIds = serverProductIds
            )
            cartDao.insertAll(cartItems)
        }
    }

    fun cartProducts() : Flow<List<ProductResponse>>{
        return userRepository.user.flatMapLatest { user ->
            cartDao.getCartProducts(user.id)
        }
    }

    suspend fun totalPrice() : Flow<Double?>{
        val userId = currentUserId()
        return cartDao.getTotalPrice(userId)
    }

    suspend fun itemCount() : Flow<Int>{
        val userId = currentUserId()
        return cartDao.getItemCount(userId)
    }
}
package com.example.eSewaMarket.data.api

import com.example.eSewaMarket.data.models.AddToCartRequest
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.data.models.FavouriteToggles
import com.example.eSewaMarket.data.models.HomeResponse
import com.example.eSewaMarket.data.models.HotDeal
import com.example.eSewaMarket.data.models.PageResponse
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.models.UserResponse
import com.example.eSewaMarket.data.models.UserSyncRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("categories")
    suspend fun getHotDealCategories(): List<HotDeal>

    @GET("products")
    suspend fun getProduct(): List<Product>

    @GET("home")
    suspend fun getHome(): HomeResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Long): Product

    @GET("products/page")
    suspend fun getRecommendedProducts(
        @Query("page") page: Int,
        @Query("size") size: Int = 6,
        @Query("sortBy") sortBy: String = "id",
        @Query("direction") direction: String = "asc"
    ): PageResponse<Product>

    @POST("users/sync")
    suspend fun syncUser(
        @Header("Authorization") token: String,
        @Body request: UserSyncRequest
    ): Response<UserResponse>

    @GET("users/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @POST("cart")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest
    )

    @PATCH("cart/product/{productId}/decrease")
    suspend fun removeOneFromCart(
        @Header("Authorization") token: String,
        @Path("productId") productId: Long
    )

    @GET("cart")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): List<ProductResponse>

    @POST("favourites")
    suspend fun toggleFavourite(
        @Header("Authorization") token: String,
        @Body request: FavouriteToggles
    )

    @GET("favourites")
    suspend fun getFavourite(
        @Header("Authorization") token: String
    ): List<ProductResponse>

    @GET("products/section")
    suspend fun getProductsBySection(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): PageResponse<Product>
}
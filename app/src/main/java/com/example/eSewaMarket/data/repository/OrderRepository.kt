package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.models.CreateOrderRequest
import com.example.eSewaMarket.data.models.OrderResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class OrderRepository(
    private val apiService: ApiService
) {

    private suspend fun getAuthToken(): String {
        val token = FirebaseAuth.getInstance()
            .currentUser
            ?.getIdToken(false)
            ?.await()
            ?.token
            ?: throw IllegalStateException("User is not authenticated.")

        return "Bearer $token"
    }

    suspend fun createOrder(
        shippingAddressId: Long,
        paymentOption: String
    ): Result<OrderResponse> {

        return try {

            val token = getAuthToken()

            val request = CreateOrderRequest(
                shippingAddressId = shippingAddressId,
                paymentOption = paymentOption
            )

            val response = apiService.createOrder(
                token = token,
                request = request
            )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
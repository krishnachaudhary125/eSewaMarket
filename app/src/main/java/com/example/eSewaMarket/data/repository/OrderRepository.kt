package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.models.CreateOrderRequest
import com.example.eSewaMarket.data.models.OrderDetailResponse
import com.example.eSewaMarket.data.models.OrderResponse
import com.example.eSewaMarket.data.models.VerifyEsewaPaymentRequest
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

    suspend fun getAllOrders(): List<OrderDetailResponse> {
        val token = getAuthToken()

        return apiService.getAllOrders(
            token = token
        )
    }

    suspend fun getOrderDetail(id: Long): OrderDetailResponse {
        val token = getAuthToken()

        return apiService.getOrderDetail(
            token = token,
            id = id
        )
    }

    suspend fun verifyEsewaPayment(
        orderId: Long,
        refId: String
    ): Result<OrderResponse> {

        return try {

            val token = getAuthToken()

            val request = VerifyEsewaPaymentRequest(
                refId = refId
            )

            val response = apiService.verifyEsewaPayment(
                token = token,
                orderId = orderId,
                request = request
            )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun deletePendingEsewaOrder(
        orderId: Long
    ): Result<Unit> {
        return try {
            val token = getAuthToken()

            apiService.deletePendingEsewaOrder(
                token = token,
                orderId = orderId
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
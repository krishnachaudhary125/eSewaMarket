package com.example.eSewaMarket.data.repository

import android.util.Log
import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.models.AddressRequest
import com.example.eSewaMarket.data.models.AddressResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AddressRepository(
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

    suspend fun createAddress(
        request: AddressRequest
    ): Result<AddressResponse> {

        return try {
            val token = getAuthToken()

            val response = apiService.createAddress(
                token = token,
                request = request
            )

            Result.success(response)

        } catch (e: Exception) {
            Log.e("LOCATION", "Unable to create address.", e)

            Result.failure(e)
        }
    }

    suspend fun getAddresses(): Result<List<AddressResponse>> {
        return try {
            val token = getAuthToken()

            Result.success(
                apiService.getAddresses(token)
            )
        } catch (e: Exception) {
            Log.e("ADDRESS", "Unable to fetch addresses.", e)
            Result.failure(e)
        }
    }

    suspend fun getAddress(id: Long): Result<AddressResponse> {
        return try {
            val token = getAuthToken()

            Result.success(
                apiService.getAddress(
                    token = token,
                    id = id
                )
            )
        } catch (e: Exception) {
            Log.e("ADDRESS", "Unable to fetch address.", e)
            Result.failure(e)
        }
    }
}
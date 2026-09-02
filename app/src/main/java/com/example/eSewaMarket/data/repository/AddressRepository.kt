package com.example.eSewaMarket.data.repository

import android.util.Log
import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.local.dao.AddressDao
import com.example.eSewaMarket.data.local.entity.AddressEntity
import com.example.eSewaMarket.data.models.AddressRequest
import com.example.eSewaMarket.data.models.AddressResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class AddressRepository(
    private val addressDao: AddressDao,
    private val userRepository: UserSessionRepository,
    private val apiService: ApiService
) {

    private suspend fun currentUserId(): Long {
        return userRepository.user.first().id
    }

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
            val userId = currentUserId()
            val token = getAuthToken()

            val response = apiService.createAddress(
                token = token,
                request = request
            )

            addressDao.insertAddress(
                AddressEntity(
                    id = response.id,
                    userId = userId,
                    fullName = response.fullName,
                    phone = response.phone,
                    province = response.province,
                    district = response.district,
                    city = response.city,
                    postalCode = response.postalCode,
                    addressName = response.addressName,
                    isDefaultAddress = response.isDefaultAddress,
                    isBillingAddress = response.isBillingAddress,
                    label = response.label
                )
            )

            Result.success(response)

        } catch (e: Exception) {
            Log.e("LOCATION", "Unable to create address.", e)

            Result.failure(e)
        }
    }

    suspend fun getAddresses(): Result<List<AddressResponse>> {

        return try {
            val userId = currentUserId()
            val token = getAuthToken()

            val response = apiService.getAddresses(token)

            val entities = response.map { address ->

                AddressEntity(
                    id = address.id,
                    userId = userId,
                    fullName = address.fullName,
                    phone = address.phone,
                    province = address.province,
                    district = address.district,
                    city = address.city,
                    postalCode = address.postalCode,
                    addressName = address.addressName,
                    isDefaultAddress = address.isDefaultAddress,
                    isBillingAddress = address.isBillingAddress,
                    label = address.label
                )
            }

            addressDao.insertAddresses(entities)

            Result.success(response)

        } catch (e: Exception) {
            Log.e("ADDRESS", "Unable to fetch addresses.", e)
            Result.failure(e)
        }
    }

    suspend fun getAddress(id: Long): Result<AddressResponse> {
        return try {
            val userId = currentUserId()
            val token = getAuthToken()

            val response = apiService.getAddress(
                token = token,
                id = id
            )

            addressDao.insertAddress(
                AddressEntity(
                    id = response.id,
                    userId = userId,
                    fullName = response.fullName,
                    phone = response.phone,
                    province = response.province,
                    district = response.district,
                    city = response.city,
                    postalCode = response.postalCode,
                    addressName = response.addressName,
                    isDefaultAddress = response.isDefaultAddress,
                    isBillingAddress = response.isBillingAddress,
                    label = response.label
                )
            )

            Result.success(response)

        } catch (e: Exception) {
            Log.e("ADDRESS", "Unable to fetch address.", e)
            Result.failure(e)
        }
    }

    suspend fun syncAddressWithServer(){
        val userId = currentUserId()
        val token = getAuthToken()
        val response = apiService.getAddresses(token)

        val addresses = response.map {
            AddressEntity(
                id = it.id,
                userId = userId,
                fullName = it.fullName,
                phone = it.phone,
                province = it.province,
                district = it.district,
                city = it.city,
                postalCode = it.postalCode,
                addressName = it.addressName,
                isDefaultAddress = it.isDefaultAddress,
                isBillingAddress = it.isBillingAddress,
                label = it.label
            )
        }

        if (addresses.isEmpty()){
            addressDao.clearAddress(userId)
        } else {
            val serverIds = addresses.map{
                it.id
            }

            addressDao.deleteNotInServer(
                userId = userId,
                serverIds = serverIds
            )

            addressDao.insertAddresses(addresses)
        }
    }
}
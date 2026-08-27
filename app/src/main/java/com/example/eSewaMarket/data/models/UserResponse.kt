package com.example.eSewaMarket.data.models

data class UserResponse(
    val id: Long,
    val firebaseUid: String,
    val name: String,
    val phone: String?,
    val email: String,
    val photoUrl: String?,
    val role: String,
    val createdAt: String,
    val updatedAt: String
)

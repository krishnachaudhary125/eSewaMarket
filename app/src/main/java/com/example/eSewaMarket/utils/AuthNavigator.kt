package com.example.eSewaMarket.utils

import android.content.Context
import com.example.eSewaMarket.data.repository.UserSessionRepository
import kotlinx.coroutines.flow.first

class AuthNavigator(
    private val userSessionRepository: UserSessionRepository
) {
    suspend fun isLoggedIn(): Boolean {
        return userSessionRepository.isLoggedIn.first()
    }
}
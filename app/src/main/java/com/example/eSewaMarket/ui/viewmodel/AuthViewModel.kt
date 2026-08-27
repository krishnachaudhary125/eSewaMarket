package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.eSewaMarket.data.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun logout() {
        authRepository.logout()
    }
}
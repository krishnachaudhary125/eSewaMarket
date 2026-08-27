package com.example.eSewaMarket.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository(
    private val userSessionRepository: UserSessionRepository,
) {
    suspend fun logout(){
        FirebaseAuth.getInstance().signOut()
        userSessionRepository.logout()
    }
}
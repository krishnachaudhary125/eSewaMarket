package com.example.eSewaMarket.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.UserResponse
import com.example.eSewaMarket.data.models.UserSyncRequest
import com.example.eSewaMarket.data.repository.UserRepository
import com.example.eSewaMarket.data.repository.UserSessionRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository()
    private val sessionRepository = UserSessionRepository(application)
    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun syncUser(
        token: String,
        request: UserSyncRequest
    ) {
        viewModelScope.launch {
            _loading.value = true

            try {
                val response = repository.syncUser(token, request)

                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    sessionRepository.saveUser(response.body()!!)
                    _user.value = user
                } else {
                    val errorBody = response.errorBody()?.string()

                    _error.value = "Code: ${response.code()}\n$errorBody"
                }

            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun getCurrentUser(token: String) {
        viewModelScope.launch {
            _loading.value = true

            try {
                val response = repository.getCurrentUser(token)

                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!

                    sessionRepository.saveUser(user)
                    _user.value = user

                } else {
                    val error = response.errorBody()?.string()
                    _error.value = "Code: ${response.code()}\n$error"
                }

            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }
}
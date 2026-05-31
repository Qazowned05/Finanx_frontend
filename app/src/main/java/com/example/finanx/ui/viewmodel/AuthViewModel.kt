package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.model.UserResponse
import com.example.finanx.data.repository.AuthRepository
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository(RetrofitClient.api)

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val response = repository.login(email, password)
                if (response.isSuccessful && response.body() != null) {
                    authState = AuthState.Success(response.body()!!.user)
                } else {
                    authState = AuthState.Error("Login fallido: ${response.code()}")
                }
            } catch (e: Exception) {
                authState = AuthState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val response = repository.register(name, email, password)
                if (response.isSuccessful && response.body() != null) {
                    authState = AuthState.Success(response.body()!!.user)
                } else {
                    authState = AuthState.Error("Registro fallido: ${response.code()}")
                }
            } catch (e: Exception) {
                authState = AuthState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun logout() {
        authState = AuthState.Idle
    }
}

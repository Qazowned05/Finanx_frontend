package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.repository.UserRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.UserRequest
import org.openapitools.client.models.UserResponse

sealed class UserProfileState {
    object Loading : UserProfileState()
    data class Success(val user: UserResponse) : UserProfileState()
    data class Error(val message: String) : UserProfileState()
}

sealed class UserActionState {
    object Idle : UserActionState()
    object Loading : UserActionState()
    data class Success(val message: String) : UserActionState()
    data class Error(val message: String) : UserActionState()
}

class UserViewModel : ViewModel() {
    private val repository = UserRepository(RetrofitClient.userApi)

    var profileState by mutableStateOf<UserProfileState>(UserProfileState.Loading)
        private set

    var updateNameState by mutableStateOf<UserActionState>(UserActionState.Idle)
        private set

    var updatePasswordState by mutableStateOf<UserActionState>(UserActionState.Idle)
        private set

    var deactivateState by mutableStateOf<UserActionState>(UserActionState.Idle)
        private set

    fun loadProfile(userId: Long) {
        viewModelScope.launch {
            profileState = UserProfileState.Loading
            try {
                val response = repository.findById(userId)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    profileState = UserProfileState.Success(body)
                } else {
                    profileState = UserProfileState.Error("Error al cargar perfil: ${response.code()}")
                }
            } catch (e: Exception) {
                profileState = UserProfileState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun updateName(userId: Long, newName: String, currentUser: UserResponse) {
        viewModelScope.launch {
            updateNameState = UserActionState.Loading
            try {
                val request = UserRequest(
                    name = newName,
                    lastname = currentUser.lastname ?: "",
                    email = currentUser.email ?: "",
                    username = currentUser.username ?: "",
                    password = "",
                    timeZone = currentUser.timeZone?.toRequestTimeZone() ?: UserRequest.TimeZone.AMERICA_LIMA
                )
                val response = repository.update(userId, request)
                if (response.isSuccessful) {
                    updateNameState = UserActionState.Success("Nombre actualizado correctamente")
                    loadProfile(userId)
                } else {
                    updateNameState = UserActionState.Error("Error al actualizar nombre: ${response.code()}")
                }
            } catch (e: Exception) {
                updateNameState = UserActionState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun updatePassword(userId: Long, newPassword: String, currentUser: UserResponse) {
        viewModelScope.launch {
            updatePasswordState = UserActionState.Loading
            try {
                val request = UserRequest(
                    name = currentUser.name ?: "",
                    lastname = currentUser.lastname ?: "",
                    email = currentUser.email ?: "",
                    username = currentUser.username ?: "",
                    password = newPassword,
                    timeZone = currentUser.timeZone?.toRequestTimeZone() ?: UserRequest.TimeZone.AMERICA_LIMA
                )
                val response = repository.update(userId, request)
                if (response.isSuccessful) {
                    updatePasswordState = UserActionState.Success("Contraseña actualizada correctamente")
                } else {
                    updatePasswordState = UserActionState.Error("Error al actualizar contraseña: ${response.code()}")
                }
            } catch (e: Exception) {
                updatePasswordState = UserActionState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun deactivateAccount(userId: Long) {
        viewModelScope.launch {
            deactivateState = UserActionState.Loading
            try {
                val response = repository.deactivate(userId)
                if (response.isSuccessful) {
                    deactivateState = UserActionState.Success("Cuenta deshabilitada correctamente")
                } else {
                    deactivateState = UserActionState.Error("Error al deshabilitar cuenta: ${response.code()}")
                }
            } catch (e: Exception) {
                deactivateState = UserActionState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun resetUpdateNameState() { updateNameState = UserActionState.Idle }
    fun resetUpdatePasswordState() { updatePasswordState = UserActionState.Idle }
    fun resetDeactivateState() { deactivateState = UserActionState.Idle }
}

private fun UserResponse.TimeZone.toRequestTimeZone(): UserRequest.TimeZone {
    return UserRequest.TimeZone.valueOf(this.name)
}

package com.example.finanx.ui.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.api.TokenStore
import com.example.finanx.data.auth.GoogleAuthError
import com.example.finanx.data.auth.GoogleAuthManager
import com.example.finanx.data.auth.GoogleAuthOutcome
import com.example.finanx.data.auth.UserManager
import com.example.finanx.data.auth.WEB_CLIENT_ID
import com.example.finanx.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openapitools.client.models.UserResponse

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository(RetrofitClient.authApi)

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    private fun onAuthSuccess(user: UserResponse, token: String?) {
        UserManager.userId = user.id ?: 0L
        UserManager.currentUser = user
        TokenStore.token = token ?: ""
        authState = AuthState.Success(user)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            authState = try {
                val response = repository.login(email, password)
                val body = response.body()
                val user = body?.user
                if (response.isSuccessful && user != null) {
                    onAuthSuccess(user, body.token)
                    AuthState.Success(user)
                } else {
                    val msg = response.errorBody()?.string()?.let { b ->
                        b.split("\"message\":\"").getOrNull(1)?.split("\"")?.getOrNull(0)
                    } ?: "Código ${response.code()}"
                    AuthState.Error(msg)
                }
            } catch (e: Exception) {
                AuthState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun register(
        name: String,
        lastname: String,
        email: String,
        username: String,
        password: String,
        timeZone: org.openapitools.client.models.UserRequest.TimeZone
    ) {
        TokenStore.token = ""
        viewModelScope.launch {
            authState = AuthState.Loading
            authState = try {
                val response = repository.register(name, lastname, email, username, password, timeZone)
                val body = response.body()
                val user = body?.user
                if (response.isSuccessful && user != null) {
                    onAuthSuccess(user, body.token)
                    AuthState.Success(user)
                } else {
                    val errorMsg = response.errorBody()?.string()?.let { body ->
                        val msg = body.split("\"message\":\"")
                            .getOrNull(1)?.split("\"")?.getOrNull(0)
                        msg ?: "Código ${response.code()}"
                    } ?: "Código ${response.code()}"
                    AuthState.Error(errorMsg)
                }
            } catch (e: Exception) {
                AuthState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun loginWithGoogle(activity: Activity, forceButtonFlow: Boolean = false) {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val authManager = GoogleAuthManager.create(activity)
                val outcome = if (forceButtonFlow) {
                    authManager.signInWithButton(
                        activity = activity,
                        serverClientId = WEB_CLIENT_ID
                    )
                } else {
                    authManager.signInWithBottomSheet(
                        activity = activity,
                        serverClientId = WEB_CLIENT_ID
                    )
                }

                when (outcome) {
                    is GoogleAuthOutcome.Success -> {
                        val idToken = outcome.result.idToken
                        val response = repository.loginWithGoogle(idToken)
                        val body = response.body()
                        val user = body?.user
                        if (response.isSuccessful && user != null) {
                            onAuthSuccess(user, body.token)
                            authState = AuthState.Success(user)
                        } else {
                            val msg = response.errorBody()?.string()?.let { b ->
                                b.split("\"message\":\"").getOrNull(1)?.split("\"")?.getOrNull(0)
                            } ?: "Código ${response.code()}"
                            authState = AuthState.Error(msg)
                        }
                    }

                    is GoogleAuthOutcome.Failure -> {
                        authState = AuthState.Error(outcome.error.toUserMessage())
                    }
                }
            } catch (e: Exception) {
                authState = AuthState.Error("Error en Google Sign-In: ${e.message}")
            }
        }
    }

    fun logout(activity: Activity? = null) {
        // Limpiamos el estado local de forma síncrona para que la UI reaccione inmediatamente
        UserManager.userId = 0L
        UserManager.currentUser = null
        TokenStore.token = ""
        authState = AuthState.Idle

        // Ejecutamos el cierre de sesión de Google de forma asíncrona
        viewModelScope.launch {
            try {
                activity?.let {
                    GoogleAuthManager.create(it.applicationContext).signOut()
                }
            } catch (_: Throwable) {
                // Errores al cerrar sesión en Google no deberían impedir el logout de la app
            }
        }
    }
}

private fun GoogleAuthError.toUserMessage(): String =
    when (this) {
        GoogleAuthError.Cancelled -> "Acceso cancelado"
        GoogleAuthError.NoCredential -> "No hay cuentas de Google disponibles"
        GoogleAuthError.UnexpectedCredential -> "No se pudo obtener credenciales de Google"
        GoogleAuthError.TokenParsingFailed -> "Respuesta de Google inválida"
        GoogleAuthError.ProviderConfiguration -> "Google/Play Services no está configurado correctamente"
        is GoogleAuthError.Unknown -> reason ?: "Error desconocido"
    }

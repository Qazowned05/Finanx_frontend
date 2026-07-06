package com.example.finanx.ui.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.auth.TokenStore
import com.example.finanx.data.api.parseErrorMessage
import com.example.finanx.data.auth.GoogleAuthError
import com.example.finanx.data.auth.GoogleAuthManager
import com.example.finanx.data.auth.GoogleAuthOutcome
import com.example.finanx.data.auth.LoginType
import com.example.finanx.data.auth.UserManager
import com.example.finanx.data.auth.WEB_CLIENT_ID
import com.example.finanx.data.repository.AuthRepository
import com.example.finanx.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.UserResponse
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val repository: AuthRepository
) : ViewModel() {

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    /**
     * Intenta restaurar la sesión desde el token persistido en disco.
     * Se llama desde MainScreen al arrancar la app.
     * Si el token existe, lo valida contra el backend y restaura el estado.
     * Si expiró o es inválido, limpia y muestra el login.
     */
    fun restoreSession() {
        if (!TokenStore.isLoggedIn) return

        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val response = userRepository.findById(TokenStore.userId)
                val user = response.body()
                if (response.isSuccessful && user != null) {
                    UserManager.userId = user.id ?: 0L
                    UserManager.currentUser = user
                    UserManager.loginType = LoginType.valueOf(TokenStore.loginType)
                    authState = AuthState.Success(user)
                } else {
                    TokenStore.clear()
                    authState = AuthState.Idle
                }
            } catch (e: Exception) {
                authState = AuthState.Idle
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            authState = try {
                val response = repository.login(email, password)
                val body = response.body()
                val user = body?.user
                if (response.isSuccessful && user != null) {
                    onAuthSuccess(user, body.token, LoginType.LOCAL)
                    AuthState.Success(user)
                } else {
                    AuthState.Error(response.parseErrorMessage())
                }
            } catch (e: Exception) {
                AuthState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun register(
        username: String,
        name: String,
        lastname: String,
        email: String,
        password: String,
        timeZone: org.openapitools.client.models.UserRequest.TimeZone
    ) {
        viewModelScope.launch {
            authState = AuthState.Loading
            authState = try {
                val response = repository.register(username, name, lastname, email, password, timeZone)
                val body = response.body()
                val user = body?.user
                if (response.isSuccessful && user != null) {
                    onAuthSuccess(user, body.token, LoginType.LOCAL)
                    AuthState.Success(user)
                } else {
                    AuthState.Error(response.parseErrorMessage())
                }
            } catch (e: Exception) {
                AuthState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun loginWithGoogle(activity: Activity) {
        viewModelScope.launch {
            authState = AuthState.Loading
            authState = try {
                val authManager = GoogleAuthManager.create(activity)
                val outcome = authManager.signInWithButton(
                    activity = activity,
                    serverClientId = WEB_CLIENT_ID
                )

                when (outcome) {
                    is GoogleAuthOutcome.Success -> {
                        val idToken = outcome.result.idToken
                        val response = repository.loginWithGoogle(idToken)
                        val body = response.body()
                        val user = body?.user
                        if (response.isSuccessful && user != null) {
                            onAuthSuccess(user, body.token, LoginType.GOOGLE)
                            AuthState.Success(user)
                        } else {
                            AuthState.Error(response.parseErrorMessage())
                        }
                    }
                    is GoogleAuthOutcome.Failure -> {
                        AuthState.Error(outcome.error.toUserMessage())
                    }
                }
            } catch (e: Exception) {
                AuthState.Error("Error en Google Sign-In: ${e.message}")
            }
        }
    }

    fun logout(activity: Activity? = null) {
        TokenStore.clear()
        UserManager.userId = 0L
        UserManager.currentUser = null
        authState = AuthState.Idle

        viewModelScope.launch {
            try {
                activity?.let {
                    GoogleAuthManager.create(it.applicationContext).signOut()
                }
            } catch (_: Throwable) {}
        }
    }

    private fun onAuthSuccess(user: UserResponse, token: String?, type: LoginType) {
        val safeToken = token ?: ""
        TokenStore.token = safeToken
        TokenStore.userId = user.id ?: 0L
        TokenStore.loginType = type.name
        UserManager.userId = user.id ?: 0L
        UserManager.currentUser = user
        UserManager.loginType = type
        authState = AuthState.Success(user)
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
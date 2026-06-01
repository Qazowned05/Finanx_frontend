package com.example.finanx.ui.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.auth.GoogleAuthError
import com.example.finanx.data.auth.GoogleAuthManager
import com.example.finanx.data.auth.GoogleAuthOutcome
import com.example.finanx.data.auth.WEB_CLIENT_ID
import com.example.finanx.data.repository.AuthRepository
import kotlinx.coroutines.launch
import org.openapitools.client.apis.AuthControllerApi
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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            authState = try {
                val response = repository.login(email, password)
                println("Login try: $response")
                val body = response.body()
                val user = body?.user
                if (response.isSuccessful && user != null) {
                    AuthState.Success(user)
                } else {
                    AuthState.Error("Login fallido: ${response.code()}")
                }
            } catch (e: Exception) {
                println(e.message)
                println(e.cause)
                println(e.stackTrace)
                AuthState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            authState = try {
                val response = repository.register(name, email, password)
                val body = response.body()
                val user = body?.user
                if (response.isSuccessful && user != null) {
                    AuthState.Success(user)
                } else {
                    AuthState.Error("Registro fallido: ${response.code()}")
                }
            } catch (e: Exception) {
                AuthState.Error("Error de red: ${e.message}")
            }
        }
    }

    /**
     * Acceder con Google (Credential Manager). Este método abre la UI de Google.
     *
     * - Usa la hoja inferior (bottom sheet) como flujo principal.
     * - Si el usuario la canceló o no hay credenciales, puedes volver a intentar
     *   usando [forceButtonFlow] = true desde un botón.
     */
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
                            authState = AuthState.Success(user)
                        } else {
                            authState = AuthState.Error("Login con Google fallido: ${response.code()}")
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

    /** Borra estado local + estado de credenciales para permitir elegir otra cuenta luego. */
    fun logout(activity: Activity? = null) {
        viewModelScope.launch {
            try {
                if (activity != null) {
                    GoogleAuthManager.create(activity).signOut()
                }
            } catch (_: Exception) {
                // Ignorar: cerrar sesión debe ser best-effort.
            } finally {
                authState = AuthState.Idle
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

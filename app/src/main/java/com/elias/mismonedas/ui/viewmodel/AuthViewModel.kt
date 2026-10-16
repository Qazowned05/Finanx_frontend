package com.elias.mismonedas.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.auth.SecurityStore
import com.elias.mismonedas.data.auth.TokenStore
import com.elias.mismonedas.data.api.parseErrorMessage
import com.elias.mismonedas.data.auth.GoogleAuthError
import com.elias.mismonedas.data.auth.GoogleAuthManager
import com.elias.mismonedas.data.auth.GoogleAuthOutcome
import com.elias.mismonedas.data.auth.LoginType
import com.elias.mismonedas.data.auth.UserManager
import com.elias.mismonedas.data.auth.WEB_CLIENT_ID
import com.elias.mismonedas.data.repository.AuthRepository
import com.elias.mismonedas.data.repository.FCMTokenRepository
import com.elias.mismonedas.data.repository.UserPreferencesRepository
import com.elias.mismonedas.data.repository.UserRepository
import com.elias.mismonedas.ui.components.TutorialPreferences
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.openapitools.client.models.TimeZone
import org.openapitools.client.models.UserPreferencesRequest
import org.openapitools.client.models.UserResponse
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class RegistrationStep {
    object Pending : RegistrationStep()
    object Loading : RegistrationStep()
    data class Error(val message: String) : RegistrationStep()
    object Completed : RegistrationStep()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val repository: AuthRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val fcmTokenRepository: FCMTokenRepository,
    @dagger.hilt.android.qualifiers.ApplicationContext private val appContext: Context
) : ViewModel() {

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    var registrationStep by mutableStateOf<RegistrationStep?>(null)
        private set

    /**
     * Intenta restaurar la sesión desde el token persistido en disco.
     * Se llama desde MainScreen al arrancar la app.
     * FIX-002: Verifica JWT expirado localmente antes de llamar al backend.
     * FIX-004: Verifica timeout de sesión por inactividad (>30 min).
     * Si el token existe y es válido, lo valida contra el backend y restaura el estado.
     * Si expiró o es inválido, limpia y muestra el login.
     */
    fun restoreSession() {
        // FIX-002: JWT expirado → sesión inválida
        if (!TokenStore.isLoggedIn) {
            viewModelScope.launch { clearLocalSession() }
            return
        }

        // FIX-004: Timeout por inactividad
        if (TokenStore.isSessionExpired) {
            viewModelScope.launch { clearLocalSession() }
            return
        }

        // FIX-004: Marcar actividad al intentar restaurar
        TokenStore.updateLastActivity()

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
                    registerFcmToken(user.id ?: 0L)
                } else {
                    clearLocalSession()
                    authState = AuthState.Idle
                }
            } catch (e: Exception) {
                clearLocalSession()
                authState = AuthState.Idle
            }
        }
    }

    /**
     * FIX-004: Verifica si la sesión expiró por inactividad.
     * Se llama desde MainScreen en LifecycleEventObserver (ON_RESUME).
     */
    fun checkSessionTimeout() {
        if (TokenStore.isLoggedIn && TokenStore.isSessionExpired) {
            logout()
        }
    }

    private suspend fun clearLocalSession() {
        val userId = TokenStore.userId
        val currentToken = TokenStore.fcmToken
        if (userId > 0L && currentToken.isNotEmpty()) {
            try {
                val tokens = fcmTokenRepository.list().getOrNull()?.items
                val matching = tokens?.firstOrNull { it.token == currentToken }
                val tokenId = matching?.id
                if (tokenId != null) {
                    fcmTokenRepository.remove(tokenId)
                }
            } catch (_: Throwable) {}
        }
        TutorialPreferences.clear(TokenStore.userId)
        TokenStore.fcmToken = ""
        TokenStore.clear()
        SecurityStore.clearPin()
        try {
            GoogleAuthManager.create(appContext).signOut()
        } catch (_: Throwable) {}
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
        password: String
    ) {
        viewModelScope.launch {
            authState = AuthState.Loading
            authState = try {
                val response = repository.register(username, name, lastname, email, password)
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
                            try { authManager.signOut() } catch (_: Throwable) {}
                            AuthState.Error(
                                "Ocurrió un error, intenta de nuevo"
                            )
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

    fun checkRegistrationStatus(userId: Long) {
        viewModelScope.launch {
            val completed = TutorialPreferences.isRegistrationCompleted(userId).first()
            if (completed) {
                registrationStep = RegistrationStep.Completed
            } else {
                val result = preferencesRepository.getPreferences()
                if (result.isSuccess) {
                    TutorialPreferences.markRegistrationCompleted(userId)
                    registrationStep = RegistrationStep.Completed
                } else {
                    registrationStep = RegistrationStep.Pending
                }
            }
        }
    }

    fun registerPreferences(
        userId: Long,
        currencyCode: String,
        timeZone: TimeZone,
        emailNotifications: Boolean
    ) {
        viewModelScope.launch {
            registrationStep = RegistrationStep.Loading

            val prefsResult = preferencesRepository.createPreferences(
                userId = userId,
                currencyCode = currencyCode,
                timeZone = timeZone,
                emailNotifications = emailNotifications
            )

            if (prefsResult.isFailure) {
                registrationStep = RegistrationStep.Error(
                    prefsResult.exceptionOrNull()?.message ?: "Error al guardar preferencias"
                )
                return@launch
            }

            val walletResult = preferencesRepository.createWallet(
                currencyCode = currencyCode
            )

            if (walletResult.isFailure) {
                registrationStep = RegistrationStep.Error(
                    walletResult.exceptionOrNull()?.message ?: "Error al crear billetera"
                )
                return@launch
            }

            TutorialPreferences.markRegistrationCompleted(userId)
            registrationStep = RegistrationStep.Completed
        }
    }

    fun logout(activity: Activity? = null) {
        viewModelScope.launch {
            clearLocalSession()
            UserManager.userId = 0L
            UserManager.currentUser = null
            UserManager.loginType = LoginType.LOCAL
            registrationStep = null
            authState = AuthState.Idle
            activity?.let {
                try { GoogleAuthManager.create(it.applicationContext).signOut() } catch (_: Throwable) {}
            }
        }
    }

    /**
     * FIX-003: Limpia estado previo antes de sobreescribir con nueva sesión.
     * Evita que datos de User A contaminen la sesión de User B.
     */
    private fun onAuthSuccess(user: UserResponse, token: String?, type: LoginType) {
        val safeToken = token ?: ""

        // FIX-003: Limpiar estado del usuario anterior si existe
        if (TokenStore.isLoggedIn) {
            try {
                // Best-effort: limpiar sesión previa sin bloquear
                val prevUserId = TokenStore.userId
                val prevFcmToken = TokenStore.fcmToken
                if (prevUserId > 0L && prevFcmToken.isNotEmpty()) {
                    viewModelScope.launch {
                        val tokens = fcmTokenRepository.list().getOrNull()?.items
                        val matching = tokens?.firstOrNull { it.token == prevFcmToken }
                        matching?.id?.let { tokenId ->
                            fcmTokenRepository.remove(tokenId)
                        }
                    }
                }
            } catch (_: Throwable) {}
        }

        // Reset completo de UserManager
        UserManager.userId = 0L
        UserManager.currentUser = null
        UserManager.loginType = LoginType.LOCAL

        // Guardar nuevos datos
        TokenStore.token = safeToken
        TokenStore.userId = user.id ?: 0L
        TokenStore.loginType = type.name
        TokenStore.updateLastActivity()

        UserManager.userId = user.id ?: 0L
        UserManager.currentUser = user
        UserManager.loginType = type

        // FIX-003: Reset registration step
        registrationStep = null

        authState = AuthState.Success(user)
        registerFcmToken(user.id ?: 0L)
    }

    private fun registerFcmToken(userId: Long) {
        viewModelScope.launch {
            try {
                var token = TokenStore.fcmToken
                if (token.isEmpty()) {
                    token = FirebaseMessaging.getInstance().token.await()
                    TokenStore.fcmToken = token
                }
                if (token.isNotEmpty()) {
                    fcmTokenRepository.register(token, Build.MODEL)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to register FCM token", e)
            }
        }
    }

    companion object {
        private const val TAG = "AuthViewModel"
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
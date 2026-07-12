package com.elias.mismonedas.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.elias.mismonedas.ui.screens.auth.LoginScreen
import com.elias.mismonedas.ui.screens.auth.RegisterPreferencesScreen
import com.elias.mismonedas.ui.screens.auth.RegisterScreen
import com.elias.mismonedas.ui.screens.onboarding.OnboardingScreen
import com.elias.mismonedas.ui.components.TutorialPreferences
import kotlinx.coroutines.flow.first
import com.elias.mismonedas.data.auth.SecurityStore
import com.elias.mismonedas.ui.screens.security.LockScreen
import com.elias.mismonedas.ui.viewmodel.*

@Composable
fun MainScreen(authViewModel: AuthViewModel = hiltViewModel()) {
    val authState = authViewModel.authState
    var showRegister by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.restoreSession()
    }

    // FIX-004: Verificar timeout de sesión cuando la app vuelve a primer plano
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                authViewModel.checkSessionTimeout()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Idle) showRegister = false
    }

    when (authState) {
        is AuthState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
            }
        }

        is AuthState.Success -> {
            val user = authState.user
            val userId = user.id
            if (userId != null) {
                var onboardingState by remember { mutableStateOf<Boolean?>(null) }
                var retry by remember { mutableIntStateOf(0) }

                LaunchedEffect(userId, retry) {
                    try {
                        val done = TutorialPreferences.isOnboardingCompleted(userId).first()
                        onboardingState = !done
                    } catch (_: Exception) {
                        onboardingState = false
                    }
                }

                when (onboardingState) {
                    null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                        }
                    }
                    true -> OnboardingScreen(
                        userId = userId,
                        onComplete = { onboardingState = false }
                    )
                    false -> {
                        LaunchedEffect(userId) {
                            authViewModel.checkRegistrationStatus(userId)
                        }
                        when (authViewModel.registrationStep) {
                            null -> {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                                }
                            }
                            is RegistrationStep.Pending,
                            is RegistrationStep.Loading,
                            is RegistrationStep.Error -> RegisterPreferencesScreen(
                                userId = userId,
                                viewModel = authViewModel,
                                onComplete = { /* registrationStep cambia a Completed */ }
                            )
                            is RegistrationStep.Completed -> {
                                var isUnlocked by rememberSaveable {
                                    mutableStateOf(!SecurityStore.isLockEnabledForUser(userId))
                                }
                                if (isUnlocked) {
                                    AppNavigation(user = user, authViewModel = authViewModel)
                                } else {
                                    LockScreen(
                                        onUnlock = { isUnlocked = true },
                                        onLogout = { authViewModel.logout() }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                LaunchedEffect(Unit) { authViewModel.logout() }
            }
        }

        else -> {
            if (showRegister) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onBackToLogin = { showRegister = false }
                )
            } else {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = { showRegister = true }
                )
            }
        }
    }
}
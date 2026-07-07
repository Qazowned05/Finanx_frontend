package com.example.finanx.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finanx.ui.screens.auth.LoginScreen
import com.example.finanx.ui.screens.auth.RegisterScreen
import com.example.finanx.ui.screens.onboarding.OnboardingScreen
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.components.TutorialPreferences
import kotlinx.coroutines.flow.first
import com.example.finanx.ui.viewmodel.*

@Composable
fun MainScreen(authViewModel: AuthViewModel = hiltViewModel()) {
    val authState = authViewModel.authState
    var showRegister by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.restoreSession()
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
                    false -> AppNavigation(user = user, authViewModel = authViewModel)
                    true -> OnboardingScreen(
                        userId = userId,
                        onComplete = { onboardingState = false }
                    )
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
package com.example.finanx.ui

import android.app.Activity
import androidx.compose.foundation.clickable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.screens.auth.LoginScreen
import com.example.finanx.ui.screens.auth.RegisterScreen
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.viewmodel.*

@Composable
fun MainScreen(authViewModel: AuthViewModel = viewModel()) {
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
                CircularProgressIndicator(color = GoldAccent)
            }
        }

        is AuthState.Success -> {
            val user = authState.user
            val userId = user.id
            if (userId != null) {
                AppNavigation(user = user, authViewModel = authViewModel)
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
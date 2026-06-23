package com.example.finanx.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.screens.analytics.AnalyticsScreen
import com.example.finanx.ui.viewmodel.AuthState
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.AnalyticsViewModel

@Composable
fun AnalysisScreen(
    authViewModel: AuthViewModel,
    analyticsViewModel: AnalyticsViewModel = viewModel(),
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val authState = authViewModel.authState
    val userId = (authState as? AuthState.Success)?.user?.id ?: return

    AnalyticsScreen(
        userId    = userId,
        viewModel = analyticsViewModel,
        authViewModel = authViewModel,
        onNavigateToNotifications = onNavigateToNotifications,
        onNavigateToProfile = onNavigateToProfile
    )
}

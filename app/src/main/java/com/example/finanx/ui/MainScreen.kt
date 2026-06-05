package com.example.finanx.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finanx.ui.screens.AdminScreen
import com.example.finanx.ui.screens.AnalysisScreen
import com.example.finanx.ui.screens.HomeScreen
import com.example.finanx.ui.screens.TransactionsScreen
import com.example.finanx.ui.screens.budget.BudgetListScreen
import com.example.finanx.ui.viewmodel.BudgetViewModel
import com.example.finanx.ui.theme.BluePrimary

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.screens.auth.LoginScreen
import com.example.finanx.ui.screens.auth.RegisterScreen
import com.example.finanx.ui.screens.profile.ProfileScreen
import com.example.finanx.ui.viewmodel.AuthState
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.UserViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Transactions : Screen("transactions", "Movimientos", Icons.Default.List)
    object Calendar : Screen("budgets", "Presupuestos", Icons.Default.AccountBalanceWallet)
    object Analysis : Screen("analysis", "Análisis", Icons.Default.Analytics)
    object More : Screen("more", "Más", Icons.Default.Menu)
}

@Composable
fun MainScreen(authViewModel: AuthViewModel = viewModel()) {
    val authState = authViewModel.authState
    var showRegister by remember { mutableStateOf(false) }
    var showProfile by remember { mutableStateOf(false) }

    if (showProfile && authState is AuthState.Success) {
        val user = authState.user
        val userId = user.id
        if (userId != null) {
            ProfileScreen(
                user = user,
                userId = userId,
                viewModel = viewModel(),
                onBack = { showProfile = false }
            )
        } else {
            LaunchedEffect(Unit) { authViewModel.logout() }
        }
    } else if (authState is AuthState.Success) {
        val userId = authState.user.id
        if (userId != null) {
            AppNavigation(userId, authViewModel, onNavigateToProfile = { showProfile = true })
        } else {
            LaunchedEffect(Unit) { authViewModel.logout() }
        }
    } else {
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

@Composable
fun AppNavigation(
    userId: Long,
    authViewModel: AuthViewModel,
    onNavigateToProfile: () -> Unit
) {
    val navController = rememberNavController()
    val budgetViewModel: BudgetViewModel = viewModel()
    val screens = listOf(
        Screen.Home,
        Screen.Transactions,
        Screen.Calendar,
        Screen.Analysis,
        Screen.More
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = NavigationBarDefaults.containerColor) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BluePrimary,
                            selectedTextColor = BluePrimary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(authViewModel, onNavigateToProfile) }
            composable(Screen.Transactions.route) { TransactionsScreen(authViewModel, onNavigateToProfile) }
            composable(Screen.Calendar.route) { BudgetListScreen(userId, budgetViewModel, authViewModel, onNavigateToProfile) }
            composable(Screen.Analysis.route) { AnalysisScreen(authViewModel, onNavigateToProfile) }
            composable(Screen.More.route) { AdminScreen(authViewModel, onNavigateToProfile) }
        }
    }
}

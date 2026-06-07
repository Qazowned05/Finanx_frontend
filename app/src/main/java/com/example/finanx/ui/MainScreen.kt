package com.example.finanx.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finanx.ui.screens.AdminScreen
import com.example.finanx.ui.screens.AnalysisScreen
import com.example.finanx.ui.screens.HomeScreen
import com.example.finanx.ui.screens.auth.LoginScreen
import com.example.finanx.ui.screens.auth.RegisterScreen
import com.example.finanx.ui.screens.budget.BudgetListScreen
import com.example.finanx.ui.screens.category.CategoryListScreen
import com.example.finanx.ui.screens.saving.SavingGoalScreen
import com.example.finanx.ui.screens.transaction.CreateTransactionScreen
import com.example.finanx.ui.screens.transaction.TransactionsScreen
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.viewmodel.AuthState
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.BudgetViewModel
import com.example.finanx.ui.viewmodel.CategoryViewModel
import com.example.finanx.ui.viewmodel.SavingGoalViewModel
import com.example.finanx.ui.viewmodel.TransactionViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Transactions : Screen("transactions", "Movimientos", Icons.Default.List)
    object Calendar : Screen("budgets", "Presupuestos", Icons.Default.AccountBalanceWallet)
    object Analysis : Screen("analysis", "Análisis", Icons.Default.Analytics)
    object More : Screen("more", "Más", Icons.Default.Menu)
    object Categories : Screen("categories", "Categorías", Icons.Default.Category)
    object SavingGoals : Screen("savinggoals", "Metas de Ahorro", Icons.Default.Savings)
    object CreateTransaction : Screen("create_transaction", "Nueva Transacción", Icons.Default.Add) // Added for navigation
}

@Composable
fun MainScreen(authViewModel: AuthViewModel = viewModel()) {
    val authState = authViewModel.authState
    var showRegister by remember { mutableStateOf(false) }

    if (authState is AuthState.Success) {
        val userId = authState.user.id
        if (userId != null) {
            AppNavigation(userId, authViewModel)
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
fun AppNavigation(userId: Long, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val budgetViewModel: BudgetViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val savingGoalViewModel: SavingGoalViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()

    val screens = listOf(
        Screen.Home,
        Screen.Transactions,
        Screen.Calendar,
        Screen.Analysis,
        Screen.Categories,
        Screen.SavingGoals,
        Screen.More
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute !in listOf(Screen.CreateTransaction.route)) { // Hide bottom bar on create transaction screen
                NavigationBar(containerColor = NavigationBarDefaults.containerColor) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Transactions.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Transactions.route) {
                LaunchedEffect(userId) {
                    transactionViewModel.fetchTransactions(userId)
                }
                TransactionsScreen(
                    state = transactionViewModel.transactionState,
                    onNavigateToCreateTransaction = { navController.navigate(Screen.CreateTransaction.route) }
                )
            }
            composable(Screen.Calendar.route) { BudgetListScreen(userId, budgetViewModel, authViewModel) }
            composable(Screen.Analysis.route) { AnalysisScreen(authViewModel) }
            composable(Screen.More.route) {
                AdminScreen(
                    authViewModel = authViewModel,
                    onNavigateToCategories = { navController.navigate(Screen.Categories.route) },
                    onNavigateToSavingGoals = { navController.navigate(Screen.SavingGoals.route) }
                )
            }
            composable(Screen.Categories.route) {
                CategoryListScreen(userId, categoryViewModel, authViewModel)
            }
            composable(Screen.SavingGoals.route) {
                SavingGoalScreen(userId, savingGoalViewModel, authViewModel)
            }
            composable(Screen.CreateTransaction.route) {
                CreateTransactionScreen(navController, transactionViewModel, userId)
            }
        }
    }
}

package com.example.finanx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finanx.ui.screens.transaction.CreateTransactionScreen
import com.example.finanx.ui.screens.transaction.TransactionsScreen
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.theme.FinanxTheme
import com.example.finanx.ui.viewmodel.TransactionViewModel

// Definiciones de pantalla para la navegación
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Transactions : Screen("transactions", "Movimientos", Icons.Default.List)
    object Budgets : Screen("budgets", "Presupuestos", Icons.Default.AccountBalanceWallet)
    object Analysis : Screen("analysis", "Análisis", Icons.Default.Analytics)
    object More : Screen("more", "Más", Icons.Default.Menu)
    object CreateTransaction : Screen("create_transaction", "Nueva Transacción", Icons.Default.Add)
}

class MainActivity : ComponentActivity() {
    private val transactionViewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanxTheme {
                AppNavigation(transactionViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(transactionViewModel: TransactionViewModel) {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.Home,
        Screen.Transactions,
        Screen.Budgets,
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
            startDestination = Screen.Transactions.route, // Iniciar en transacciones
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { Text("Pantalla de Inicio") }
            composable(Screen.Budgets.route) { Text("Pantalla de Presupuestos") }
            composable(Screen.Analysis.route) { Text("Pantalla de Análisis") }
            composable(Screen.More.route) { Text("Pantalla de Más Opciones") }

            composable(Screen.Transactions.route) {
                transactionViewModel.fetchTransactions(1) // Usuario de ejemplo
                TransactionsScreen(
                    state = transactionViewModel.transactionState,
                    onNavigateToCreateTransaction = { navController.navigate(Screen.CreateTransaction.route) }
                )
            }
            composable(Screen.CreateTransaction.route) {
                CreateTransactionScreen(
                    navController = navController,
                    viewModel = transactionViewModel,
                    userId = 1L // Usuario de ejemplo
                )
            }
        }
    }
}

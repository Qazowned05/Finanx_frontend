package com.example.finanx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finanx.data.auth.UserManager
import com.example.finanx.ui.screens.HomeScreen
import com.example.finanx.ui.screens.transaction.CreateTransactionScreen
import com.example.finanx.ui.screens.transaction.TransactionsScreen
import com.example.finanx.ui.theme.FinanxTheme
import com.example.finanx.ui.viewmodel.TransactionViewModel
import com.example.finanx.ui.navigation.notificationNavigation

class MainActivity : ComponentActivity() {
    private val transactionViewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FinanxApp(transactionViewModel)
                }
            }
        }
    }
}

@Composable
fun FinanxApp(transactionViewModel: TransactionViewModel) {
    val navController = rememberNavController()

    // Cargar los movimientos y categorías al iniciar la app.
    // Esto se ejecutará solo una vez, cuando el NavHost se componga por primera vez.
    LaunchedEffect(Unit) {
        transactionViewModel.fetchTransactions(UserManager.userId)
        transactionViewModel.fetchCategories(UserManager.userId)
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        // Se agrega el destino de notificaciones al NavHost existente.
        // No modifica las rutas actuales de Home, Transacciones ni Crear Transacción.
        notificationNavigation(UserManager.userId)
        composable("transactions") {
            TransactionsScreen(
                state = transactionViewModel.transactionState,
                onNavigateToCreateTransaction = { navController.navigate("createTransaction") }
            )
        }
        composable("createTransaction") {
            CreateTransactionScreen(
                navController = navController,
                viewModel = transactionViewModel,
                userId = UserManager.userId // Usamos el ID del UserManager
            )
        }
    }
}

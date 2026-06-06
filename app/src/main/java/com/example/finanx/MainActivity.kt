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
import com.example.finanx.ui.screens.movement.CreateMovementScreen
import com.example.finanx.ui.screens.movement.MovementsScreen
import com.example.finanx.ui.theme.FinanxTheme
import com.example.finanx.ui.viewmodel.MovementViewModel

class MainActivity : ComponentActivity() {
    private val movementViewModel: MovementViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FinanxApp(movementViewModel)
                }
            }
        }
    }
}

@Composable
fun FinanxApp(movementViewModel: MovementViewModel) {
    val navController = rememberNavController()

    // Cargar los movimientos y categorías al iniciar la app.
    // Esto se ejecutará solo una vez, cuando el NavHost se componga por primera vez.
    LaunchedEffect(Unit) {
        movementViewModel.fetchMovements(UserManager.userId)
        movementViewModel.fetchCategories(UserManager.userId)
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("movements") {
            MovementsScreen(
                state = movementViewModel.movementState,
                onNavigateToCreateMovement = { navController.navigate("createMovement") }
            )
        }
        composable("createMovement") {
            CreateMovementScreen(
                navController = navController,
                viewModel = movementViewModel,
                userId = UserManager.userId // Usamos el ID del UserManager
            )
        }
    }
}

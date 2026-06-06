package com.example.finanx.ui.screens.movement

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.viewmodel.MovementState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.finanx.ui.components.MovementItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsScreen(
    state: MovementState,
    onNavigateToCreateMovement: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateMovement) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Movimiento")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is MovementState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is MovementState.Success -> {
                    val allMovements = state.income + state.expenses
                    if (allMovements.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay movimientos registrados.")
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Corregido: Usar 'createdAt' para ordenar
                            items(allMovements.sortedByDescending { it.createdAt }) { movement ->
                                MovementItem(movement)
                            }
                        }
                    }
                }
                is MovementState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = Color.Red)
                    }
                }
            }
        }
    }
}

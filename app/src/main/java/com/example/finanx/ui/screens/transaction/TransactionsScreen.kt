package com.example.finanx.ui.screens.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.viewmodel.TransactionState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.finanx.ui.components.SearchBar
import com.example.finanx.ui.components.SegmentedButton
import com.example.finanx.ui.components.TransactionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    state: TransactionState,
    onNavigateToCreateTransaction: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transacciones") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateTransaction) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Transacción")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp)) {
            SearchBar(value = "", onValueChange = {}, label = "Buscar")
            Spacer(modifier = Modifier.height(16.dp))
            SegmentedButton(items = listOf("Todas", "Programadas")) { /* TODO: Handle filter */ }
            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is TransactionState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is TransactionState.Success -> {
                    val allTransactions = state.income + state.expenses
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(allTransactions) { transaction ->
                            TransactionItem(transaction)
                        }
                    }
                }
                is TransactionState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = Color.Red)
                    }
                }
            }
        }
    }
}

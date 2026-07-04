package com.example.finanx.ui.screens

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.TransactionRecord
import com.example.finanx.ui.components.EmptyStateCard
import com.example.finanx.ui.components.ErrorMessage
import com.example.finanx.ui.util.toSearchDate
import com.example.finanx.ui.components.TransactionsFilterBar
import com.example.finanx.ui.components.ALL_TRANSACTIONS_CATEGORY
import com.example.finanx.ui.components.TransactionRecordItem
import com.example.finanx.ui.viewmodel.TransactionUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    state: TransactionUiState,
    onQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onNewTransaction: () -> Unit,
    onDeleteTransaction: (Long) -> Unit,
    onOpenTransactionDetails: (TransactionRecord) -> Unit
) {
    var selectedCategory by rememberSaveable { mutableStateOf(ALL_TRANSACTIONS_CATEGORY) }

    val visibleTransactions = state.transactions.filter { transaction ->
        val normalizedQuery = state.query.trim().lowercase()
        val dateString = transaction.createdAt.toSearchDate()
        val matchesQuery =
            normalizedQuery.isBlank() ||
                    transaction.description.lowercase().contains(normalizedQuery) ||
                    transaction.categoryName.lowercase().contains(normalizedQuery) ||
                    dateString.contains(normalizedQuery)
        val matchesCategory =
            selectedCategory == ALL_TRANSACTIONS_CATEGORY ||
                    transaction.categoryName.trim().equals(
                        selectedCategory.trim(),
                        ignoreCase = true
                    )

        matchesQuery && matchesCategory
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<TransactionRecord?>(null) }
    val datePickerState = rememberDatePickerState()

    val snackbarHostState = remember { SnackbarHostState() }

    val availableCategories = state.formOptions.categories
        .filter { it.label.isNotBlank() }
        .distinct()

    LaunchedEffect(availableCategories) {
        val labels = availableCategories.map { it.label }
        if (selectedCategory != ALL_TRANSACTIONS_CATEGORY &&
            selectedCategory !in labels
        ) {
            selectedCategory = ALL_TRANSACTIONS_CATEGORY
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNewTransaction() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Nuevo",
                )
            }
        }
        ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            TransactionsFilterBar(
                query = state.query,
                onQueryChange = onQueryChange,
                categories = availableCategories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                },
                onDateClick = { showDatePicker = true }
            )

            Spacer(Modifier.height(12.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorMessage(message = state.error, onRetry = onRefresh)
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp),
                        contentPadding =
                            PaddingValues(bottom = 88.dp)
                    ) {
                        if (visibleTransactions.isEmpty()) {
                            item {
                                EmptyStateCard(
                                    icon = Icons.Default.Add,
                                    title = "Aún nada por aquí",
                                    subtitle = "Empieza registrando tu primer movimiento.",
                                    actionLabel = "Nuevo movimiento",
                                    onAction = onNewTransaction
                                )
                            }
                        } else {
                            items(
                                items = visibleTransactions,
                                key = { it.id }
                            ) { transaction ->
                                TransactionRecordItem(
                                    transaction = transaction,
                                    modifier = Modifier.combinedClickable(
                                        onClick = { onOpenTransactionDetails(transaction) },
                                        onLongClick = { transactionToDelete = transaction }
                                    )
                                )
                            }
                        }
                    }
                }

            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
                        onQueryChange(sdf.format(Date(millis)))
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    transactionToDelete?.let { tx ->
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Eliminar movimiento") },
            text = { Text("Si elimina este movimiento, el balance y los registros relacionados se verán afectados. ¿Desea continuar?") },
            confirmButton = {
                TextButton(onClick = {
                    tx.id?.let { onDeleteTransaction(it) }
                    transactionToDelete = null
                }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { transactionToDelete = null }) { Text("Cancelar") } }
        )
    }
}

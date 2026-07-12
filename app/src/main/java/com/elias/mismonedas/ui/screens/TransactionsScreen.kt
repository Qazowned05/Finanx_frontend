package com.elias.mismonedas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.components.ErrorMessage
import com.elias.mismonedas.ui.util.toSearchDate
import com.elias.mismonedas.ui.components.TransactionsFilterBar
import com.elias.mismonedas.ui.components.ALL_TRANSACTIONS_CATEGORY
import com.elias.mismonedas.ui.components.TransactionRecordItem
import com.elias.mismonedas.ui.viewmodel.TransactionUiState
import org.openapitools.client.models.TransactionResponse
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
    onScanReceipt: () -> Unit,
    onDeleteTransaction: (Long) -> Unit,
    onOpenTransactionDetails: (TransactionResponse) -> Unit
) {
    var selectedCategory by rememberSaveable { mutableStateOf(ALL_TRANSACTIONS_CATEGORY) }

    val visibleTransactions = state.transactions.filter { transaction ->
        val normalizedQuery = state.query.trim().lowercase()
        val dateString = transaction.createdAt.toSearchDate()
        val matchesQuery =
            normalizedQuery.isBlank() ||
                    (transaction.reasonDescription?.lowercase()?.contains(normalizedQuery) == true) ||
                    (transaction.categoryName?.lowercase()?.contains(normalizedQuery) == true) ||
                    dateString.contains(normalizedQuery)
        val matchesCategory =
            selectedCategory == ALL_TRANSACTIONS_CATEGORY ||
                    transaction.categoryName?.trim().equals(
                        selectedCategory.trim(),
                        ignoreCase = true
                    )

        matchesQuery && matchesCategory
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<TransactionResponse?>(null) }
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
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FloatingActionButton(
                    onClick = { onScanReceipt() },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Escanear recibo",
                        modifier = Modifier.size(24.dp),
                    )
                }
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(4) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(72.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                            )
                        }
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
                    com.elias.mismonedas.ui.components.RefreshableList(
                        isRefreshing = state.isRefreshing,
                        onRefresh = onRefresh,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            verticalArrangement =
                                Arrangement.spacedBy(10.dp),
                            contentPadding =
                                PaddingValues(bottom = 88.dp)
                        ) {
                            if (visibleTransactions.isEmpty()) {
                                item {
                                    TransactionsEmptyState(
                                        hasQuery = state.query.isNotBlank() || selectedCategory != ALL_TRANSACTIONS_CATEGORY,
                                        onNewTransaction = onNewTransaction,
                                        onScanReceipt = onScanReceipt
                                    )
                                }
                            } else {
                                items(
                                    items = visibleTransactions,
                                    key = { it.id ?: 0 }
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
            text = { Text("Si eliminas este movimiento, el balance y los registros relacionados se veran afectados. Deseas continuar?") },
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

@Composable
private fun TransactionsEmptyState(
    hasQuery: Boolean,
    onNewTransaction: () -> Unit,
    onScanReceipt: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Receipt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = if (hasQuery) "No se encontraron movimientos" else "Aun no hay movimientos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Text(
            text = if (hasQuery) {
                "Intenta ajustar los filtros de busqueda"
            } else {
                "Registra tu primer gasto o ingreso para empezar a hacer seguimiento"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        if (!hasQuery) {
            Spacer(Modifier.height(4.dp))
            TextButton(onClick = onNewTransaction) {
                Text("Registrar movimiento")
            }
        }
    }
}

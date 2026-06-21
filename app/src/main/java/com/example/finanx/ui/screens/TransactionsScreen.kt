package com.example.finanx.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.data.model.ScheduledTransactionRecord
import com.example.finanx.data.model.TransactionRecord
import com.example.finanx.data.model.TransactionsTab
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.components.ProfileMenuAction
import com.example.finanx.ui.components.TransactionRecordItem
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.util.toReadableDate
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.TransactionUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    state: TransactionUiState,
    onQueryChange: (String) -> Unit,
    onTabChange: (TransactionsTab) -> Unit,
    onRefresh: () -> Unit,
    onNewTransaction: () -> Unit,
    onPauseSchedule: (Long) -> Unit,
    onResumeSchedule: (Long) -> Unit,
    onCancelSchedule: (Long) -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val normalizedQuery = state.query.trim().lowercase()

    val visibleTransactions =
        state.transactions.filter { transaction ->
            normalizedQuery.isBlank() ||
                    transaction.description
                        .lowercase()
                        .contains(normalizedQuery) ||
                    transaction.categoryName
                        .lowercase()
                        .contains(normalizedQuery)
        }

    val visibleSchedules =
        state.scheduledTransactions.filter { schedule ->
            normalizedQuery.isBlank() ||
                    schedule.description
                        .lowercase()
                        .contains(normalizedQuery) ||
                    schedule.categoryName
                        .lowercase()
                        .contains(normalizedQuery)
        }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Movimientos",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    ProfileMenuAction(
                        onMyProfile = onProfile,
                        onLogout = onLogout
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewTransaction,
                containerColor =
                    MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Nuevo movimiento",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text("Buscar por motivo o categoría")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(12.dp))

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    selected =
                        state.selectedTab == TransactionsTab.ALL,
                    onClick = {
                        onTabChange(TransactionsTab.ALL)
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 0,
                        count = 2
                    ),
                    label = {
                        Text("Todos")
                    }
                )

                SegmentedButton(
                    selected =
                        state.selectedTab ==
                                TransactionsTab.SCHEDULED,
                    onClick = {
                        onTabChange(TransactionsTab.SCHEDULED)
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 1,
                        count = 2
                    ),
                    label = {
                        Text("Programados")
                    }
                )
            }

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
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement =
                            Arrangement.Center,
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error,
                            color =
                                MaterialTheme.colorScheme.error
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(onClick = onRefresh) {
                            Text("Reintentar")
                        }
                    }
                }

                state.selectedTab == TransactionsTab.ALL -> {
                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp),
                        contentPadding =
                            PaddingValues(bottom = 88.dp)
                    ) {
                        if (visibleTransactions.isEmpty()) {
                            item {
                                EmptyTransactionsMessage(
                                    "No se encontraron movimientos"
                                )
                            }
                        } else {
                            items(
                                items = visibleTransactions,
                                key = { it.id }
                            ) {
                                TransactionRecordItem(it)
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp),
                        contentPadding =
                            PaddingValues(bottom = 88.dp)
                    ) {
                        if (visibleSchedules.isEmpty()) {
                            item {
                                EmptyTransactionsMessage(
                                    "No tienes movimientos programados"
                                )
                            }
                        } else {
                            items(
                                items = visibleSchedules,
                                key = { it.id }
                            ) { schedule ->
                                ScheduledTransactionItem(
                                    item = schedule,
                                    onPause = {
                                        onPauseSchedule(schedule.id)
                                    },
                                    onResume = {
                                        onResumeSchedule(schedule.id)
                                    },
                                    onCancel = {
                                        onCancelSchedule(schedule.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduledTransactionItem(
    item: ScheduledTransactionRecord,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.description,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = item.categoryName,
                    color = TextGray,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Próximo: ${item.nextRunAt.toReadableDate()}",
                    color = TextGray,
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    text = item.state,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Text(
                text = item.amount.toPen(),
                fontWeight = FontWeight.Bold
            )

            Box {
                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    if (item.state == "RUNNING") {
                        DropdownMenuItem(
                            text = { Text("Pausar") },
                            onClick = {
                                expanded = false
                                onPause()
                            }
                        )
                    }

                    if (item.state == "PAUSED") {
                        DropdownMenuItem(
                            text = { Text("Reanudar") },
                            onClick = {
                                expanded = false
                                onResume()
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text("Cancelar") },
                        onClick = {
                            expanded = false
                            onCancel()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyTransactionsMessage(
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = TextGray
        )
    }
}

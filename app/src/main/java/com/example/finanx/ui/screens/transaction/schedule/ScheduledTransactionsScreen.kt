package com.example.finanx.ui.screens.transaction.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.ScheduledTransactionRecord
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.util.toReadableDate
import com.example.finanx.ui.util.toSearchDate
import com.example.finanx.ui.viewmodel.ScheduledTransactionUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduledTransactionsScreen(
    state: ScheduledTransactionUiState,
    onQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onNewSchedule: () -> Unit,
    onPauseSchedule: (Long) -> Unit,
    onResumeSchedule: (Long) -> Unit,
    onCancelSchedule: (Long) -> Unit,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val normalizedQuery = state.query.trim().lowercase()

    val visibleSchedules = state.scheduledTransactions.filter { schedule ->
        val dateString = schedule.nextRunAt.toSearchDate()

        normalizedQuery.isBlank() ||
                schedule.description.lowercase().contains(normalizedQuery) ||
                schedule.categoryName.lowercase().contains(normalizedQuery) ||
                dateString.contains(normalizedQuery)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Programaciones",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewSchedule,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Nueva programación",
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
                    Text("Buscar por motivo, categoría o fecha")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            showDatePicker = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha"
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
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
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(onClick = onRefresh) {
                            Text("Reintentar")
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
                                EmptySchedulesMessage(
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
}

@Composable
private fun ScheduledTransactionItem(
    item: ScheduledTransactionRecord,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
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
private fun EmptySchedulesMessage(
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

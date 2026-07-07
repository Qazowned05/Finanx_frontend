package com.example.finanx.ui.screens.schedule

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.FinanxCard
import com.example.finanx.ui.components.SpotlightOverlay
import com.example.finanx.ui.components.TutorialPreferences
import com.example.finanx.ui.components.tutorialTarget
import com.example.finanx.ui.components.TutorialController
import com.example.finanx.ui.components.TutorialStep
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.util.toReadableDate
import com.example.finanx.ui.util.toSearchDate
import com.example.finanx.ui.viewmodel.RecurringState
import com.example.finanx.ui.viewmodel.ScheduleViewModel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.ScheduleResponse
import org.openapitools.client.models.TransactionScheduleResponse
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduledTransactionsScreen(
    viewModel: ScheduleViewModel,
    userId: Long,
    onNewSchedule: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val recurringState = viewModel.recurringState
    val focusManager = LocalFocusManager.current
    val normalizedQuery = query.trim().lowercase()
    val targetBounds = remember { mutableStateMapOf<String, Rect>() }
    var tutorialController by remember { mutableStateOf<TutorialController?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val completed = TutorialPreferences.isCompleted(userId, "tutorial_programaciones").first()
        if (!completed) {
            tutorialController = TutorialController(
                listOf(
                    TutorialStep("schedule_overview", "Aquí puedes programar movimientos y presupuestos que se repiten automáticamente: diario, semanal, mensual o anual."),
                    TutorialStep("new_schedule_button", "Toca el botón + para crear tu primera programación.")
                )
            ) {
                coroutineScope.launch {
                    TutorialPreferences.markCompleted(userId, "tutorial_programaciones")
                }
                tutorialController = null
            }
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val visibleTransactions = if (recurringState is RecurringState.Success) {
        recurringState.transactionSchedules.filter { ts ->
            normalizedQuery.isBlank() ||
                    (ts.categoryName ?: "").lowercase().contains(normalizedQuery) ||
                    ts.nextRunAt?.toLocalDate().toSearchDate().contains(normalizedQuery)
        }
    } else emptyList()

    val visibleBudgets = if (recurringState is RecurringState.Success) {
        recurringState.budgetSchedules.filter { bs ->
            normalizedQuery.isBlank() ||
                    (bs.description ?: "").lowercase().contains(normalizedQuery) ||
                    (bs.categoryName ?: "").lowercase().contains(normalizedQuery) ||
                    bs.nextRunAt?.toLocalDate().toSearchDate().contains(normalizedQuery)
        }
    } else emptyList()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewSchedule,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.tutorialTarget("new_schedule_button", targetBounds)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva programación", tint = MaterialTheme.colorScheme.onPrimary)
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
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Buscar por descripción, categoría o fecha") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { focusManager.clearFocus(); showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(12.dp))

            when (recurringState) {
                is RecurringState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                is RecurringState.Error -> Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(recurringState.message, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { viewModel.fetchAll(userId) }) { Text("Reintentar") }
                }

                is RecurringState.Success -> {
                    if (visibleTransactions.isEmpty() && visibleBudgets.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No tienes programaciones activas", color = TextGray)
                        }
                    } else {
                        com.example.finanx.ui.components.RefreshableList(
                            isRefreshing = (recurringState as? com.example.finanx.ui.viewmodel.RecurringState.Success)?.isRefreshing ?: false,
                            onRefresh = { viewModel.fetchAll(userId, force = true) },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                contentPadding = PaddingValues(bottom = 88.dp)
                            ) {
                                if (visibleTransactions.isNotEmpty()) {
                                    item {
                                        Text(
                                            "Movimientos programados",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = TextGray,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                    items(visibleTransactions, key = { "tx-${it.id}" }) { ts ->
                                        TransactionScheduleItem(
                                            ts = ts,
                                            onPause = { viewModel.pause(ts.id ?: 0, userId) },
                                            onResume = { viewModel.resume(ts.id ?: 0, userId) },
                                            onCancel = { viewModel.cancel(ts.id ?: 0, userId) }
                                        )
                                    }
                                }

                                if (visibleBudgets.isNotEmpty()) {
                                    item {
                                        Text(
                                            "Presupuestos programados",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = TextGray,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                    items(visibleBudgets, key = { "budget-${it.id}" }) { bs ->
                                        BudgetScheduleItem(
                                            bs = bs,
                                            onPause = { viewModel.pause(bs.id ?: 0, userId) },
                                            onResume = { viewModel.resume(bs.id ?: 0, userId) },
                                            onCancel = { viewModel.cancel(bs.id ?: 0, userId) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(tutorialController, tutorialController?.currentIndex) {
        if (tutorialController == null) return@LaunchedEffect
        snapshotFlow { tutorialController?.currentStep?.canAdvance() ?: false }
            .drop(1)
            .distinctUntilChanged()
            .filter { it }
            .collect { tutorialController?.next() }
    }

    if (tutorialController != null) {
        val current = tutorialController?.currentStep
        SpotlightOverlay(
            targetBounds = current?.let { targetBounds[it.targetKey] },
            message = current?.message ?: "",
            isLastStep = tutorialController?.isLastStep ?: false,
            showNextButton = !(current?.isAutoGated ?: false),
            onNext = { tutorialController?.next() },
            onSkip = {
                coroutineScope.launch {
                    TutorialPreferences.markCompleted(userId, "tutorial_programaciones")
                }
                tutorialController = null
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        query = sdf.format(Date(millis))
                    }
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }
}

@Composable
private fun TransactionScheduleItem(
    ts: TransactionScheduleResponse,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isPaused = ts.state == ScheduleResponse.State.PAUSED

    FinanxCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(ts.categoryName ?: "Movimiento programado", fontWeight = FontWeight.SemiBold)
                Text("Próximo: ${ts.nextRunAt?.toLocalDate().toReadableDate()}", color = TextGray, style = MaterialTheme.typography.labelSmall)
                Text(ts.state?.value ?: "", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
            }
            Text((ts.amount ?: BigDecimal.ZERO).toPen(), fontWeight = FontWeight.Bold)
            Box {
                IconButton(onClick = { expanded = true }) { Icon(Icons.Default.MoreVert, contentDescription = "Opciones") }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    if (!isPaused) DropdownMenuItem(text = { Text("Pausar") }, onClick = { expanded = false; onPause() })
                    if (isPaused) DropdownMenuItem(text = { Text("Reanudar") }, onClick = { expanded = false; onResume() })
                    DropdownMenuItem(text = { Text("Cancelar") }, onClick = { expanded = false; onCancel() })
                }
            }
        }
    }
}

@Composable
private fun BudgetScheduleItem(
    bs: BudgetScheduleResponse,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isPaused = bs.state == ScheduleResponse.State.PAUSED

    FinanxCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(bs.description ?: bs.categoryName ?: "Presupuesto programado", fontWeight = FontWeight.SemiBold)
                Text(bs.categoryName ?: "", color = TextGray, style = MaterialTheme.typography.bodySmall)
                Text("Próximo: ${bs.nextRunAt?.toLocalDate().toReadableDate()}", color = TextGray, style = MaterialTheme.typography.labelSmall)
                Text(bs.state?.value ?: "", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
            }
            Text("S/ ${bs.limitAmount ?: BigDecimal.ZERO}", fontWeight = FontWeight.Bold)
            Box {
                IconButton(onClick = { expanded = true }) { Icon(Icons.Default.MoreVert, contentDescription = "Opciones") }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    if (!isPaused) DropdownMenuItem(text = { Text("Pausar") }, onClick = { expanded = false; onPause() })
                    if (isPaused) DropdownMenuItem(text = { Text("Reanudar") }, onClick = { expanded = false; onResume() })
                    DropdownMenuItem(text = { Text("Cancelar") }, onClick = { expanded = false; onCancel() })
                }
            }
        }
    }
}

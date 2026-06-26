package com.example.finanx.ui.screens.budget.recurrent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.BrownSecondary
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.StoneGray
import com.example.finanx.ui.theme.SurfaceWhite
import com.example.finanx.ui.util.toReadableDate
import com.example.finanx.ui.viewmodel.CategoryState
import com.example.finanx.ui.viewmodel.RecurringActionState
import com.example.finanx.ui.viewmodel.RecurringState
import com.example.finanx.ui.viewmodel.ScheduleViewModel
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.ScheduleRequest
import org.openapitools.client.models.TransactionScheduleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@Composable
fun RecurringTab(scheduleViewModel: ScheduleViewModel, userId: Long) {
    val state = scheduleViewModel.recurringState

    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is RecurringState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrownPrimary)
            }
            is RecurringState.Success -> {
                if (state.transactionSchedules.isEmpty() && state.budgetSchedules.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Repeat, contentDescription = null, tint = BrownSecondary.copy(alpha = 0.4f), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Sin gastos o presupuestos fijos aún", color = BrownSecondary)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.transactionSchedules) { ts ->
                            RecurringTransactionCard(ts, onPause = { scheduleViewModel.pause(ts.id ?: 0, userId) }, onResume = { scheduleViewModel.resume(ts.id ?: 0, userId) }, onCancel = { scheduleViewModel.cancel(ts.id ?: 0, userId) })
                        }
                        items(state.budgetSchedules) { bs ->
                            RecurringBudgetCard(bs, onPause = { scheduleViewModel.pause(bs.id ?: 0, userId) }, onResume = { scheduleViewModel.resume(bs.id ?: 0, userId) }, onCancel = { scheduleViewModel.cancel(bs.id ?: 0, userId) })
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
            is RecurringState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun RecurringTransactionCard(ts: TransactionScheduleResponse, onPause: () -> Unit, onResume: () -> Unit, onCancel: () -> Unit) {
    val isPaused = ts.state == org.openapitools.client.models.ScheduleResponse.State.PAUSED
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(ts.categoryName ?: "Transacción recurrente", style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
                Text("S/ ${ts.amount ?: BigDecimal.ZERO}", style = MaterialTheme.typography.titleMedium, color = if (ts.transactionType?.value == "INCOME") IncomeGreen else ExpenseRed)
            }
            Text("Próxima ejecución: ${ts.nextRunAt?.toLocalDate().toReadableDate()}", style = MaterialTheme.typography.labelMedium, color = StoneGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = if (isPaused) onResume else onPause, modifier = Modifier.weight(1f)) {
                    Icon(if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isPaused) "Reanudar" else "Pausar")
                }
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancelar", color = ExpenseRed)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecurringDialog(
    userId: Long,
    categoryState: CategoryState,
    viewModel: ScheduleViewModel,
    onDismiss: () -> Unit
) {
    var kind by remember { mutableStateOf(0) } // 0 = transacción, 1 = presupuesto
    var description by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var transactionType by remember { mutableStateOf(TransactionScheduleRequest.TransactionType.SPENT) }
    var paymentMethod by remember { mutableStateOf(TransactionScheduleRequest.PaymentMethod.CASH) }
    var alertable by remember { mutableStateOf(true) }
    var percentAlertStr by remember { mutableStateOf("80") }
    var recurrenceType by remember { mutableStateOf(RecurrenceRuleRequest.RecurrenceType.MONTHLY) }
    var interval by remember { mutableStateOf("1") }

    val context = LocalContext.current
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusYears(1)) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val actionState = viewModel.actionState
    LaunchedEffect(actionState) {
        if (actionState is RecurringActionState.Success) {
            viewModel.resetActionState()
            onDismiss()
        }
    }

    val parsedAmount = amountStr.toBigDecimalOrNull()
    val parsedInterval = interval.toIntOrNull() ?: 1
    val parsedPercent = percentAlertStr.toIntOrNull() ?: 0
    val canCreate = selectedCategoryId != null &&
            (if (kind == 0) parsedAmount != null else description.isNotBlank() && parsedAmount != null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo recurrente", color = BrownPrimary) },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(max = 460.dp).verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = kind == 0, onClick = { kind = 0 }, label = { Text("Transacción") })
                    FilterChip(selected = kind == 1, onClick = { kind = 1 }, label = { Text("Presupuesto") })
                }

                if (categoryState is CategoryState.Success) {
                    ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = !categoryExpanded }) {
                        OutlinedTextField(
                            value = categoryState.categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                            categoryState.categories.forEach { cat ->
                                DropdownMenuItem(text = { Text(cat.name ?: "") }, onClick = { selectedCategoryId = cat.id; categoryExpanded = false })
                            }
                        }
                    }
                }

                if (kind == 1) {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it.replace(',', '.') },
                    label = { Text(if (kind == 0) "Monto (S/)" else "Límite (S/)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (kind == 0) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = transactionType == TransactionScheduleRequest.TransactionType.SPENT, onClick = { transactionType =
                            TransactionScheduleRequest.TransactionType.SPENT }, label = { Text("Gasto") })
                        FilterChip(selected = transactionType == TransactionScheduleRequest.TransactionType.INCOME, onClick = { transactionType =
                            TransactionScheduleRequest.TransactionType.INCOME }, label = { Text("Ingreso") })
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Alertas", color = StoneGray)
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(checked = alertable, onCheckedChange = { alertable = it })
                    }
                    OutlinedTextField(
                        value = percentAlertStr,
                        onValueChange = { percentAlertStr = it.filter { c -> c.isDigit() }.take(3) },
                        label = { Text("Porcentaje alerta") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Text("Recurrencia", style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        RecurrenceRuleRequest.RecurrenceType.DAILY to "Diario",
                        RecurrenceRuleRequest.RecurrenceType.WEEKLY to "Semanal",
                        RecurrenceRuleRequest.RecurrenceType.MONTHLY to "Mensual",
                        RecurrenceRuleRequest.RecurrenceType.YEARLY to "Anual"
                    ).forEach { (value, label) ->
                        FilterChip(selected = recurrenceType == value, onClick = { recurrenceType = value }, label = { Text(label) })
                    }
                }

                OutlinedTextField(
                    value = interval,
                    onValueChange = { interval = it.filter { c -> c.isDigit() }.take(2) },
                    label = { Text("Cada cuántos periodos") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = startDate.toReadableDate(),
                    onValueChange = {},
                    label = { Text("Fecha inicio") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { showStartPicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = null)
                        }
                    }
                )
                OutlinedTextField(
                    value = endDate.toReadableDate(),
                    onValueChange = {},
                    label = { Text("Fecha fin") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { showEndPicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    }
                )

                if (showStartPicker) {
                    val todayMillis = LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()

                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = startDate
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli(),
                        selectableDates = object : SelectableDates {
                            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                return utcTimeMillis >= todayMillis
                            }
                        }
                    )

                    DatePickerDialog(
                        onDismissRequest = { showStartPicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val newDate = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()

                                    startDate = newDate

                                    if (endDate.isBefore(startDate.plusDays(1))) {
                                        endDate = startDate.plusDays(1)
                                    }
                                }
                                showStartPicker = false
                            }) { Text("Aceptar") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showStartPicker = false }) {
                                Text("Cancelar")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                if (showEndPicker) {
                    val tomorrowMillis = LocalDate.now().plusDays(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()

                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = endDate
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli(),
                        selectableDates = object : SelectableDates {
                            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                return utcTimeMillis >= tomorrowMillis
                            }
                        }
                    )

                    DatePickerDialog(
                        onDismissRequest = { showEndPicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    endDate = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                }
                                showEndPicker = false
                            }) { Text("Aceptar") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showEndPicker = false }) {
                                Text("Cancelar")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                if (actionState is RecurringActionState.Error) {
                    Text(actionState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val catId = selectedCategoryId ?: return@Button
                    if (kind == 0) {
                        viewModel.createTransactionSchedule(
                            userId = userId,
                            categoryId = catId,
                            amount = parsedAmount ?: BigDecimal.ZERO,
                            paymentMethod = paymentMethod,
                            transactionType = transactionType,
                            recurrenceType = recurrenceType,
                            interval = parsedInterval,
                            start = startDate,
                            end = endDate
                        )
                    } else {
                        viewModel.createBudgetSchedule(
                            userId = userId,
                            categoryId = catId,
                            alertable = alertable,
                            percentAlert = parsedPercent,
                            limitAmount = parsedAmount ?: BigDecimal.ZERO,
                            description = description,
                            recurrenceType = recurrenceType,
                            interval = parsedInterval,
                            start = startDate,
                            end = endDate
                        )
                    }
                },
                enabled = canCreate && actionState !is RecurringActionState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
            ) {
                if (actionState is RecurringActionState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Crear")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = StoneGray) }
        }
    )
}
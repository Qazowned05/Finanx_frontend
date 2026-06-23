package com.example.finanx.ui.screens.budget

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.StoneGray
import com.example.finanx.ui.util.toReadableDate
import com.example.finanx.ui.viewmodel.BudgetActionState
import com.example.finanx.ui.viewmodel.BudgetViewModel
import com.example.finanx.ui.viewmodel.CategoryState
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBudgetDialog(
    userId: Long,
    categoryState: CategoryState,
    viewModel: BudgetViewModel,
    onDismiss: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var limitAmount by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var alertable by remember { mutableStateOf(true) }
    var percentAlertStr by remember { mutableStateOf("80") }
    var categoryExpanded by remember { mutableStateOf(false) }

    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusMonths(1)) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val actionState = viewModel.actionState

    LaunchedEffect(actionState) {
        if (actionState is BudgetActionState.Success) {
            viewModel.resetActionState()
            onDismiss()
        }
    }

    val parsedLimit = limitAmount.toBigDecimalOrNull()
    val parsedPercent = percentAlertStr.toIntOrNull()
    val canCreate = description.isNotBlank() && parsedLimit != null && selectedCategoryId != null && !endDate.isBefore(startDate)

    if (showStartPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = startDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val today = LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    return utcTimeMillis >= today
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        startDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showStartPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEndPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = endDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val tomorrow = LocalDate.now().plusDays(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    return utcTimeMillis >= tomorrow
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
                TextButton(onClick = { showEndPicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo presupuesto", color = BrownPrimary) },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp).verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = limitAmount,
                    onValueChange = { limitAmount = it.replace(',', '.') },
                    label = { Text("Monto límite (S/)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (categoryState is CategoryState.Success) {
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
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
                                DropdownMenuItem(
                                    text = { Text(cat.name ?: "") },
                                    onClick = { selectedCategoryId = cat.id; categoryExpanded = false }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = startDate.toReadableDate(),
                    onValueChange = {},
                    label = { Text("Fecha inicio") },
                    modifier = Modifier.fillMaxWidth().clickable { showStartPicker = true },
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = endDate.toReadableDate(),
                    onValueChange = {},
                    label = { Text("Fecha fin") },
                    modifier = Modifier.fillMaxWidth().clickable { showEndPicker = true },
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp)
                )

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

                if (actionState is BudgetActionState.Error) {
                    Text(actionState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val request = BudgetRequest(
                        alertable = alertable,
                        limitAmount = parsedLimit ?: BigDecimal.ZERO,
                        description = description,
                        userId = userId,
                        categoryId = selectedCategoryId ?: 0L,
                        start = startDate,
                        end = endDate,
                        percentAlert = parsedPercent
                    )
                    viewModel.createBudget(request, userId)
                },
                enabled = canCreate && actionState !is BudgetActionState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
            ) {
                if (actionState is BudgetActionState.Loading) {
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBudgetDialog(
    userId: Long,
    budget: BudgetExecutionResponse,
    categoryState: CategoryState,
    viewModel: BudgetViewModel,
    onDismiss: () -> Unit
) {
    var description by remember { mutableStateOf(budget.description.orEmpty()) }
    var limitAmount by remember { mutableStateOf(budget.limitAmount?.toPlainString() ?: "") }
    var alertable by remember { mutableStateOf(budget.alertable ?: true) }
    var percentAlertStr by remember { mutableStateOf(budget.percentAlert?.toString() ?: "80") }

    var endDate by remember {
        mutableStateOf(budget.end?.toLocalDate() ?: LocalDate.now().plusMonths(1))
    }
    var showEndPicker by remember { mutableStateOf(false) }

    val actionState = viewModel.actionState

    LaunchedEffect(actionState) {
        if (actionState is BudgetActionState.Success) {
            viewModel.resetActionState()
            onDismiss()
        }
    }

    val parsedLimit = limitAmount.toBigDecimalOrNull()
    val parsedPercent = percentAlertStr.toIntOrNull()
    val canUpdate = description.isNotBlank() && parsedLimit != null

    if (showEndPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = endDate
                .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val tomorrow = LocalDate.now().plusDays(1)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    return utcTimeMillis >= tomorrow
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        endDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showEndPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar presupuesto", color = BrownPrimary) },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp).verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = limitAmount,
                    onValueChange = { limitAmount = it.replace(',', '.') },
                    label = { Text("Monto límite (S/)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = endDate.toReadableDate(),
                    onValueChange = {},
                    label = { Text("Fecha límite") },
                    modifier = Modifier.fillMaxWidth().clickable { showEndPicker = true },
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp)
                )
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

                if (actionState is BudgetActionState.Error) {
                    Text(actionState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val request = BudgetRequest(
                        alertable = alertable,
                        limitAmount = parsedLimit ?: BigDecimal.ZERO,
                        description = description,
                        userId = userId,
                        categoryId = budget.categoryId ?: 0L,
                        start = budget.start?.toLocalDate() ?: LocalDate.now(),
                        end = endDate,
                        percentAlert = parsedPercent
                    )
                    viewModel.updateBudget(budget.id ?: 0L, request, userId)
                },
                enabled = canUpdate && actionState !is BudgetActionState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
            ) {
                if (actionState is BudgetActionState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = StoneGray) }
        }
    )
}
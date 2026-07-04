package com.example.finanx.ui.screens.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.CategoryDropdown
import com.example.finanx.ui.components.toCategoryOption
import com.example.finanx.ui.screens.schedule.ScheduleDateField
import com.example.finanx.ui.screens.schedule.toLocalDateOrNull
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.StoneGray
import com.example.finanx.ui.viewmodel.BudgetActionState
import com.example.finanx.ui.viewmodel.BudgetViewModel
import com.example.finanx.ui.viewmodel.CategoryState
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetRequest
import java.math.BigDecimal
import java.time.LocalDate

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

    var startDateText by remember { mutableStateOf("") }
    var endDateText by remember { mutableStateOf("") }

    val parsedStart = startDateText.toLocalDateOrNull()
    val parsedEnd = endDateText.toLocalDateOrNull()

    val actionState = viewModel.actionState

    LaunchedEffect(actionState) {
        if (actionState is BudgetActionState.Success) {
            viewModel.resetActionState()
            onDismiss()
        }
    }

    val parsedLimit = limitAmount.toBigDecimalOrNull()
    val parsedPercent = percentAlertStr.toIntOrNull()

    val canCreate =
        description.isNotBlank() &&
                parsedLimit != null &&
                parsedLimit.signum() > 0 &&
                selectedCategoryId != null &&
                parsedStart != null &&
                parsedEnd != null &&
                !parsedStart.isBefore(LocalDate.now()) &&
                !parsedEnd.isBefore(parsedStart) &&
                (!alertable || (parsedPercent != null && parsedPercent in 0..100))

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
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    supportingText = {
                        Text("Resume el objetivo del presupuesto")
                    }
                )

                OutlinedTextField(
                    value = limitAmount,
                    onValueChange = { limitAmount = it.replace(',', '.') },
                    label = { Text("Monto límite (S/)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    supportingText = {
                        Text("Usa hasta dos decimales")
                    },
                    isError = limitAmount.isNotBlank() && (parsedLimit == null || parsedLimit.signum() <= 0),
                    singleLine = true
                )

                if (categoryState is CategoryState.Success) {
                    CategoryDropdown(
                        categories = categoryState.categories.mapNotNull { it.toCategoryOption() },
                        selectedCategoryId = selectedCategoryId,
                        enabled = true,
                        onCategorySelected = { selectedCategoryId = it }
                    )
                }

                ScheduleDateField(
                    label = "Fecha inicio",
                    value = startDateText,
                    enabled = true,
                    isError = parsedStart == null && startDateText.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    minSelectableDate = LocalDate.now(),
                    supportingText = if (parsedStart == null && startDateText.isNotEmpty()) {
                        "Ingresa una fecha válida"
                    } else {
                        "Formato dd-MM-yyyy"
                    },
                    onValueChange = { newValue ->
                        startDateText = newValue
                        val start = newValue.toLocalDateOrNull()
                        val end = endDateText.toLocalDateOrNull()

                        if (start != null && end != null && end.isBefore(start.plusDays(1))) {
                            endDateText = start.plusDays(1).toString()
                        }
                    }
                )

                ScheduleDateField(
                    label = "Fecha fin",
                    value = endDateText,
                    enabled = true,
                    isError = parsedEnd != null &&
                            parsedStart != null &&
                            parsedEnd.isBefore(parsedStart),
                    modifier = Modifier.fillMaxWidth(),
                    minSelectableDate = LocalDate.now().plusDays(1),
                    supportingText = when {
                        parsedEnd == null && endDateText.isNotEmpty() -> "Ingresa una fecha válida"
                        parsedEnd != null && parsedStart != null && parsedEnd.isBefore(parsedStart) ->
                            "La fecha fin debe ser posterior a la fecha inicio"
                        else -> "Formato dd-MM-yyyy"
                    },
                    onValueChange = { endDateText = it }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Alertas", color = StoneGray)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = alertable, onCheckedChange = { alertable = it })
                }

                OutlinedTextField(
                    value = percentAlertStr,
                    onValueChange = {
                        percentAlertStr = it.filter { c -> c.isDigit() }.take(3)
                    },
                    label = { Text("Porcentaje alerta") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    supportingText = {
                        Text("Entre 0 y 100")
                    },
                    isError = percentAlertStr.isNotBlank() && (parsedPercent == null || parsedPercent !in 0..100),
                    singleLine = true
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
                        start = parsedStart!!,
                        end = parsedEnd!!,
                        percentAlert = parsedPercent
                    )
                    viewModel.createBudget(request, userId)
                },
                enabled = canCreate && actionState !is BudgetActionState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
            ) {
                if (actionState is BudgetActionState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Crear")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = StoneGray)
            }
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

    var endDateText by remember {
        mutableStateOf(budget.end?.toLocalDate()?.toString().orEmpty())
    }

    val parsedEnd = endDateText.toLocalDateOrNull()

    val actionState = viewModel.actionState

    LaunchedEffect(actionState) {
        if (actionState is BudgetActionState.Success) {
            viewModel.resetActionState()
            onDismiss()
        }
    }

    val parsedLimit = limitAmount.toBigDecimalOrNull()
    val parsedPercent = percentAlertStr.toIntOrNull()

    val canUpdate =
        description.isNotBlank() &&
                parsedLimit != null &&
                parsedLimit.signum() > 0 &&
                parsedEnd != null &&
                !parsedEnd.isBefore(LocalDate.now().plusDays(1)) &&
                (!alertable || (parsedPercent != null && parsedPercent in 0..100))

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
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    supportingText = {
                        Text("Describe la meta presupuestaria")
                    }
                )

                OutlinedTextField(
                    value = limitAmount,
                    onValueChange = { limitAmount = it.replace(',', '.') },
                    label = { Text("Monto límite (S/)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    supportingText = {
                        Text("Usa hasta dos decimales")
                    },
                    isError = limitAmount.isNotBlank() && (parsedLimit == null || parsedLimit.signum() <= 0),
                    singleLine = true
                )

                ScheduleDateField(
                    label = "Fecha límite",
                    value = endDateText,
                    enabled = true,
                    isError = parsedEnd != null &&
                            parsedEnd.isBefore(LocalDate.now().plusDays(1)),
                    modifier = Modifier.fillMaxWidth(),
                    minSelectableDate = LocalDate.now().plusDays(1),
                    supportingText = if (parsedEnd != null && parsedEnd.isBefore(LocalDate.now().plusDays(1))) {
                        "La fecha límite debe ser posterior a hoy"
                    } else {
                        "Formato dd-MM-yyyy"
                    },
                    onValueChange = { endDateText = it }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Alertas", color = StoneGray)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = alertable, onCheckedChange = { alertable = it })
                }

                OutlinedTextField(
                    value = percentAlertStr,
                    onValueChange = {
                        percentAlertStr = it.filter { c -> c.isDigit() }.take(3)
                    },
                    label = { Text("Porcentaje alerta") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    supportingText = {
                        Text("Entre 0 y 100")
                    },
                    isError = percentAlertStr.isNotBlank() && (parsedPercent == null || parsedPercent !in 0..100),
                    singleLine = true
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
                        end = parsedEnd!!,
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
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = StoneGray)
            }
        }
    )
}

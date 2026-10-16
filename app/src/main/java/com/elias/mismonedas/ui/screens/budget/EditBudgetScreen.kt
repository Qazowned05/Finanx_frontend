package com.elias.mismonedas.ui.screens.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.screens.schedule.ScheduleDateField
import com.elias.mismonedas.ui.navigation.LocalSnackbarHostState
import com.elias.mismonedas.ui.screens.schedule.toLocalDateOrNull
import com.elias.mismonedas.ui.util.toCurrency
import com.elias.mismonedas.ui.util.toReadableDate
import com.elias.mismonedas.ui.viewmodel.BudgetActionState
import com.elias.mismonedas.ui.viewmodel.BudgetViewModel
import com.elias.mismonedas.ui.viewmodel.CategoryState
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetRequest
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBudgetScreen(
    userId: Long,
    budget: BudgetExecutionResponse,
    categoryState: CategoryState,
    viewModel: BudgetViewModel,
    onCancel: () -> Unit
) {
    var description by remember { mutableStateOf(budget.description.orEmpty()) }
    var limitAmount by remember { mutableStateOf(budget.limitAmount?.toPlainString() ?: "") }
    var alertable by remember { mutableStateOf(budget.alertable ?: true) }
    var percentAlert by remember { mutableFloatStateOf((budget.percentAlert ?: 80).toFloat()) }

    var endDateText by remember { mutableStateOf(budget.end?.toLocalDate()?.toString().orEmpty()) }

    val parsedEnd = endDateText.toLocalDateOrNull()
    val budgetStart = budget.start?.toLocalDate()

    val actionState = viewModel.actionState
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(actionState) {
        when (actionState) {
            is BudgetActionState.Success -> {
                viewModel.resetActionState()
                onCancel()
            }
            is BudgetActionState.Error -> snackbarHostState.showSnackbar(actionState.message)
            else -> {}
        }
    }

    val parsedLimit = limitAmount.toBigDecimalOrNull()
    val parsedPercent = percentAlert.toInt()

    val canUpdate =
        description.isNotBlank() &&
                parsedLimit != null &&
                parsedLimit.signum() > 0 &&
                parsedEnd != null &&
                parsedEnd.isAfter(LocalDate.now()) &&
                parsedEnd.isAfter(budgetStart ?: LocalDate.now()) &&
                (!alertable || (parsedPercent in 0..100))

    val categoryName = if (categoryState is CategoryState.Success) {
        categoryState.categories
            .firstOrNull { it.id == budget.categoryId }
            ?.name
    } else null

    val focusManager = LocalFocusManager.current

    Scaffold(
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        supportingText = { Text("Describe la meta presupuestaria") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                        )
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
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                        ),
                        supportingText = { Text("Usa hasta dos decimales") },
                        isError = limitAmount.isNotBlank() && (parsedLimit == null || parsedLimit.signum() <= 0),
                        singleLine = true
                    )

                    ScheduleDateField(
                        label = "Fecha límite",
                        value = endDateText,
                        enabled = true,
                        isError = parsedEnd != null && (!parsedEnd.isAfter(LocalDate.now()) ||
                                (budgetStart != null && !parsedEnd.isAfter(budgetStart))),
                        modifier = Modifier.fillMaxWidth(),
                        minSelectableDate = LocalDate.now().plusDays(1),
                        supportingText = when {
                            parsedEnd != null && !parsedEnd.isAfter(LocalDate.now()) ->
                                "La fecha límite debe ser posterior a hoy"
                            parsedEnd != null && budgetStart != null && !parsedEnd.isAfter(budgetStart) ->
                                "La fecha límite debe ser posterior a la fecha de inicio"
                            else -> "Formato dd-MM-yyyy"
                        },
                        onValueChange = { endDateText = it }
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Alertas", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(checked = alertable, onCheckedChange = { alertable = it })
                    }

                    if (alertable) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Porcentaje alerta: ${percentAlert.toInt()}%")
                            Slider(
                                value = percentAlert,
                                onValueChange = { percentAlert = it },
                                valueRange = 50f..100f,
                                steps = 9
                            )
                        }
                    }
                }

                if (canUpdate) {
                    BudgetEditPreviewCard(
                        description = description,
                        categoryName = categoryName,
                        limitAmount = parsedLimit,
                        startDate = budgetStart,
                        endDate = parsedEnd,
                        alertable = alertable,
                        percentAlert = parsedPercent,
                        currencyCode = budget.currencyCode ?: "PEN"
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = { onCancel() }, modifier = Modifier.weight(1f)) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            val request = BudgetRequest(
                                alertable = alertable,
                                limitAmount = parsedLimit ?: BigDecimal.ZERO,
                                description = description,
                                userId = userId,
                                categoryId = budget.categoryId ?: 0L,
                                start = budgetStart ?: LocalDate.now(),
                                end = parsedEnd!!,
                                percentAlert = parsedPercent
                            )
                            viewModel.updateBudget(budget.id ?: 0L, request, userId)
                        },
                        enabled = canUpdate && actionState !is BudgetActionState.Loading,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (actionState is BudgetActionState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun BudgetEditPreviewCard(
    description: String,
    categoryName: String?,
    limitAmount: BigDecimal?,
    startDate: LocalDate?,
    endDate: LocalDate?,
    alertable: Boolean,
    percentAlert: Int,
    currencyCode: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Resumen de cambios",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        description,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        categoryName ?: "Sin categoría",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Límite: ${(limitAmount ?: BigDecimal.ZERO).toCurrency(currencyCode)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium
            )
            if (startDate != null && endDate != null) {
                Text(
                    "Vigencia: ${startDate.toReadableDate()} → ${endDate.toReadableDate()}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                if (alertable) "Alertas activas al $percentAlert%" else "Alertas desactivadas",
                color = if (alertable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

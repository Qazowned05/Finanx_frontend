package com.example.finanx.ui.screens.budget

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
import com.example.finanx.data.model.TransactionType
import com.example.finanx.ui.components.CategoryDropdown
import com.example.finanx.ui.components.toCategoryOption
import com.example.finanx.ui.screens.schedule.ScheduleDateField
import com.example.finanx.ui.screens.schedule.toLocalDateOrNull
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.util.toReadableDate
import com.example.finanx.ui.viewmodel.BudgetActionState
import com.example.finanx.ui.viewmodel.BudgetViewModel
import com.example.finanx.ui.viewmodel.CategoryState
import org.openapitools.client.models.BudgetRequest
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBudgetScreen(
    userId: Long,
    categoryState: CategoryState,
    viewModel: BudgetViewModel,
    onCancel: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var limitAmount by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var alertable by remember { mutableStateOf(true) }
    var percentAlert by remember { mutableFloatStateOf(80f) }

    var startDateText by remember { mutableStateOf("") }
    var endDateText by remember { mutableStateOf("") }

    val parsedStart = startDateText.toLocalDateOrNull()
    val parsedEnd = endDateText.toLocalDateOrNull()

    val actionState = viewModel.actionState
    val snackbarHostState = remember { SnackbarHostState() }

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

    val expenseCategories = if (categoryState is CategoryState.Success) {
        categoryState.categories
            .mapNotNull { it.toCategoryOption() }
            .filter { it.type == TransactionType.SPENT }
    } else emptyList()

    val selectedCategoryName = if (categoryState is CategoryState.Success) {
        categoryState.categories
            .firstOrNull { it.id == selectedCategoryId }
            ?.name
    } else null

    val canCreate =
        description.isNotBlank() &&
                parsedLimit != null &&
                parsedLimit.signum() > 0 &&
                selectedCategoryId != null &&
                parsedStart != null &&
                parsedEnd != null &&
                !parsedStart.isBefore(LocalDate.now()) &&
                !parsedEnd.isBefore(parsedStart) &&
                (!alertable || (parsedPercent in 0..100))

    val focusManager = LocalFocusManager.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                        supportingText = { Text("Resume el objetivo del presupuesto") },
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

                    if (categoryState is CategoryState.Success) {
                        CategoryDropdown(
                            categories = expenseCategories,
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
                        isError = parsedEnd != null && parsedStart != null && parsedEnd.isBefore(parsedStart),
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

                if (canCreate) {
                    BudgetPreviewCard(
                        description = description,
                        categoryName = selectedCategoryName,
                        limitAmount = parsedLimit,
                        startDate = parsedStart,
                        endDate = parsedEnd,
                        alertable = alertable,
                        percentAlert = parsedPercent
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
                                categoryId = selectedCategoryId ?: 0L,
                                start = parsedStart!!,
                                end = parsedEnd!!,
                                percentAlert = parsedPercent
                            )
                            viewModel.createBudget(request, userId)
                        },
                        enabled = canCreate && actionState !is BudgetActionState.Loading,
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
                            Text("Crear")
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun BudgetPreviewCard(
    description: String,
    categoryName: String?,
    limitAmount: BigDecimal?,
    startDate: LocalDate?,
    endDate: LocalDate?,
    alertable: Boolean,
    percentAlert: Int
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
                "Vista previa",
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
                "Límite: ${limitAmount?.toPen() ?: "S/0"}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium
            )
            if (startDate != null && endDate != null) {
                Text(
                    "Duración: ${startDate.toReadableDate()} → ${endDate.toReadableDate()}",
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

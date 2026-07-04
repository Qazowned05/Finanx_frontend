package com.example.finanx.ui.screens.budget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
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
    var percentAlert by remember { mutableStateOf(80f) }

    var startDateText by remember { mutableStateOf("") }
    var endDateText by remember { mutableStateOf("") }

    val parsedStart = startDateText.toLocalDateOrNull()
    val parsedEnd = endDateText.toLocalDateOrNull()

    val actionState = viewModel.actionState

    LaunchedEffect(actionState) {
        if (actionState is BudgetActionState.Success) {
            viewModel.resetActionState()
            onCancel()
        }
    }

    val parsedLimit = limitAmount.toBigDecimalOrNull()
    val parsedPercent = percentAlert.toInt()

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

    var showConfirmDiscard by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    BackHandler(enabled = true) {
        if (description.isNotBlank() || limitAmount.isNotBlank() || startDateText.isNotBlank() || endDateText.isNotBlank()) {
            showConfirmDiscard = true
        } else {
            onCancel()
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Nuevo presupuesto") },
                navigationIcon = null
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("Resume el objetivo del presupuesto") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = androidx.compose.ui.text.input.KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    )
                )

                OutlinedTextField(
                    value = limitAmount,
                    onValueChange = { limitAmount = it.replace(',', '.') },
                    label = { Text("Monto límite (S/)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = androidx.compose.ui.text.input.KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    ),
                    supportingText = { Text("Usa hasta dos decimales") },
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
                    Text("Alertas", color = StoneGray)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = alertable, onCheckedChange = { alertable = it })
                }

                if (alertable) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Porcentaje alerta: ${percentAlert.toInt()}%")
                        Slider(
                            value = percentAlert,
                            onValueChange = { percentAlert = it },
                            valueRange = 0f..100f,
                            steps = 19
                        )
                    }
                }

                if (actionState is BudgetActionState.Error) {
                    Text(actionState.message, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = { onCancel() }) {
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
                        colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
                    ) {
                        if (actionState is BudgetActionState.Loading) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                        } else {
                            Text("Crear")
                        }
                    }
                }
            }
        }
    )

    if (showConfirmDiscard) {
        AlertDialog(onDismissRequest = { showConfirmDiscard = false }, title = { Text("Descartar cambios?") }, text = { Text("Se perderá la información ingresada.") }, confirmButton = { TextButton(onClick = { showConfirmDiscard = false; onCancel() }) { Text("Descartar") } }, dismissButton = { TextButton(onClick = { showConfirmDiscard = false }) { Text("Seguir editando") } })
    }
}

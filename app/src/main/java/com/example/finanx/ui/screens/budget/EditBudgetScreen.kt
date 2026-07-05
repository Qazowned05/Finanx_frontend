package com.example.finanx.ui.screens.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.example.finanx.ui.components.CategoryDropdown
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

    val actionState = viewModel.actionState

    LaunchedEffect(actionState) {
        if (actionState is BudgetActionState.Success) {
            viewModel.resetActionState()
            onCancel()
        }
    }

    val parsedLimit = limitAmount.toBigDecimalOrNull()
    val parsedPercent = percentAlert.toInt()

    val canUpdate =
        description.isNotBlank() &&
                parsedLimit != null &&
                parsedLimit.signum() > 0 &&
                parsedEnd != null &&
                !parsedEnd.isBefore(LocalDate.now().plusDays(1)) &&
                (!alertable || (parsedPercent in 0..100))

    val focusManager = LocalFocusManager.current

    Scaffold(
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
                    isError = parsedEnd != null && parsedEnd.isBefore(LocalDate.now().plusDays(1)),
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
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                        } else {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    )
}

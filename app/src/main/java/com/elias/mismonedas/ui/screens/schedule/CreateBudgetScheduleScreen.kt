package com.elias.mismonedas.ui.screens.schedule


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.components.CategoryDropdown
import com.elias.mismonedas.ui.components.FinanxOutlinedTextField
import com.elias.mismonedas.ui.components.toCategoryOption
import com.elias.mismonedas.ui.viewmodel.CategoryState
import com.elias.mismonedas.ui.viewmodel.RecurringActionState
import com.elias.mismonedas.ui.viewmodel.ScheduleViewModel
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.TransactionRequest
import java.math.BigDecimal
import java.time.LocalDate


@Composable
fun CreateBudgetScheduleScreen(
    registerHasUnsaved: ((() -> Boolean) -> Unit)? = null,
    userId: Long,
    categoryState: CategoryState,
    viewModel: ScheduleViewModel,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var description by remember { mutableStateOf("") }
    var limitText by remember { mutableStateOf("") }
    var alertable by remember { mutableStateOf(true) }
    var percentAlertText by remember { mutableStateOf("80") }
    var recurrenceType by remember { mutableStateOf(RecurrenceRuleRequest.RecurrenceType.MONTHLY) }
    var selectedDayOfWeek by remember { mutableStateOf<RecurrenceRuleRequest.DayOfWeek?>(null) }
    var intervalText by remember { mutableStateOf("1") }
    var startDateText by remember { mutableStateOf(LocalDate.now().toString()) }
    var endDateText by remember { mutableStateOf(LocalDate.now().plusYears(1).toString()) }

    val actionState = viewModel.actionState

    LaunchedEffect(actionState) {
        if (actionState is RecurringActionState.Success) {
            viewModel.resetActionState()
            onSuccess()
        }
    }

    val isLoading = actionState is RecurringActionState.Loading
    val parsedLimit = limitText.replace(",", ".").toBigDecimalOrNull()
    val parsedInterval = intervalText.toIntOrNull()?.takeIf { it > 0 }
    val parsedStart = startDateText.toLocalDateOrNull()
    val parsedEnd = endDateText.toLocalDateOrNull()
    val parsedPercent = percentAlertText.toIntOrNull() ?: 0
    val datesValid = parsedStart != null && parsedEnd != null && !parsedEnd.isBefore(parsedStart)

    val categories = if (categoryState is CategoryState.Success) {
        categoryState.categories
            .mapNotNull { it.toCategoryOption() }
            .filter { it.type == TransactionRequest.Type.SPENT }
    } else emptyList()

    val canCreate = selectedCategoryId != null && description.isNotBlank() &&
            parsedLimit != null && parsedLimit.signum() > 0 &&
            parsedInterval != null && datesValid && !isLoading &&
            (!alertable || (parsedPercent in 50..100))

    // register unsaved-check callback for parent navigation
    registerHasUnsaved?.invoke {
        selectedCategoryId != null || description.isNotBlank() || limitText.isNotBlank() ||
                alertable || percentAlertText != "80" || recurrenceType != RecurrenceRuleRequest.RecurrenceType.MONTHLY || selectedDayOfWeek != null ||
                intervalText != "1" || startDateText != LocalDate.now().toString() || endDateText != LocalDate.now().plusYears(1).toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CategoryDropdown(
            categories = categories,
            selectedCategoryId = selectedCategoryId,
            enabled = !isLoading && categoryState is CategoryState.Success,
            onCategorySelected = { selectedCategoryId = it }
        )

        FinanxOutlinedTextField(
            value = description,
            onValueChange = { description = it },
            enabled = !isLoading,
            label = "Descripción",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            supportingText = "Describe el presupuesto que quieres controlar"
        )

        FinanxOutlinedTextField(
            value = limitText,
            onValueChange = { limitText = it.replace(",", ".") },
            enabled = !isLoading,
            label = "Límite (S/)",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            supportingText = when {
                limitText.isBlank() -> "Ingresa un monto"
                parsedLimit == null -> "Ingresa un monto válido"
                else -> "Puedes ingresar hasta dos decimales"
            },
            isError = limitText.isNotBlank() && (parsedLimit == null || parsedLimit.signum() <= 0)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Alertas", style = MaterialTheme.typography.bodyMedium)
            Switch(checked = alertable, onCheckedChange = { alertable = it }, enabled = !isLoading)
        }

        if (alertable) {
            val percentAlert = percentAlertText.toIntOrNull() ?: 80
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Porcentaje de alerta: $percentAlert%")
                Slider(
                    value = percentAlert.toFloat(),
                    onValueChange = { v -> percentAlertText = v.toInt().toString() },
                    valueRange = 50f..100f,
                    steps = 9
                )
            }
        }

        RecurrenceSection(
            recurrenceType = recurrenceType,
            intervalText = intervalText,
            startDateText = startDateText,
            endDateText = endDateText,
            enabled = !isLoading,
            selectedDayOfWeek = selectedDayOfWeek,
            onRecurrenceTypeChange = { recurrenceType = it; if (it != RecurrenceRuleRequest.RecurrenceType.WEEKLY) selectedDayOfWeek = null },
            onIntervalChange = { intervalText = it },
            onStartDateChange = { startDateText = it },
            onEndDateChange = { endDateText = it },
            onDayOfWeekChange = { selectedDayOfWeek = it }
        )

        if (actionState is RecurringActionState.Error) {
            Text(actionState.message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.createBudgetSchedule(
                    userId = userId,
                    categoryId = selectedCategoryId ?: return@Button,
                    alertable = alertable,
                    percentAlert = parsedPercent,
                    limitAmount = parsedLimit ?: BigDecimal.ZERO,
                    description = description.trim(),
                    recurrenceType = recurrenceType,
                    interval = parsedInterval ?: 1,
                    start = parsedStart ?: LocalDate.now(),
                    end = parsedEnd ?: LocalDate.now().plusYears(1),
                    dayOfWeek = selectedDayOfWeek
                )
            },
            enabled = canCreate,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Programar presupuesto")
            }
        }
    }

}

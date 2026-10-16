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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.data.model.SelectOption
import org.openapitools.client.models.TransactionRequest
import com.elias.mismonedas.ui.components.CategoryDropdown
import com.elias.mismonedas.ui.components.FinanxOutlinedTextField
import com.elias.mismonedas.ui.components.toCategoryOption
import com.elias.mismonedas.ui.components.ReasonAutocomplete

import com.elias.mismonedas.ui.screens.transaction.create.PaymentMethodDropdown
import com.elias.mismonedas.ui.viewmodel.CategoryState
import com.elias.mismonedas.ui.viewmodel.RecurringActionState
import com.elias.mismonedas.ui.viewmodel.ScheduleViewModel
import org.openapitools.client.models.DayOfWeek
import org.openapitools.client.models.PaymentMethod
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.RecurrenceType
import org.openapitools.client.models.TransactionScheduleRequest
import org.openapitools.client.models.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun CreateTransactionScheduleScreen(
    registerHasUnsaved: ((() -> Boolean) -> Unit)? = null,
    userId: Long,
    categoryState: CategoryState,
    reasons: List<SelectOption>,
    viewModel: ScheduleViewModel,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var transactionType by remember { mutableStateOf(TransactionType.SPENT) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var reasonId by remember { mutableStateOf<Long?>(null) }
    var reasonText by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.CASH) }
    var recurrenceType by remember { mutableStateOf(RecurrenceType.MONTHLY) }
    var selectedDayOfWeek by remember { mutableStateOf<DayOfWeek?>(null) }
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
    val parsedAmount = amountText.replace(",", ".").toBigDecimalOrNull()
    val parsedInterval = intervalText.toIntOrNull()?.takeIf { it > 0 }
    val parsedStart = startDateText.toLocalDateOrNull()
    val parsedEnd = endDateText.toLocalDateOrNull()
    val datesValid = parsedStart != null && parsedEnd != null && !parsedEnd.isBefore(parsedStart)
    val hasDescription = reasonId != null || reasonText.isNotBlank()

    val filteredCategories = if (categoryState is CategoryState.Success) {
        categoryState.categories.mapNotNull { it.toCategoryOption() }.filter { cat ->
            if (transactionType == TransactionType.SPENT)
                cat.type == TransactionType.SPENT
            else
                cat.type == TransactionType.INCOME
        }
    } else emptyList()

    val canCreate = selectedCategoryId != null && parsedAmount != null && parsedAmount.signum() > 0 &&
            parsedInterval != null && datesValid && hasDescription && !isLoading

    // register unsaved-check callback for parent navigation
    registerHasUnsaved?.invoke {
        selectedCategoryId != null || reasonId != null || reasonText.isNotBlank() || amountText.isNotBlank() ||
                paymentMethod != PaymentMethod.CASH || recurrenceType != RecurrenceType.MONTHLY || selectedDayOfWeek != null ||
                intervalText != "1" || startDateText != LocalDate.now().toString() || endDateText != LocalDate.now().plusYears(1).toString() ||
                transactionType != TransactionType.SPENT
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = transactionType == TransactionType.SPENT,
                onClick = {
                    transactionType = TransactionType.SPENT
                    selectedCategoryId = null
                },
                label = { Text("Gasto") }
            )
            FilterChip(
                selected = transactionType == TransactionType.INCOME,
                onClick = {
                    transactionType = TransactionType.INCOME
                    selectedCategoryId = null
                },
                label = { Text("Ingreso") }
            )
        }

        CategoryDropdown(
            categories = filteredCategories,
            selectedCategoryId = selectedCategoryId,
            enabled = !isLoading && categoryState is CategoryState.Success,
            onCategorySelected = { selectedCategoryId = it }
        )

        ReasonAutocomplete(
            text = reasonText,
            reasons = reasons,
            enabled = !isLoading,
            onReasonChange = { rid, newReason ->
                reasonId = rid
                reasonText = newReason
            }
        )

        FinanxOutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it.replace(",", ".") },
            enabled = !isLoading,
            label = "Monto (S/)",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            supportingText = when {
                amountText.isBlank() -> "Ingresa un monto"
                parsedAmount == null -> "Ingresa un monto válido"
                else -> "Puedes ingresar hasta dos decimales"
            },
            isError = amountText.isNotBlank() && (parsedAmount == null || parsedAmount.signum() <= 0)
        )

        PaymentMethodDropdown(
            selected = when (paymentMethod) {
                PaymentMethod.CASH -> PaymentMethod.CASH
                PaymentMethod.CREDIT_CARD -> PaymentMethod.CREDIT_CARD
                PaymentMethod.DEBIT_CARD -> PaymentMethod.DEBIT_CARD
                PaymentMethod.BANK_TRANSFER -> PaymentMethod.BANK_TRANSFER
                PaymentMethod.YAPE -> PaymentMethod.YAPE
                PaymentMethod.PLIN -> PaymentMethod.PLIN
                PaymentMethod.OTHER -> PaymentMethod.OTHER
            },
            enabled = !isLoading,
            onSelected = { pm ->
                paymentMethod = PaymentMethod.valueOf(pm.name)
            }
        )

        RecurrenceSection(
            recurrenceType = recurrenceType,
            intervalText = intervalText,
            startDateText = startDateText,
            endDateText = endDateText,
            enabled = !isLoading,
            selectedDayOfWeek = selectedDayOfWeek,
            onRecurrenceTypeChange = { recurrenceType = it; if (it != RecurrenceType.WEEKLY) selectedDayOfWeek = null },
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
                viewModel.createTransactionSchedule(
                    userId = userId,
                    categoryId = selectedCategoryId ?: return@Button,
                    reasonId = reasonId,
                    description = if (reasonId == null) reasonText.trim().ifBlank { null } else null,
                    amount = parsedAmount ?: BigDecimal.ZERO,
                    paymentMethod = paymentMethod,
                    transactionType = transactionType,
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
                Text("Programar movimiento")
            }
        }
    }

}

package com.example.finanx.ui.screens.transaction.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.PaymentMethod
import com.example.finanx.data.model.SelectOption
import com.example.finanx.data.model.TransactionType
import com.example.finanx.ui.components.ReasonAutocomplete
import com.example.finanx.ui.components.tutorialTarget
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.viewmodel.TransactionDraftField
import com.example.finanx.ui.viewmodel.TransactionDraftState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionDetailsStep(
    draft: TransactionDraftState,
    categoryLabel: String?,
    reasons: List<SelectOption>,
    hasSavingGoalStep: Boolean,
    isSaving: Boolean,
    saveError: String?,
    onAmountChange: (String) -> Unit,
    onReasonChange: (Long?, String) -> Unit,
    onPaymentChange: (PaymentMethod) -> Unit,
    onSubmit: () -> Unit,
    registry: MutableMap<String, Rect>,
    modifier: Modifier = Modifier
) {
    val amountFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var attemptedSubmit by remember { mutableStateOf(false) }

    val parsedAmount = draft.amountText.replace(",", ".").toBigDecimalOrNull()
    val amountError = (attemptedSubmit && (parsedAmount == null || parsedAmount.signum() <= 0)) ||
        draft.fieldErrors.contains(TransactionDraftField.AMOUNT)
    val paymentError = (attemptedSubmit && draft.paymentMethod == null) ||
        draft.fieldErrors.contains(TransactionDraftField.PAYMENT_METHOD)

    LaunchedEffect(Unit) {
        amountFocus.requestFocus()
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DetailsSummaryCard(
            type = draft.type,
            categoryLabel = categoryLabel,
            amountText = draft.amountText
        )

        OutlinedTextField(
            value = draft.amountText,
            onValueChange = onAmountChange,
            label = { Text("Monto") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            enabled = !isSaving,
            isError = amountError,
            supportingText = {
                Text(
                    when {
                        amountError && draft.amountText.isBlank() -> "Ingresa un monto"
                        amountError -> "Ingresa un monto válido"
                        else -> "Puedes ingresar hasta dos decimales"
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(amountFocus)
                .tutorialTarget("amount_field", registry)
        )

        ReasonAutocomplete(
            text = draft.reasonText,
            reasons = reasons,
            enabled = !isSaving,
            onReasonChange = onReasonChange,
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .tutorialTarget("reason_field", registry)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .tutorialTarget("payment_method_row", registry),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Método de pago")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PaymentMethod.entries.forEach { method ->
                    FilterChip(
                        selected = draft.paymentMethod == method,
                        onClick = { if (!isSaving) onPaymentChange(method) },
                        label = { Text(method.toSpanishLabel()) }
                    )
                }
            }
            if (paymentError) {
                Text(
                    text = "Selecciona un método de pago",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (saveError != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = saveError,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Button(
            onClick = {
                attemptedSubmit = true
                if (!amountError && !paymentError) {
                    onSubmit()
                }
            },
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .tutorialTarget("save_button", registry)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (hasSavingGoalStep) "Siguiente" else "Guardar")
            }
        }
    }
}

@Composable
private fun DetailsSummaryCard(
    type: TransactionType?,
    categoryLabel: String?,
    amountText: String
) {
    val isIncome = type == TransactionType.INCOME
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = if (isIncome) "Ingreso" else "Gasto",
                color = TextGray
            )
            Text(
                text = categoryLabel ?: "Sin categoría",
                style = MaterialTheme.typography.titleMedium
            )
            if (amountText.isNotBlank()) {
                Text(
                    text = "S/ $amountText",
                    color = if (isIncome) IncomeGreen else ExpenseRed,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

private fun PaymentMethod.toSpanishLabel(): String {
    return when (this) {
        PaymentMethod.CASH -> "Efectivo"
        PaymentMethod.CREDIT_CARD -> "Tarjeta crédito"
        PaymentMethod.DEBIT_CARD -> "Tarjeta débito"
        PaymentMethod.BANK_TRANSFER -> "Transferencia"
        PaymentMethod.YAPE -> "Yape"
        PaymentMethod.PLIN -> "Plin"
        PaymentMethod.OTHER -> "Otro"
    }
}

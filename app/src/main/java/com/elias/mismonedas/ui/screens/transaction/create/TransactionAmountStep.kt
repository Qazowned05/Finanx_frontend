package com.elias.mismonedas.ui.screens.transaction.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.data.preferences.TransactionQuickPrefs
import com.elias.mismonedas.ui.theme.TextGray

private val moneyInputRegex =
    Regex("""^\d{0,9}([.,]\d{0,2})?$""")
//todo verificar uso
@Composable
fun TransactionAmountStep(
    form: CreateTransactionFormState,
    onFormChange:
        (CreateTransactionFormState) -> Unit,
    onContinue: () -> Unit
) {
    val invalidAmount =
        form.amountText.isNotBlank() &&
                !form.isFirstStepValid
    val amountFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        amountFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "¿Qué deseas registrar?",
            style =
                MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Selecciona el tipo de movimiento e ingresa el monto.",
            color = TextGray
        )

        TransactionTypeSelector(
            selected = form.type,
            onSelected = { selectedType ->
                val preferredCategoryId = TransactionQuickPrefs.lastCategoryId(selectedType)
                onFormChange(
                    form.copy(
                        type = selectedType,
                        categoryId = preferredCategoryId
                    )
                )
            }
        )

        OutlinedTextField(
            value = form.amountText,
            onValueChange = { newValue ->
                if (
                    newValue.matches(
                        moneyInputRegex
                    )
                ) {
                    onFormChange(
                        form.copy(
                            amountText = newValue
                        )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(amountFocusRequester),
            label = {
                Text("Monto")
            },
            placeholder = {
                Text("0.00")
            },
            prefix = {
                Text("S/ ")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { onContinue() }
            ),
            singleLine = true,
            isError = invalidAmount,
            supportingText = {
                Text(
                    if (invalidAmount) {
                        "Ingresa un monto mayor que cero"
                    } else {
                        "Puedes ingresar hasta dos decimales"
                    }
                )
            }
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onContinue,
            enabled = form.isFirstStepValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Continuar",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

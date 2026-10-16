package com.elias.mismonedas.ui.screens.transaction.create

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.elias.mismonedas.ui.components.FinanxOutlinedTextField
import org.openapitools.client.models.PaymentMethod
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.RecurrenceType
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTypeSelector(
    selected: TransactionType,
    onSelected: (TransactionType) -> Unit
) {
    val options = listOf(
        TransactionType.SPENT to "Gasto",
        TransactionType.INCOME to "Ingreso"
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { _, option ->
            val isSelected = selected == option.first
            Button(
                onClick = { onSelected(option.first) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                ),
                modifier = with(this@Row) { Modifier.weight(1f) }
            ) {
                Text(option.second)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormDropdown(
    label: String,
    placeholder: String,
    items: List<T>,
    selectedItem: T?,
    itemLabel: (T) -> String,
    enabled: Boolean,
    onItemSelected: (T?) -> Unit,
    allowEmptySelection: Boolean = false,
    emptySelectionLabel: String = "",
    supportingText: String? = null,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (enabled) {
                    expanded = !expanded
                }
            },
        modifier = modifier.fillMaxWidth()
    ) {
        FinanxOutlinedTextField(
            value = selectedItem
                ?.let(itemLabel)
                ?: if (allowEmptySelection) {
                    emptySelectionLabel
                } else {
                    ""
                },
            onValueChange = {},
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            enabled = enabled,
            label = label,
            supportingText = supportingText,
            isError = isError,
            placeholder = placeholder,
            trailingIcon = {
                ExposedDropdownMenuDefaults
                    .TrailingIcon(
                        expanded = expanded
                    )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            if (allowEmptySelection) {
                DropdownMenuItem(
                    text = {
                        Text(emptySelectionLabel)
                    },
                    onClick = {
                        onItemSelected(null)
                        expanded = false
                    }
                )
            }

            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(itemLabel(item))
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PaymentMethodDropdown(
    selected: PaymentMethod,
    enabled: Boolean,
    onSelected: (PaymentMethod) -> Unit
) {
    FormDropdown(
        label = "Método de pago",
        placeholder = "Selecciona un método",
        items = PaymentMethod.entries,
        selectedItem = selected,
        itemLabel = {
            it.toSpanishLabel()
        },
        enabled = enabled,
        onItemSelected = {
            it?.let(onSelected)
        }
    )
}

@Composable
fun RecurrenceTypeDropdown(
    selected: RecurrenceType,
    enabled: Boolean,
    onSelected: (RecurrenceType) -> Unit,
    modifier: Modifier = Modifier
) {
    FormDropdown(
        label = "Frecuencia",
        placeholder = "Selecciona una frecuencia",
        items = RecurrenceType.entries,
        selectedItem = selected,
        itemLabel = {
            it.toSpanishLabel()
        },
        enabled = enabled,
        onItemSelected = {
            it?.let(onSelected)
        },
        modifier = modifier
    )
}

//todo verificar si debe ir aqui o si ya existe/duplicacion
fun PaymentMethod.toSpanishLabel(): String = when (this) {
    PaymentMethod.CASH -> "Efectivo"
    PaymentMethod.CREDIT_CARD -> "Tarjeta de crédito"
    PaymentMethod.DEBIT_CARD -> "Tarjeta de débito"
    PaymentMethod.BANK_TRANSFER -> "Transferencia bancaria"
    PaymentMethod.YAPE -> "Yape"
    PaymentMethod.PLIN -> "Plin"
    PaymentMethod.OTHER -> "Otro"
}

private fun RecurrenceType.toSpanishLabel(): String = when (this) {
    RecurrenceType.DAILY -> "Diaria"
    RecurrenceType.WEEKLY -> "Semanal"
    RecurrenceType.MONTHLY -> "Mensual"
    RecurrenceType.YEARLY -> "Anual"
    RecurrenceType.NONE -> "Sin recurrencia"
}

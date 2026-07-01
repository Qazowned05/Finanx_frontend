package com.example.finanx.ui.screens.transaction.create

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.finanx.data.model.TransactionType
import com.example.finanx.data.model.PaymentMethod
import com.example.finanx.data.model.TransactionRecurrenceType

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

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed {
                index,
                option ->

            SegmentedButton(
                selected = selected == option.first,
                onClick = {
                    onSelected(option.first)
                },
                shape =
                    SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                label = {
                    Text(option.second)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReasonModeSelector(
    useNewReason: Boolean,
    enabled: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        SegmentedButton(
            selected = !useNewReason,
            onClick = {
                onModeChange(false)
            },
            enabled = enabled,
            shape =
                SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2
                ),
            label = {
                Text("Usar existente")
            }
        )

        SegmentedButton(
            selected = useNewReason,
            onClick = {
                onModeChange(true)
            },
            enabled = enabled,
            shape =
                SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2
                ),
            label = {
                Text("Crear nuevo")
            }
        )
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
    emptySelectionLabel: String = ""
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
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedItem
                ?.let(itemLabel)
                ?: if (allowEmptySelection) {
                    emptySelectionLabel
                } else {
                    ""
                },
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            enabled = enabled,
            label = {
                Text(label)
            },
            placeholder = {
                Text(placeholder)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults
                    .TrailingIcon(
                        expanded = expanded
                    )
            }
        )

        /*
         * Se utiliza DropdownMenu y no el import
         * ExposedDropdownMenu, porque tu versión del
         * proyecto no reconoce ese import.
         */
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
    selected: TransactionRecurrenceType,
    enabled: Boolean,
    onSelected: (TransactionRecurrenceType) -> Unit
) {
    FormDropdown(
        label = "Frecuencia",
        placeholder = "Selecciona una frecuencia",
        items = TransactionRecurrenceType.entries,
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

private fun PaymentMethod.toSpanishLabel():
        String {
    return when (this) {
        PaymentMethod.CASH ->
            "Efectivo"

        PaymentMethod.CREDIT_CARD ->
            "Tarjeta de crédito"

        PaymentMethod.DEBIT_CARD ->
            "Tarjeta de débito"

        PaymentMethod.BANK_TRANSFER ->
            "Transferencia bancaria"

        PaymentMethod.YAPE ->
            "Yape"

        PaymentMethod.PLIN ->
            "Plin"

        PaymentMethod.OTHER ->
            "Otro"
    }
}

private fun TransactionRecurrenceType.toSpanishLabel():
        String {
    return when (this) {
        TransactionRecurrenceType.DAILY ->
            "Diaria"

        TransactionRecurrenceType.WEEKLY ->
            "Semanal"

        TransactionRecurrenceType.MONTHLY ->
            "Mensual"

        TransactionRecurrenceType.YEARLY ->
            "Anual"
    }
}

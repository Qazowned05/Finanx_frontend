package com.example.finanx.ui.screens.transaction.create

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import com.example.finanx.data.model.MovementType
import com.example.finanx.data.model.TransactionFormOptions
import com.example.finanx.data.model.TransactionRecurrenceType
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.util.toReadableDate
import com.example.finanx.ui.viewmodel.TransactionSaveState
import java.time.LocalDate
import java.util.TimeZone

@Composable
fun TransactionDetailsStep(
    paddingValues: PaddingValues,
    form: CreateTransactionFormState,
    options: TransactionFormOptions,
    isLoading: Boolean,
    loadError: String?,
    saveState: TransactionSaveState,
    allowSchedule: Boolean,
    lockScheduleMode: Boolean,
    onFormChange:
        (CreateTransactionFormState) -> Unit,
    onRetry: () -> Unit,
    onPrevious: () -> Unit,
    onSave: () -> Unit
) {
    if (
        isLoading &&
        options.categories.isEmpty()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    if (options.categories.isEmpty()) {
        EmptyCategoriesContent(
            paddingValues = paddingValues,
            message = loadError
                ?: "No tienes categorías disponibles.",
            onRetry = onRetry
        )

        return
    }

    val hasExistingReasons =
        options.reasons.isNotEmpty()

    val useNewReason =
        form.usesNewReason(hasExistingReasons)

    val selectedCategory =
        options.categories.firstOrNull {
            it.id == form.categoryId
        }

    val selectedReason =
        options.reasons.firstOrNull {
            it.id == form.reasonId
        }

    val selectedGoal =
        options.savingGoals.firstOrNull {
            it.id == form.savingGoalId
        }

    val isSaving =
        saveState is TransactionSaveState.Saving

    val saveError =
        (saveState as? TransactionSaveState.Error)
            ?.message

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        TransactionSummaryCard(
            form = form,
            onEdit = onPrevious,
            enabled = !isSaving
        )

        FormDropdown(
            label = "Categoría",
            placeholder = "Selecciona una categoría",
            items = options.categories,
            selectedItem = selectedCategory,
            itemLabel = { it.label },
            enabled = !isSaving,
            onItemSelected = { option ->
                onFormChange(
                    form.copy(
                        categoryId = option?.id
                    )
                )
            }
        )

        Text(
            text = "La categoría debe corresponder al tipo de movimiento.",
            style =
                MaterialTheme.typography.labelSmall,
            color = TextGray
        )

        if (hasExistingReasons) {
            ReasonModeSelector(
                useNewReason = useNewReason,
                enabled = !isSaving,
                onModeChange = { createNew ->
                    onFormChange(
                        form.copy(
                            useNewReason = createNew,
                            reasonId = null
                        )
                    )
                }
            )
        }

        if (useNewReason) {
            OutlinedTextField(
                value = form.newReason,
                onValueChange = {
                    onFormChange(
                        form.copy(newReason = it)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving,
                label = {
                    Text("Descripción")
                },
                placeholder = {
                    Text(
                        if (
                            form.type ==
                            MovementType.INCOME
                        ) {
                            "Ejemplo: Pago de sueldo"
                        } else {
                            "Ejemplo: Compra semanal"
                        }
                    )
                },
                singleLine = true
            )
        } else {
            FormDropdown(
                label = "Motivo",
                placeholder = "Selecciona un motivo",
                items = options.reasons,
                selectedItem = selectedReason,
                itemLabel = { it.label },
                enabled = !isSaving,
                onItemSelected = { option ->
                    onFormChange(
                        form.copy(
                            reasonId = option?.id
                        )
                    )
                }
            )
        }

        PaymentMethodDropdown(
            selected = form.paymentMethod,
            enabled = !isSaving,
            onSelected = {
                onFormChange(
                    form.copy(
                        paymentMethod = it
                    )
                )
            }
        )

        if (allowSchedule) {
            ScheduleSection(
                form = form,
                enabled = !isSaving,
                locked = lockScheduleMode,
                onFormChange = onFormChange
            )
        }

        FormDropdown(
            label = "Meta de ahorro",
            placeholder = "Sin meta de ahorro",
            items = options.savingGoals,
            selectedItem = selectedGoal,
            itemLabel = { it.label },
            enabled = !isSaving && !form.isScheduled,
            allowEmptySelection = true,
            emptySelectionLabel =
                "Sin meta de ahorro",
            onItemSelected = { option ->
                onFormChange(
                    form.copy(
                        savingGoalId = option?.id
                    )
                )
            }
        )

        if (form.isScheduled) {
            Text(
                text = "Las metas de ahorro no se envían en movimientos programados porque el contrato actual de programación no expone ese campo.",
                style = MaterialTheme.typography.labelSmall,
                color = TextGray
            )
        }

        if (saveError != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme
                            .colorScheme
                            .errorContainer
                )
            ) {
                Text(
                    text = saveError,
                    modifier = Modifier.padding(14.dp),
                    color =
                        MaterialTheme
                            .colorScheme
                            .onErrorContainer
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onPrevious,
                enabled = !isSaving,
                modifier = Modifier.weight(1f)
            ) {
                Text("Atrás")
            }

            Button(
                onClick = onSave,
                enabled =
                    form.isValid(
                        hasExistingReasons
                    ) &&
                            !isSaving,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimary
                    )
                } else {
                    Text(
                        if (form.isScheduled) {
                            "Programar"
                        } else {
                            "Guardar"
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionSummaryCard(
    form: CreateTransactionFormState,
    onEdit: () -> Unit,
    enabled: Boolean
) {
    val isIncome =
        form.type == MovementType.INCOME

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isIncome) {
                        "Ingreso"
                    } else {
                        "Gasto"
                    },
                    color = TextGray
                )

                Text(
                    text =
                        "S/ ${form.parsedAmount?.toPlainString()}",
                    style =
                        MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isIncome) {
                        IncomeGreen
                    } else {
                        ExpenseRed
                    }
                )
            }

            OutlinedButton(
                onClick = onEdit,
                enabled = enabled
            ) {
                Text("Editar")
            }
        }
    }
}

@Composable
private fun ScheduleSection(
    form: CreateTransactionFormState,
    enabled: Boolean,
    locked: Boolean,
    onFormChange:
        (CreateTransactionFormState) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (locked) {
                            "Programación"
                        } else {
                            "Movimiento programado"
                        },
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Para gastos o ingresos fijos con recurrencia.",
                        style =
                            MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }

                if (!locked) {
                    Switch(
                        checked = form.isScheduled,
                        enabled = enabled,
                        onCheckedChange = { checked ->
                            onFormChange(
                                form.copy(
                                    isScheduled = checked,
                                    savingGoalId = if (checked) {
                                        null
                                    } else {
                                        form.savingGoalId
                                    }
                                )
                            )
                        }
                    )
                }
            }

            if (form.isScheduled) {
                RecurrenceTypeDropdown(
                    selected = form.recurrenceType,
                    enabled = enabled,
                    onSelected = { recurrence ->
                        onFormChange(
                            form.copy(
                                recurrenceType = recurrence
                            )
                        )
                    }
                )

                OutlinedTextField(
                    value = form.recurrenceIntervalText,
                    onValueChange = { value ->
                        if (
                            value.length <= 3 &&
                            value.all(Char::isDigit)
                        ) {
                            onFormChange(
                                form.copy(
                                    recurrenceIntervalText = value
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    label = {
                        Text("Cada cuántos periodos")
                    },
                    placeholder = {
                        Text("1")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    isError =
                        form.parsedRecurrenceInterval == null,
                    supportingText = {
                        Text(
                            "Ejemplo: 1 = cada ${form.recurrenceType.periodLabel()}"
                        )
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    ScheduleDateField(
                        label = "Inicio",
                        value = form.scheduleStartDateText,
                        enabled = enabled,
                        isError =
                            form.parsedScheduleStartDate == null,
                        modifier = Modifier.weight(1f),
                        onValueChange = { value ->
                            onFormChange(
                                form.copy(
                                    scheduleStartDateText = value
                                )
                            )
                        }
                    )

                    ScheduleDateField(
                        label = "Fin",
                        value = form.scheduleEndDateText,
                        enabled = enabled,
                        isError =
                            form.parsedScheduleEndDate == null ||
                                    !form.isScheduleValid,
                        modifier = Modifier.weight(1f),
                        onValueChange = { value ->
                            onFormChange(
                                form.copy(
                                    scheduleEndDateText = value
                                )
                            )
                        }
                    )
                }

                if (!form.isScheduleValid) {
                    Text(
                        text = "Revisa el intervalo y las fechas. Usa formato dd-MM-yyyy y una fecha fin igual o posterior al inicio.",
                        style =
                            MaterialTheme.typography.labelSmall,
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleDateField(
    label: String,
    value: String,
    enabled: Boolean,
    isError: Boolean,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value.toDisplayScheduleDate(),
            onValueChange = {}, // Vacío porque la entrada viene del calendario
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = true, // CRÍTICO: Evita que se abra el teclado de texto
            label = {
                Text(label)
            },
            placeholder = {
                Text("dd-MM-yyyy")
            },
            singleLine = true,
            isError = isError,
            trailingIcon = {
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        showDialog = true
                    },
                    enabled = enabled
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                        contentDescription = "Seleccionar fecha"
                    )
                }
            }
        )

        // Capa invisible encima del campo. Permite que al tocar
        // en cualquier parte (no solo el ícono) se abra el calendario.
        if (enabled) {
            Surface(
                modifier = Modifier.matchParentSize(),
                color = androidx.compose.ui.graphics.Color.Transparent,
                onClick = {
                    focusManager.clearFocus()
                    showDialog = true
                }
            ) {}
        }
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        onValueChange(sdf.format(Date(millis)))
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun String.toDisplayScheduleDate(): String =
    runCatching {
        LocalDate.parse(this).toReadableDate()
    }.getOrElse {
        this
    }

private fun TransactionRecurrenceType.periodLabel():
        String {
    return when (this) {
        TransactionRecurrenceType.DAILY ->
            "día"

        TransactionRecurrenceType.WEEKLY ->
            "semana"

        TransactionRecurrenceType.MONTHLY ->
            "mes"

        TransactionRecurrenceType.YEARLY ->
            "año"
    }
}

@Composable
private fun EmptyCategoriesContent(
    paddingValues: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(message)

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}

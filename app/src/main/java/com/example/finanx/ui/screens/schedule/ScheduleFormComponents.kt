package com.example.finanx.ui.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.TransactionRecurrenceType
import com.example.finanx.ui.screens.transaction.create.RecurrenceTypeDropdown
import com.example.finanx.ui.util.toReadableDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
internal fun RecurrenceSection(
    recurrenceType: TransactionRecurrenceType,
    intervalText: String,
    startDateText: String,
    endDateText: String,
    enabled: Boolean,
    onRecurrenceTypeChange: (TransactionRecurrenceType) -> Unit,
    onIntervalChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit
) {
    val parsedInterval = intervalText.toIntOrNull()?.takeIf { it > 0 }
    val parsedStart = startDateText.toLocalDateOrNull()
    val parsedEnd = endDateText.toLocalDateOrNull()
    val datesValid = parsedStart != null && parsedEnd != null && !parsedEnd.isBefore(parsedStart)
    val intervalError = intervalText.isNotEmpty() && parsedInterval == null
    val startError = startDateText.isNotEmpty() && parsedStart == null
    val endError = endDateText.isNotEmpty() && (!datesValid || parsedEnd == null)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Recurrencia", fontWeight = FontWeight.SemiBold)

            RecurrenceTypeDropdown(
                selected = recurrenceType,
                enabled = enabled,
                onSelected = onRecurrenceTypeChange
            )

            OutlinedTextField(
                value = intervalText,
                onValueChange = { v ->
                    if (v.length <= 3 && v.all(Char::isDigit)) onIntervalChange(v)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                label = { Text("Cada cuántos periodos") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                isError = intervalError,
                supportingText = {
                    Text(
                        if (intervalError) {
                            "Ingresa un número mayor que cero"
                        } else {
                            "Ejemplo: 1 = cada periodo"
                        }
                    )
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ScheduleDateField(
                    label = "Inicio",
                    value = startDateText,
                    enabled = enabled,
                    isError = startError,
                    supportingText = if (startError) {
                        "Ingresa una fecha válida"
                    } else {
                        "Formato dd-MM-yyyy"
                    },
                    modifier = Modifier.weight(1f),
                    minSelectableDate = LocalDate.now(),
                    onValueChange = onStartDateChange
                )
                ScheduleDateField(
                    label = "Fin",
                    value = endDateText,
                    enabled = enabled,
                    isError = endError,
                    supportingText = when {
                        endError && parsedEnd == null -> "Ingresa una fecha válida"
                        endError -> "La fecha fin debe ser igual o posterior al inicio"
                        else -> "Formato dd-MM-yyyy"
                    },
                    modifier = Modifier.weight(1f),
                    minSelectableDate = LocalDate.now().plusDays(1),
                    onValueChange = onEndDateChange
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDateField(
    label: String,
    value: String,
    enabled: Boolean,
    isError: Boolean,
    supportingText: String? = null,
    modifier: Modifier,
    minSelectableDate: LocalDate? = null,
    onValueChange: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val minMillis = remember(minSelectableDate) {
        minSelectableDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                minMillis == null || utcTimeMillis >= minMillis
        }
    )
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value.toDisplayDate(),
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = true,
            label = { Text(label) },
            supportingText = supportingText?.let { text -> { Text(text) } },
            placeholder = { Text("dd-MM-yyyy") },
            singleLine = true,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.None
            ),
            trailingIcon = {
                IconButton(
                    onClick = { focusManager.clearFocus(); showDialog = true },
                    enabled = enabled
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                }
            }
        )
        if (enabled) {
            Surface(
                modifier = Modifier.matchParentSize(),
                color = Color.Transparent,
                onClick = { focusManager.clearFocus(); showDialog = true }
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
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }
}

private fun String.toDisplayDate(): String =
    runCatching { LocalDate.parse(trim()).toReadableDate() }.getOrElse { this }

internal fun String.toLocalDateOrNull(): LocalDate? =
    runCatching { LocalDate.parse(trim()) }.getOrNull()

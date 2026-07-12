package com.elias.mismonedas.ui.screens.saving

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.components.FinanxOutlinedTextField
import com.elias.mismonedas.ui.screens.schedule.ScheduleDateField
import com.elias.mismonedas.ui.viewmodel.SavingGoalViewModel
import java.time.LocalDate

@Composable
fun CreateSavingGoalScreen(
    userId: Long,
    viewModel: SavingGoalViewModel,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var description by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var deadlineText by remember { mutableStateOf(LocalDate.now().plusMonths(6).toString()) }
    var attemptedSave by remember { mutableStateOf(false) }

    val parsedAmount = amountText.replace(",", ".").toBigDecimalOrNull()
    val parsedDeadline = deadlineText.toLocalDateOrNull()

    val isSaving = viewModel.savingGoalState is com.elias.mismonedas.ui.viewmodel.SavingGoalState.Loading
    val canCreate = description.isNotBlank() && parsedAmount != null && parsedAmount.signum() > 0 && parsedDeadline != null && !isSaving

    LaunchedEffect(viewModel.actionSuccess) {
        if (viewModel.actionSuccess != null) {
            viewModel.clearActionSuccess()
            onSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FinanxOutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = "Nombre de la meta",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            isError = attemptedSave && description.isBlank(),
            supportingText = if (attemptedSave && description.isBlank()) {
                "Ingresa un nombre para la meta"
            } else {
                null
            }
        )

        FinanxOutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = "Monto objetivo",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            isError = attemptedSave && (parsedAmount == null || parsedAmount.signum() <= 0),
            supportingText = when {
                attemptedSave && amountText.isBlank() -> "Ingresa un monto"
                attemptedSave && (parsedAmount == null || parsedAmount.signum() <= 0) -> "Ingresa un monto válido"
                else -> "Puedes ingresar hasta dos decimales"
            },
            modifier = Modifier
        )

        ScheduleDateField(
            label = "Fecha objetivo",
            value = deadlineText,
            enabled = !isSaving,
            isError = attemptedSave && parsedDeadline == null,
            supportingText = if (attemptedSave && parsedDeadline == null) {
                "Ingresa una fecha válida"
            } else {
                "La fecha debe ser futura"
            },
            modifier = Modifier.fillMaxWidth(),
            minSelectableDate = LocalDate.now().plusDays(1),
            onValueChange = { deadlineText = it }
        )

        Button(
            onClick = {
                attemptedSave = true
                val amount = parsedAmount ?: return@Button
                val deadline = parsedDeadline ?: return@Button
                if (description.isBlank() || amount.signum() <= 0) return@Button
                viewModel.createSavingGoal(userId, description.trim(), amount, deadline)
            },
            enabled = canCreate,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSaving) {
                CircularProgressIndicator()
            } else {
                Text("Crear meta")
            }
        }
    }
}

private fun String.toLocalDateOrNull(): LocalDate? =
    runCatching { LocalDate.parse(trim()) }.getOrNull()

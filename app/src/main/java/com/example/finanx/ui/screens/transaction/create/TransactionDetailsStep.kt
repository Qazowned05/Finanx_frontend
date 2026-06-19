package com.example.finanx.ui.screens.transaction.create

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.MovementType
import com.example.finanx.data.model.TransactionFormOptions
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.viewmodel.TransactionSaveState

@Composable
fun TransactionDetailsStep(
    paddingValues: PaddingValues,
    form: CreateTransactionFormState,
    options: TransactionFormOptions,
    isLoading: Boolean,
    loadError: String?,
    saveState: TransactionSaveState,
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

        FormDropdown(
            label = "Meta de ahorro",
            placeholder = "Sin meta de ahorro",
            items = options.savingGoals,
            selectedItem = selectedGoal,
            itemLabel = { it.label },
            enabled = !isSaving,
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
                    Text("Guardar")
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
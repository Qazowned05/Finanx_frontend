package com.example.finanx.ui.screens.transaction.schedule

import androidx.compose.runtime.Composable
import com.example.finanx.data.model.NewTransactionCommand
import com.example.finanx.ui.screens.transaction.create.CreateTransactionFormState
import com.example.finanx.ui.screens.transaction.create.CreateTransactionScreen
import com.example.finanx.ui.viewmodel.ScheduledTransactionUiState

@Composable
fun CreateScheduledTransactionScreen(
    userId: Long,
    state: ScheduledTransactionUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onSubmit: (NewTransactionCommand) -> Unit,
    onSaved: () -> Unit
) {
    CreateTransactionScreen(
        userId = userId,
        formOptions = state.formOptions,
        isLoading = state.isLoading,
        loadError = state.error,
        saveState = state.saveState,
        title = "Nueva programación",
        initialForm = CreateTransactionFormState(isScheduled = true),
        allowSchedule = true,
        lockScheduleMode = true,
        onBack = onBack,
        onRetry = onRetry,
        onSubmit = onSubmit,
        onSaved = onSaved
    )
}

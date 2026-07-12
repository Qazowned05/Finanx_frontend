package com.elias.mismonedas.ui.navigation.entries

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.pop
import com.elias.mismonedas.ui.navigation.popUpToOrPush
import com.elias.mismonedas.ui.navigation.push
import com.elias.mismonedas.ui.navigation.rememberUnsavedChangesBackHandler
import com.elias.mismonedas.ui.screens.schedule.CreateBudgetScheduleScreen
import com.elias.mismonedas.ui.screens.schedule.CreateTransactionScheduleScreen
import com.elias.mismonedas.ui.screens.schedule.ScheduleChoiceScreen
import com.elias.mismonedas.ui.screens.schedule.ScheduledTransactionsScreen
import com.elias.mismonedas.ui.viewmodel.CategoryState
import com.elias.mismonedas.ui.viewmodel.ScheduleFormState
import com.elias.mismonedas.ui.viewmodel.ScheduleViewModel

fun EntryProviderScope<NavKey>.scheduleEntries(
    backStack: NavBackStack<NavKey>,
    scheduleViewModel: ScheduleViewModel,
    userId: Long
) {
    entry<AppDestinations.ScheduledTransactions> {
        SetTopBar(TopBarConfig(title = "Programaciones"))

        LaunchedEffect(userId) { scheduleViewModel.fetchAll(userId) }

        ScheduledTransactionsScreen(
            viewModel = scheduleViewModel,
            userId = userId,
            onNewSchedule = { backStack.push(AppDestinations.ScheduleChoice) }
        )
    }

    entry<AppDestinations.ScheduleChoice> {
        SetTopBar(TopBarConfig(title = "Nueva programación"))

        ScheduleChoiceScreen(
            onChooseTransaction = { backStack.push(AppDestinations.CreateTransactionSchedule) },
            onChooseBudget = { backStack.push(AppDestinations.CreateBudgetSchedule) }
        )
    }

    entry<AppDestinations.CreateTransactionSchedule> {
        var hasUnsavedChanges by remember { mutableStateOf<(() -> Boolean)?>(null) }

        LaunchedEffect(userId) { scheduleViewModel.loadFormOptions(userId) }

        val backHandler = rememberUnsavedChangesBackHandler(
            hasUnsavedChanges = { hasUnsavedChanges?.invoke() == true },
            onLeave = { backStack.pop() }
        )
        SetTopBar(TopBarConfig(title = "Programar movimiento", onBack = backHandler.requestBack))

        val formState = scheduleViewModel.formState
        val categories = if (formState is ScheduleFormState.Ready)
            CategoryState.Success(formState.options.categories) else CategoryState.Loading
        val reasons = if (formState is ScheduleFormState.Ready) formState.options.reasons else emptyList()

        CreateTransactionScheduleScreen(
            registerHasUnsaved = { fn -> hasUnsavedChanges = fn },
            userId = userId,
            categoryState = categories,
            reasons = reasons,
            viewModel = scheduleViewModel,
            onSuccess = { backStack.popUpToOrPush(AppDestinations.ScheduledTransactions) },
            onBack = backHandler.requestBack
        )
    }

    entry<AppDestinations.CreateBudgetSchedule> {
        var hasUnsavedChanges by remember { mutableStateOf<(() -> Boolean)?>(null) }

        LaunchedEffect(userId) { scheduleViewModel.loadFormOptions(userId) }

        val backHandler = rememberUnsavedChangesBackHandler(
            hasUnsavedChanges = { hasUnsavedChanges?.invoke() == true },
            onLeave = { backStack.pop() }
        )
        SetTopBar(TopBarConfig(title = "Programar presupuesto", onBack = backHandler.requestBack))

        val formState = scheduleViewModel.formState
        val categories = if (formState is ScheduleFormState.Ready)
            CategoryState.Success(formState.options.categories) else CategoryState.Loading

        CreateBudgetScheduleScreen(
            registerHasUnsaved = { fn -> hasUnsavedChanges = fn },
            userId = userId,
            categoryState = categories,
            viewModel = scheduleViewModel,
            onSuccess = { backStack.popUpToOrPush(AppDestinations.ScheduledTransactions) },
            onBack = backHandler.requestBack
        )
    }
}

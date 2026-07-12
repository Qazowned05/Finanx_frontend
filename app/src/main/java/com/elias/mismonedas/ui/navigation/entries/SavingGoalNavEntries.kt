package com.elias.mismonedas.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.pop
import com.elias.mismonedas.ui.navigation.push
import com.elias.mismonedas.ui.screens.saving.CreateSavingGoalScreen
import com.elias.mismonedas.ui.screens.saving.SavingGoalScreen
import com.elias.mismonedas.ui.viewmodel.SavingGoalViewModel

fun EntryProviderScope<NavKey>.savingGoalEntries(
    backStack: NavBackStack<NavKey>,
    savingGoalViewModel: SavingGoalViewModel,
    userId: Long
) {
    entry<AppDestinations.SavingGoals> {
        SetTopBar(TopBarConfig(title = "Metas de ahorro"))

        SavingGoalScreen(
            userId = userId,
            viewModel = savingGoalViewModel,
            onCreateGoal = { backStack.push(AppDestinations.CreateSavingGoal) }
        )
    }

    entry<AppDestinations.CreateSavingGoal> {
        SetTopBar(TopBarConfig(title = "Crear meta"))

        CreateSavingGoalScreen(
            userId = userId,
            viewModel = savingGoalViewModel,
            onSuccess = { backStack.pop() }
        )
    }
}

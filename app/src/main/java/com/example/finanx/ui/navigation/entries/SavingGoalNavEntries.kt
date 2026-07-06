package com.example.finanx.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.navigation.AppDestination
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.SetTopBar
import com.example.finanx.ui.navigation.TopBarConfig
import com.example.finanx.ui.navigation.pop
import com.example.finanx.ui.navigation.push
import com.example.finanx.ui.screens.saving.CreateSavingGoalScreen
import com.example.finanx.ui.screens.saving.SavingGoalScreen
import com.example.finanx.ui.viewmodel.SavingGoalViewModel

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

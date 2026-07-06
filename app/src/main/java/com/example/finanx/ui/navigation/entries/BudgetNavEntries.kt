package com.example.finanx.ui.navigation.entries

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.navigation.AppDestination
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.SetTopBar
import com.example.finanx.ui.navigation.TopBarConfig
import com.example.finanx.ui.navigation.pop
import com.example.finanx.ui.navigation.push
import com.example.finanx.ui.screens.budget.BudgetListScreen
import com.example.finanx.ui.screens.budget.CreateBudgetScreen
import com.example.finanx.ui.screens.budget.EditBudgetScreen
import com.example.finanx.ui.viewmodel.BudgetState
import com.example.finanx.ui.viewmodel.BudgetViewModel
import com.example.finanx.ui.viewmodel.CategoryViewModel
import com.example.finanx.ui.viewmodel.ScheduleViewModel

fun EntryProviderScope<NavKey>.budgetEntries(
    backStack: NavBackStack<NavKey>,
    budgetViewModel: BudgetViewModel,
    categoryViewModel: CategoryViewModel,
    scheduleViewModel: ScheduleViewModel,
    userId: Long
) {
    entry<AppDestinations.Budgets> {
        SetTopBar(TopBarConfig(title = "Presupuestos"))

        BudgetListScreen(
            userId = userId,
            viewModel = budgetViewModel,
            scheduleViewModel = scheduleViewModel,
            categoryViewModel = categoryViewModel,
            onCreateBudget = { backStack.push(AppDestinations.CreateBudget) },
            onEditBudget = { id -> backStack.push(AppDestinations.EditBudget(id)) }
        )
    }

    entry<AppDestinations.CreateBudget> {
        SetTopBar(TopBarConfig(title = "Nuevo presupuesto"))

        LaunchedEffect(userId) { categoryViewModel.fetchCategories(userId) }

        CreateBudgetScreen(
            userId = userId,
            categoryState = categoryViewModel.categoryState,
            viewModel = budgetViewModel,
            onCancel = { backStack.pop() }
        )
    }

    entry<AppDestinations.EditBudget> { key ->
        SetTopBar(TopBarConfig(title = "Editar presupuesto"))

        LaunchedEffect(key.id) {
            categoryViewModel.fetchCategories(userId)
            budgetViewModel.fetchDetailedBudgets(userId)
        }

        val detailed = budgetViewModel.budgetState
        val budget = (detailed as? BudgetState.Success)?.budgets?.firstOrNull { it.id == key.id }

        if (budget != null) {
            EditBudgetScreen(
                userId = userId,
                budget = budget,
                categoryState = categoryViewModel.categoryState,
                viewModel = budgetViewModel,
                onCancel = { backStack.pop() }
            )
        }
    }
}

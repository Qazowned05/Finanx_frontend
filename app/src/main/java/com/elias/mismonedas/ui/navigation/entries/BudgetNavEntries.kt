package com.elias.mismonedas.ui.navigation.entries

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.pop
import com.elias.mismonedas.ui.navigation.push
import com.elias.mismonedas.ui.screens.budget.BudgetListScreen
import com.elias.mismonedas.ui.screens.budget.CreateBudgetScreen
import com.elias.mismonedas.ui.screens.budget.EditBudgetScreen
import com.elias.mismonedas.ui.viewmodel.BudgetUIState
import com.elias.mismonedas.ui.viewmodel.BudgetViewModel
import com.elias.mismonedas.ui.viewmodel.CategoryViewModel
import com.elias.mismonedas.ui.viewmodel.ScheduleViewModel

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
            state = budgetViewModel.budgetListState,
            scheduleViewModel = scheduleViewModel,
            categoryViewModel = categoryViewModel,
            onCreateBudget = { backStack.push(AppDestinations.CreateBudget) },
            onEditBudget = { id -> backStack.push(AppDestinations.EditBudget(id)) },
            onRefresh = budgetViewModel::refreshBudgetList,
            onLoadMore = budgetViewModel::loadMore,
            onStateFilter = budgetViewModel::setStateFilter,
            onHealthFilter = budgetViewModel::setHealthFilter,
            onDateFilter = budgetViewModel::setDateFilter,
            onClearFilters = budgetViewModel::clearFilters
        )
    }

    entry<AppDestinations.CreateBudget> {
        SetTopBar(TopBarConfig(title = "Nuevo presupuesto"))

        LaunchedEffect(userId) { categoryViewModel.fetchCategories() }

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
            categoryViewModel.fetchCategories()
            budgetViewModel.fetchDetailedBudgets(userId)
        }

        val detailed = budgetViewModel.budgetUIState
        val budget = (detailed as? BudgetUIState.Success)?.budgets?.firstOrNull { it.id == key.id }

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

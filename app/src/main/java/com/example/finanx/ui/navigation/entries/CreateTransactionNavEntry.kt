package com.example.finanx.ui.navigation.entries

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.navigation.AppDestination
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.SetTopBar
import com.example.finanx.ui.navigation.TopBarConfig
import com.example.finanx.ui.navigation.pop
import com.example.finanx.ui.navigation.push
import com.example.finanx.ui.navigation.rememberUnsavedChangesBackHandler
import com.example.finanx.ui.screens.transaction.create.CreateTransactionScreen
import com.example.finanx.ui.viewmodel.SavingGoalState
import com.example.finanx.ui.viewmodel.SavingGoalViewModel
import com.example.finanx.ui.viewmodel.TransactionViewModel
import com.example.finanx.ui.viewmodel.HomeViewModel

/**
 * Wizard de creación de movimiento. Es uno de los flujos "clave" migrados a
 * fondo: aquí es donde vivía el bug reportado (back nativo vs back del
 * TopBar desincronizados). Ahora ambos llaman exactamente a la misma función
 * (`backHandler.requestBack`), así que no pueden desincronizarse.
 */
fun EntryProviderScope<NavKey>.createTransactionEntries(
    backStack: NavBackStack<NavKey>,
    transactionViewModel: TransactionViewModel,
    savingGoalViewModel: SavingGoalViewModel,
    homeViewModel: HomeViewModel,
    userId: Long,
    userDisplayName: String,
    shouldPreserveDraft: () -> Boolean,
    markPreserveDraft: () -> Unit
) {
    entry<AppDestinations.CreateTransaction> {
        var hasUnsavedChanges by remember { mutableStateOf<(() -> Boolean)?>(null) }

        LaunchedEffect(Unit) {
            if (!shouldPreserveDraft()) {
                transactionViewModel.resetDraft()
                transactionViewModel.initDefaultsFromPrefs()
            }
            val options = transactionViewModel.state.formOptions
            if (options.categories.isEmpty() && !transactionViewModel.state.isLoading) {
                transactionViewModel.load(userId)
            }
            savingGoalViewModel.fetchSavingGoals(userId)
        }

        val backHandler = rememberUnsavedChangesBackHandler(
            hasUnsavedChanges = { hasUnsavedChanges?.invoke() == true },
            onLeave = { backStack.pop() }
        )

        SetTopBar(
            TopBarConfig(
                title = "Nuevo movimiento",
                showBack = true,
                onBack = backHandler.requestBack
            )
        )

        CreateTransactionScreen(
            registerHasUnsaved = { fn -> hasUnsavedChanges = fn },
            userId = userId,
            transactionViewModel = transactionViewModel,
            savingGoalViewModel = savingGoalViewModel,
            onCreateSavingGoal = {
                markPreserveDraft()
                backStack.push(AppDestinations.CreateSavingGoal)
            },
            onCreateCategory = { backStack.push(AppDestinations.Categories) },
            onSaved = {
                transactionViewModel.resetDraft()
                transactionViewModel.consumeSaveState()
                homeViewModel.load(userId = userId, userName = userDisplayName)
                backStack.pop()
            }
        )
    }
}

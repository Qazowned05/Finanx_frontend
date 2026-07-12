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
import com.elias.mismonedas.ui.navigation.push
import com.elias.mismonedas.ui.navigation.rememberUnsavedChangesBackHandler
import com.elias.mismonedas.ui.screens.transaction.create.CreateTransactionScreen
import com.elias.mismonedas.ui.viewmodel.SavingGoalViewModel
import com.elias.mismonedas.ui.viewmodel.TransactionViewModel
import com.elias.mismonedas.ui.viewmodel.HomeViewModel
 
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

    entry<AppDestinations.CreateTransactionFromOcr> { key ->
        var hasUnsavedChanges by remember { mutableStateOf<(() -> Boolean)?>(null) }

        LaunchedEffect(Unit) {
            transactionViewModel.resetDraft()
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
                title = "Nuevo movimiento (OCR)",
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
            },
            ocrAmount = key.ocrAmount,
            ocrMerchant = key.ocrMerchant,
            ocrReceiptId = key.ocrReceiptId,
            ocrCurrency = key.ocrCurrency,
            ocrPaymentMethod = key.ocrPaymentMethod,
            ocrTransactionType = key.ocrTransactionType,
            ocrCategoryId = key.ocrCategoryId
        )
    }
}

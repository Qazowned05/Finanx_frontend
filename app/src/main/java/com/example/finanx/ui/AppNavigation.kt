package com.example.finanx.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.AppScaffold
import com.example.finanx.ui.navigation.entries.analyticsEntries
import com.example.finanx.ui.navigation.entries.budgetEntries
import com.example.finanx.ui.navigation.entries.categoryEntries
import com.example.finanx.ui.navigation.entries.chatEntries
import com.example.finanx.ui.navigation.entries.createTransactionEntries
import com.example.finanx.ui.navigation.entries.homeEntries
import com.example.finanx.ui.navigation.entries.moreEntries
import com.example.finanx.ui.navigation.entries.notificationEntries
import com.example.finanx.ui.navigation.entries.profileEntries
import com.example.finanx.ui.navigation.entries.savingGoalEntries
import com.example.finanx.ui.navigation.entries.scheduleEntries
import com.example.finanx.ui.navigation.entries.transactionsEntries
import com.example.finanx.ui.navigation.navigateToTab
import com.example.finanx.ui.navigation.push
import com.example.finanx.ui.viewmodel.AnalyticsViewModel
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.BudgetViewModel
import com.example.finanx.ui.viewmodel.CategoryViewModel
import com.example.finanx.ui.viewmodel.ChatViewModel
import com.example.finanx.ui.viewmodel.HomeViewModel
import com.example.finanx.ui.viewmodel.NotificationViewModel
import com.example.finanx.ui.viewmodel.SavingGoalViewModel
import com.example.finanx.ui.viewmodel.ScheduleViewModel
import com.example.finanx.ui.viewmodel.TransactionViewModel
import com.example.finanx.ui.viewmodel.UserViewModel
import org.openapitools.client.models.UserResponse

/**
 * Punto de entrada de la navegación autenticada.
 *
 * Antes: un solo archivo de 686 líneas con el NavHost, el Scaffold, el TopBar
 * y la lógica de cada una de las ~20 pantallas mezclados.
 *
 * Ahora: este archivo solo hace 3 cosas —
 *  1) crea el back stack y los ViewModels de sesión (inyectados por Hilt),
 *  2) arma la tabla de destinos combinando los *Entries de cada feature,
 *  3) delega el Scaffold/TopBar/BottomBar a [AppScaffold].
 *
 * Cada feature (home, transactions, budgets, schedule, etc.) vive en su
 * propio archivo bajo `ui/navigation/entries/`, dueño de su propio TopBar,
 * su propio manejo de back y su propia navegación interna.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(user: UserResponse, authViewModel: AuthViewModel) {
    val userId = requireNotNull(user.id)
    val userName = user.displayName()
    val context = LocalContext.current
    val activity = context as? Activity

    // ViewModels de sesión: viven mientras dure la sesión autenticada y se
    // comparten entre pantallas (p.ej. crear un movimiento debe refrescar
    // Home y Transacciones). Se obtienen una sola vez aquí, fuera de
    // cualquier NavEntry, así que quedan scoped a la Activity — igual que
    // antes con `viewModel()`, solo que ahora inyectados por Hilt.
    val homeViewModel: HomeViewModel = hiltViewModel()
    val transactionViewModel: TransactionViewModel = hiltViewModel()
    val budgetViewModel: BudgetViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val savingGoalViewModel: SavingGoalViewModel = hiltViewModel()
    val scheduleViewModel: ScheduleViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    val chatViewModel: ChatViewModel = hiltViewModel()
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val analyticsViewModel: AnalyticsViewModel = hiltViewModel()

    val backStack = rememberNavBackStack(AppDestinations.START_DESTINATION)
    val snackbarHostState = remember { SnackbarHostState() }

    // Reemplaza al truco de `savedStateHandle["preserve_transaction_draft"]`:
    // como el back stack ahora es una simple lista en memoria (no hay grafo
    // separado por tab), basta una bandera de "consumir una vez" para saber
    // si hay que preservar el draft al volver de "Crear meta de ahorro".
    var preserveTransactionDraft by remember { mutableStateOf(false) }

    LaunchedEffect(userId) { notificationViewModel.fetchAll(userId) }

    LaunchedEffect(transactionViewModel.state.actionMessage) {
        val message = transactionViewModel.state.actionMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        transactionViewModel.consumeMessage()
    }

    AppScaffold(
        backStack = backStack,
        notificationCount = notificationViewModel.badgeCount,
        onFinn = { backStack.push(AppDestinations.Chat) },
        onNotifications = { backStack.push(AppDestinations.Notifications) },
        onProfile = { backStack.push(AppDestinations.Profile) },
        onLogout = { authViewModel.logout(activity) },
        onNavigateToTab = { tab -> backStack.navigateToTab(tab) },
        snackbarHostState = snackbarHostState
    ) { modifier ->
        Box(modifier = modifier) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    homeEntries(backStack, homeViewModel, userId, userName)
                    transactionsEntries(backStack, transactionViewModel, userId)
                    createTransactionEntries(
                        backStack = backStack,
                        transactionViewModel = transactionViewModel,
                        savingGoalViewModel = savingGoalViewModel,
                        homeViewModel = homeViewModel,
                        userId = userId,
                        userDisplayName = userName,
                        shouldPreserveDraft = {
                            val preserve = preserveTransactionDraft
                            preserveTransactionDraft = false
                            preserve
                        },
                        markPreserveDraft = { preserveTransactionDraft = true }
                    )
                    categoryEntries(backStack, categoryViewModel, authViewModel, userId)
                    savingGoalEntries(backStack, savingGoalViewModel, userId)
                    moreEntries(backStack)
                    analyticsEntries(backStack, analyticsViewModel, userId)
                    chatEntries(backStack, chatViewModel, userId)
                    budgetEntries(backStack, budgetViewModel, categoryViewModel, scheduleViewModel, userId)
                    scheduleEntries(backStack, scheduleViewModel, userId)
                    notificationEntries(backStack, notificationViewModel, userId)
                    profileEntries(
                        backStack = backStack,
                        userViewModel = userViewModel,
                        user = user,
                        userId = userId,
                        onLogout = { authViewModel.logout(activity) }
                    )
                }
            )
        }
    }
}

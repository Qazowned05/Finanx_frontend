package com.elias.mismonedas.ui.navigation.entries

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.navigateToTab
import com.elias.mismonedas.ui.navigation.push
import com.elias.mismonedas.ui.screens.HomeScreen
import com.elias.mismonedas.ui.viewmodel.HomeViewModel

fun EntryProviderScope<NavKey>.homeEntries(
    backStack: NavBackStack<NavKey>,
    homeViewModel: HomeViewModel,
    userId: Long,
    userName: String
) {
    entry<AppDestinations.Home> {
        SetTopBar(TopBarConfig(title = "Inicio"))

        LaunchedEffect(userId) {
            homeViewModel.load(userId = userId, userName = userName)
        }

        HomeScreen(
            userId = userId,
            state = homeViewModel.state,
            onNewTransaction = { backStack.push(AppDestinations.CreateTransaction) },
            onScanReceipt = { backStack.push(AppDestinations.Scanner) },
            onOpenTransactions = { backStack.navigateToTab(AppDestinations.Transactions) },
            onRetry = { homeViewModel.refresh() }
        )
    }
}

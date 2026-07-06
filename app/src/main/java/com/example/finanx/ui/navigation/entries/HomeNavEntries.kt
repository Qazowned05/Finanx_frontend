package com.example.finanx.ui.navigation.entries

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.navigation.AppDestination
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.SetTopBar
import com.example.finanx.ui.navigation.TopBarConfig
import com.example.finanx.ui.navigation.navigateToTab
import com.example.finanx.ui.navigation.push
import com.example.finanx.ui.screens.HomeScreen
import com.example.finanx.ui.viewmodel.HomeViewModel

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
            state = homeViewModel.state,
            onNewTransaction = { backStack.push(AppDestinations.CreateTransaction) },
            onOpenTransactions = { backStack.navigateToTab(AppDestinations.Transactions) },
            onRetry = { homeViewModel.refresh() }
        )
    }
}

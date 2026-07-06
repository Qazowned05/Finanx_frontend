package com.example.finanx.ui.navigation.entries

import androidx.compose.runtime.DisposableEffect
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
import com.example.finanx.ui.screens.TransactionsScreen
import com.example.finanx.ui.screens.transaction.TransactionDetailScreen
import com.example.finanx.ui.viewmodel.TransactionViewModel

fun EntryProviderScope<NavKey>.transactionsEntries(
    backStack: NavBackStack<NavKey>,
    transactionViewModel: TransactionViewModel,
    userId: Long
) {
    entry<AppDestinations.Transactions> {
        SetTopBar(TopBarConfig(title = "Movimientos"))

        LaunchedEffect(userId) { transactionViewModel.load(userId) }

        TransactionsScreen(
            state = transactionViewModel.state,
            onQueryChange = transactionViewModel::setQuery,
            onRefresh = transactionViewModel::refresh,
            onNewTransaction = { backStack.push(AppDestinations.CreateTransaction) },
            onDeleteTransaction = transactionViewModel::disableTransaction,
            onOpenTransactionDetails = { transaction ->
                transaction.id?.let { id ->
                    transactionViewModel.selectTransaction(id)
                    backStack.push(AppDestinations.TransactionDetail(id))
                }
            }
        )
    }

    entry<AppDestinations.TransactionDetail> { key ->
        SetTopBar(TopBarConfig(title = "Detalle del movimiento"))

        LaunchedEffect(key.id) { transactionViewModel.selectTransaction(key.id) }
        DisposableEffect(Unit) { onDispose { transactionViewModel.clearSelectedTransaction() } }

        TransactionDetailScreen(
            transactionId = key.id,
            viewModel = transactionViewModel,
            onBack = { backStack.pop() }
        )
    }
}

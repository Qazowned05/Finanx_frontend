package com.elias.mismonedas.ui.navigation.entries

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.pop
import com.elias.mismonedas.ui.navigation.push
import com.elias.mismonedas.ui.screens.ScannerScreen
import com.elias.mismonedas.ui.screens.TransactionsScreen
import com.elias.mismonedas.ui.screens.transaction.TransactionDetailScreen
import com.elias.mismonedas.ui.viewmodel.ReceiptViewModel
import com.elias.mismonedas.ui.viewmodel.TransactionViewModel

fun EntryProviderScope<NavKey>.transactionsEntries(
    backStack: NavBackStack<NavKey>,
    transactionViewModel: TransactionViewModel,
    receiptViewModel: ReceiptViewModel,
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
            onScanReceipt = { backStack.push(AppDestinations.Scanner) },
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
        DisposableEffect(Unit) { onDispose { receiptViewModel.clearReceiptImage() } }

        TransactionDetailScreen(
            transactionId = key.id,
            viewModel = transactionViewModel,
            receiptViewModel = receiptViewModel,
            onBack = { backStack.pop() }
        )
    }

    entry<AppDestinations.Scanner> {
        SetTopBar(TopBarConfig(title = "Escanear recibo", showBack = true, onBack = { backStack.pop() }))

        ScannerScreen(
            receiptViewModel = receiptViewModel,
            userId = userId,
            onScanComplete = { amount, merchant, receiptId, currency, paymentMethod, transactionType, categoryId ->
                backStack.pop()
                backStack.push(
                    AppDestinations.CreateTransactionFromOcr(
                        ocrAmount = amount,
                        ocrMerchant = merchant,
                        ocrReceiptId = receiptId,
                        ocrCurrency = currency,
                        ocrPaymentMethod = paymentMethod,
                        ocrTransactionType = transactionType,
                        ocrCategoryId = categoryId
                    )
                )
            },
            onBack = { backStack.pop() }
        )
    }
}

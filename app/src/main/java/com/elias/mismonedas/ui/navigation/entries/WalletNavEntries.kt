package com.elias.mismonedas.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.pop
import com.elias.mismonedas.ui.screens.wallet.WalletScreen
import com.elias.mismonedas.ui.viewmodel.WalletViewModel

fun EntryProviderScope<NavKey>.walletEntries(
    backStack: NavBackStack<NavKey>,
    walletViewModel: WalletViewModel,
    userId: Long
) {
    entry<AppDestinations.Wallets> {
        SetTopBar(TopBarConfig(title = "Billeteras"))

        WalletScreen(
            userId = userId,
            viewModel = walletViewModel,
            onBack = { backStack.pop() }
        )
    }
}

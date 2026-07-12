package com.elias.mismonedas.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.pop
import com.elias.mismonedas.ui.screens.subscription.SubscriptionScreen
import com.elias.mismonedas.ui.viewmodel.SubscriptionViewModel

fun EntryProviderScope<NavKey>.subscriptionEntries(
    backStack: NavBackStack<NavKey>,
    subscriptionViewModel: SubscriptionViewModel,
    userId: Long
) {
    entry<AppDestinations.Subscription> {
        SetTopBar(TopBarConfig(title = "Suscripción"))

        SubscriptionScreen(
            userId = userId,
            viewModel = subscriptionViewModel,
            onBack = { backStack.pop() }
        )
    }
}

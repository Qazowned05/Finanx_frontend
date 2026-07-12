package com.elias.mismonedas.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.LocalTopBarController
import com.elias.mismonedas.ui.navigation.toTopBarConfig
import com.elias.mismonedas.ui.screens.analytics.AnalyticsScreen
import com.elias.mismonedas.ui.viewmodel.AnalyticsViewModel

fun EntryProviderScope<NavKey>.analyticsEntries(
    backStack: NavBackStack<NavKey>,
    analyticsViewModel: AnalyticsViewModel,
    userId: Long
) {
    entry<AppDestinations.Analysis> {
        val topBarController = LocalTopBarController.current
        AnalyticsScreen(
            userId = userId,
            viewModel = analyticsViewModel,
            onAppBarState = { topBarController.update(it.toTopBarConfig()) }
        )
    }
}

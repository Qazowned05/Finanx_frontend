package com.example.finanx.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.navigation.AppDestination
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.LocalTopBarController
import com.example.finanx.ui.navigation.toTopBarConfig
import com.example.finanx.ui.screens.analytics.AnalyticsScreen
import com.example.finanx.ui.viewmodel.AnalyticsViewModel

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

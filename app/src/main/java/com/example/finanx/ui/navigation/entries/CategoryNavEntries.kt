package com.example.finanx.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.navigation.AppDestination
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.SetTopBar
import com.example.finanx.ui.navigation.TopBarConfig
import com.example.finanx.ui.navigation.push
import com.example.finanx.ui.screens.category.CategoryListScreen
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.CategoryViewModel

fun EntryProviderScope<NavKey>.categoryEntries(
    backStack: NavBackStack<NavKey>,
    categoryViewModel: CategoryViewModel,
    authViewModel: AuthViewModel,
    userId: Long
) {
    entry<AppDestinations.Categories> {
        SetTopBar(TopBarConfig(title = "Categorías"))

        CategoryListScreen(
            userId = userId,
            viewModel = categoryViewModel,
            authViewModel = authViewModel,
            onNavigateToNotifications = { backStack.push(AppDestinations.Notifications) },
            onNavigateToProfile = { backStack.push(AppDestinations.Profile) }
        )
    }
}

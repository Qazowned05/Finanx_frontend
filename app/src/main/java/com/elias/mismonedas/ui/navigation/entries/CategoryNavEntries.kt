package com.elias.mismonedas.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.push
import com.elias.mismonedas.ui.screens.category.CategoryListScreen
import com.elias.mismonedas.ui.viewmodel.AuthViewModel
import com.elias.mismonedas.ui.viewmodel.CategoryViewModel

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

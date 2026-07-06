package com.example.finanx.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.navigation.AppDestination
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.SetTopBar
import com.example.finanx.ui.navigation.TopBarConfig
import com.example.finanx.ui.navigation.push
import com.example.finanx.ui.screens.FunctionalitiesScreen

fun EntryProviderScope<NavKey>.moreEntries(
    backStack: NavBackStack<NavKey>
) {
    entry<AppDestinations.More> {
        SetTopBar(TopBarConfig(title = "Más"))
        FunctionalitiesScreen(onNavigate = { destination -> backStack.push(destination) })
    }
}

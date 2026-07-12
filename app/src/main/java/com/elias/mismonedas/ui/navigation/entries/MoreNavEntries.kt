package com.elias.mismonedas.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.push
import com.elias.mismonedas.ui.screens.FunctionalitiesScreen

fun EntryProviderScope<NavKey>.moreEntries(
    backStack: NavBackStack<NavKey>
) {
    entry<AppDestinations.More> {
        SetTopBar(TopBarConfig(title = "Más"))
        FunctionalitiesScreen(onNavigate = { destination -> backStack.push(destination) })
    }
}

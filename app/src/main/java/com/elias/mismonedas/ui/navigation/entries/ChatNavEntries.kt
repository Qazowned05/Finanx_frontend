package com.elias.mismonedas.ui.navigation.entries

import androidx.activity.compose.BackHandler
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.LocalTopBarController
import com.elias.mismonedas.ui.navigation.pop
import com.elias.mismonedas.ui.navigation.toTopBarConfig
import com.elias.mismonedas.ui.screens.chat.ChatScreen
import com.elias.mismonedas.ui.viewmodel.ChatViewModel

fun EntryProviderScope<NavKey>.chatEntries(
    backStack: NavBackStack<NavKey>,
    chatViewModel: ChatViewModel,
    userId: Long
) {
    entry<AppDestinations.Chat> {
        val topBarController = LocalTopBarController.current
        val hasOpenSession = chatViewModel.state.selectedSession != null

        // Si hay una sesión de chat abierta, el back (nativo o del TopBar) la
        // cierra primero en vez de salir de la pantalla — mismo comportamiento
        // que antes, ahora sincronizado por construcción porque ambos caminos
        // llaman exactamente a la misma función.
        val onBack: () -> Unit = {
            if (hasOpenSession) chatViewModel.closeSelectedSession() else backStack.pop()
        }

        BackHandler(onBack = onBack)

        ChatScreen(
            userId = userId,
            viewModel = chatViewModel,
            onAppBarState = { topBarController.update(it.toTopBarConfig(onBackOverride = onBack)) }
        )
    }
}

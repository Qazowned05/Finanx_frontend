package com.elias.mismonedas.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.navigation.pop
import com.elias.mismonedas.ui.navigation.push
import com.elias.mismonedas.ui.screens.profile.DeleteAccountScreen
import com.elias.mismonedas.ui.screens.profile.ProfileScreen
import com.elias.mismonedas.ui.screens.profile.ResetDataScreen
import com.elias.mismonedas.ui.viewmodel.UserViewModel
import org.openapitools.client.models.UserResponse

fun EntryProviderScope<NavKey>.profileEntries(
    backStack: NavBackStack<NavKey>,
    userViewModel: UserViewModel,
    user: UserResponse,
    userId: Long,
    onLogout: () -> Unit
) {
    entry<AppDestinations.Profile> {
        SetTopBar(TopBarConfig(title = "Mi perfil"))

        ProfileScreen(
            user = user,
            userId = userId,
            viewModel = userViewModel,
            onLogout = onLogout,
            onOpenSubscription = { backStack.push(AppDestinations.Subscription) },
            onOpenResetData = { backStack.push(AppDestinations.ProfileResetData) },
            onOpenDeleteAccount = { backStack.push(AppDestinations.ProfileDeleteAccount) }
        )
    }

    entry<AppDestinations.ProfileResetData> {
        SetTopBar(TopBarConfig(title = "Empezar de Cero"))

        ResetDataScreen(
            userId = userId,
            viewModel = userViewModel,
            onBack = { backStack.pop() },
            onLogout = onLogout
        )
    }

    entry<AppDestinations.ProfileDeleteAccount> {
        SetTopBar(TopBarConfig(title = "Eliminar Cuenta"))

        DeleteAccountScreen(
            userId = userId,
            viewModel = userViewModel,
            onBack = { backStack.pop() },
            onLogout = onLogout
        )
    }
}

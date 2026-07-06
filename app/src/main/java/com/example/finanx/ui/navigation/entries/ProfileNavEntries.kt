package com.example.finanx.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.navigation.AppDestination
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.SetTopBar
import com.example.finanx.ui.navigation.TopBarConfig
import com.example.finanx.ui.navigation.pop
import com.example.finanx.ui.navigation.push
import com.example.finanx.ui.screens.profile.DeleteAccountScreen
import com.example.finanx.ui.screens.profile.ProfileScreen
import com.example.finanx.ui.screens.profile.ResetDataScreen
import com.example.finanx.ui.viewmodel.UserViewModel
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

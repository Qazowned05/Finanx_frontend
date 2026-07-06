package com.example.finanx.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.navigation.AppDestination
import com.example.finanx.ui.navigation.AppDestinations
import com.example.finanx.ui.navigation.SetTopBar
import com.example.finanx.ui.navigation.TopBarConfig
import com.example.finanx.ui.navigation.pop
import com.example.finanx.ui.screens.notification.NotificationsScreen
import com.example.finanx.ui.viewmodel.NotificationViewModel

fun EntryProviderScope<NavKey>.notificationEntries(
    backStack: NavBackStack<NavKey>,
    notificationViewModel: NotificationViewModel,
    userId: Long
) {
    entry<AppDestinations.Notifications> {
        SetTopBar(TopBarConfig(title = "Notificaciones"))

        NotificationsScreen(
            userId = userId,
            viewModel = notificationViewModel,
            onBack = { backStack.pop() }
        )
    }
}

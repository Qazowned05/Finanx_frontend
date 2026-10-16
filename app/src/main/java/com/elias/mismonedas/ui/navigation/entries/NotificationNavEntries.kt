package com.elias.mismonedas.ui.navigation.entries

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.elias.mismonedas.ui.navigation.AppDestinations
import com.elias.mismonedas.ui.navigation.SetTopBar
import com.elias.mismonedas.ui.navigation.TopBarConfig
import com.elias.mismonedas.ui.screens.notification.NotificationsScreen
import com.elias.mismonedas.ui.viewmodel.NotificationViewModel

fun EntryProviderScope<NavKey>.notificationEntries(
    backStack: NavBackStack<NavKey>,
    notificationViewModel: NotificationViewModel,
    userId: Long
) {
    entry<AppDestinations.Notifications> {
        SetTopBar(TopBarConfig(title = "Notificaciones"))

        LaunchedEffect(userId) { notificationViewModel.load(userId) }

        NotificationsScreen(
            state = notificationViewModel.notificationState,
            onRefresh = notificationViewModel::refresh,
            onLoadMore = notificationViewModel::loadMore,
            onSearchChange = notificationViewModel::setSearch,
            onStateFilter = notificationViewModel::setStateFilter,
            onDiscardNotification = { id -> notificationViewModel.discardNotification(userId, id) },
            onMarkAsRead = { id -> notificationViewModel.markAsRead(userId, id) }
        )
    }
}

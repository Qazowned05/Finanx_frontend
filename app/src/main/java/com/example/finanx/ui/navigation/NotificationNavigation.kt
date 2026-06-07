package com.example.finanx.ui.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finanx.ui.screens.notification.NotificacionesScreen
import com.example.finanx.ui.viewmodel.NotificationViewModel

// Ruta única para la pantalla de notificaciones.
// Se usa como constante para evitar escribir "notifications" varias veces.
const val NOTIFICATIONS_ROUTE = "notifications"

// Registra la pantalla de notificaciones dentro del NavHost principal.
// Esta función mantiene la navegación de notificaciones separada del resto del proyecto.
fun NavGraphBuilder.notificationNavigation(userId: Long) {
    composable(NOTIFICATIONS_ROUTE) {
        // ViewModel propio para manejar la lógica y estado de notificaciones.
        val notificationViewModel: NotificationViewModel = viewModel()

        // Pantalla de notificaciones asociada al usuario actual.
        NotificacionesScreen(userId, notificationViewModel)
    }
}


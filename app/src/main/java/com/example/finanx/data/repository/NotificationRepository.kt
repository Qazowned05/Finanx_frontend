package com.example.finanx.data.repository

import org.openapitools.client.apis.NotificationControllerApi
import org.openapitools.client.models.NotificationDTO
import retrofit2.Response

// Repositorio encargado de consumir la API generada de notificaciones.
// No crea endpoints nuevos, solo usa NotificationControllerApi del kotlin-client.
class NotificationRepository(
    private val api: NotificationControllerApi
) {
    // Obtiene todas las notificaciones de un usuario.
    suspend fun getNotificationsByUser(userId: Long): Response<List<NotificationDTO>> {
        return api.findAllByUser2(userId)
    }

    // Obtiene notificaciones filtradas por estado.
    suspend fun getNotificationsByState(
        userId: Long,
        state: String
    ): Response<List<NotificationDTO>> {
        return api.findAllByUserAndState3(userId, state)
    }

    // Descarta una notificación.
    suspend fun discardNotification(notificationId: Long): Response<Unit> {
        return api.discard(notificationId)
    }
}
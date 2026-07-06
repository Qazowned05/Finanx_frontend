package com.example.finanx.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.NotificationsApi
import org.openapitools.client.models.NotificationDTO
import retrofit2.Response
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val api: NotificationsApi
) {
    suspend fun getNotificationsByUser(userId: Long): Response<List<NotificationDTO>> =
        withContext(Dispatchers.IO) { api.findAllNotificationByUser(userId) }

    suspend fun getNotificationById(id: Long): Response<NotificationDTO> =
        withContext(Dispatchers.IO) { api.findNotificationById(id) }

    suspend fun getNotificationsByState(
        userId: Long,
        state: String
    ): Response<List<NotificationDTO>> =
        withContext(Dispatchers.IO) { api.findAllNotificationsByUserAndState(userId, state) }

    suspend fun discardNotification(notificationId: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.discardNotification(notificationId) }

    suspend fun markAsRead(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.markAsRead(id) }
}

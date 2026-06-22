package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.NotificationControllerApi
import org.openapitools.client.models.NotificationDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NotificationRepository(
    private val api: NotificationControllerApi = RetrofitClient.notificationApi
) {
    suspend fun getNotificationsByUser(userId: Long): Response<List<NotificationDTO>> =
        withContext(Dispatchers.IO) { api.findAllByUser2(userId) }

    suspend fun getNotificationById(id: Long): Response<NotificationDTO> =
        withContext(Dispatchers.IO) { api.findById6(id) }

    suspend fun getNotificationsByState(
        userId: Long,
        state: String
    ): Response<List<NotificationDTO>> =
        withContext(Dispatchers.IO) { api.findAllByUserAndState3(userId, state) }

    suspend fun getScheduledNotifications(userId: Long): Response<List<NotificationDTO>> =
        withContext(Dispatchers.IO) { api.findAllByUser2(userId) }

    suspend fun discardNotification(notificationId: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.discard(notificationId) }

    suspend fun markAsRead(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.markAsRead(id) }
}

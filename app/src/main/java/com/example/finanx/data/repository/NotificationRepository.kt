package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.NotificationControllerApi
import org.openapitools.client.models.NotificationDTO
import retrofit2.Response

class NotificationRepository(
    private val api: NotificationControllerApi = RetrofitClient.notificationApi
) {
    suspend fun getNotificationsByUser(userId: Long): Response<List<NotificationDTO>> {
        return api.findNotificationsByUser(userId)
    }

    suspend fun getNotificationById(id: Long): Response<NotificationDTO> {
        return api.findNotificationById(id)
    }

    suspend fun getNotificationsByState(
        userId: Long,
        state: String
    ): Response<List<NotificationDTO>> {
        return api.findNotificationsByUserAndState(userId, state)
    }

    suspend fun getScheduledNotifications(userId: Long): Response<List<NotificationDTO>> {
        return api.findScheduledNotificationsByUser(userId)
    }

    suspend fun discardNotification(notificationId: Long): Response<Unit> {
        return api.discardNotification(notificationId)
    }

    suspend fun markAsRead(id: Long): Response<Unit> {
        return api.markNotificationAsRead(id)
    }

    suspend fun disableNotification(id: Long): Response<Unit> {
        return api.disableNotification(id)
    }
}
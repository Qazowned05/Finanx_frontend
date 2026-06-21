package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.NotificationControllerApi
import org.openapitools.client.models.NotificationDTO
import retrofit2.Response

class NotificationRepository(
    private val api: NotificationControllerApi = RetrofitClient.notificationApi
) {
    suspend fun getNotificationsByUser(userId: Long): Response<List<NotificationDTO>> {
        return api.findAllByUser2(userId)
    }

    suspend fun getNotificationById(id: Long): Response<NotificationDTO> {
        return api.findById6(id)
    }

    suspend fun getNotificationsByState(
        userId: Long,
        state: String
    ): Response<List<NotificationDTO>> {
        return api.findAllByUserAndState3(userId, state)
    }

    suspend fun getScheduledNotifications(userId: Long): Response<List<NotificationDTO>> {
        return api.findAllByUser2(userId)
    }

    suspend fun discardNotification(notificationId: Long): Response<Unit> {
        return api.discard(notificationId)
    }

    suspend fun markAsRead(id: Long): Response<Unit> {
        return api.markAsRead(id)
    }
}
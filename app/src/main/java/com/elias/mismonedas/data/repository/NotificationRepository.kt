package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.NotificationsApi
import org.openapitools.client.models.NotificationDTO
import org.openapitools.client.models.NotificationState
import org.openapitools.client.models.PageResponseNotificationDTO
import retrofit2.Response
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val api: NotificationsApi
) {
    suspend fun list(state: NotificationState?): Response<PageResponseNotificationDTO> =
        withContext(Dispatchers.IO) { api.listNotifications(state = state) }

    suspend fun listNotificationPage(
        state: NotificationState? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE
    ): PageResponseNotificationDTO =
        withContext(Dispatchers.IO) {
            api
                .listNotifications(
                    state = state,
                    search = search,
                    page = page,
                    size = size
                )
                .notificationBodyOrThrow("No se pudieron obtener las notificaciones")
        }

    suspend fun getNotificationById(id: Long): Response<NotificationDTO> =
        withContext(Dispatchers.IO) { api.findNotificationById(id) }

    suspend fun discardNotification(notificationId: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.discardNotification(notificationId) }

    suspend fun markAsRead(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.markAsRead(id) }
}

private fun <T> Response<T>.notificationBodyOrThrow(defaultMessage: String): T {
    if (!isSuccessful) {
        throw ApiException(notificationErrorMessage(defaultMessage))
    }
    return body() ?: throw ApiException("$defaultMessage: respuesta vacía")
}

private fun Response<*>.notificationErrorMessage(defaultMessage: String): String {
    val content = runCatching {
        errorBody()?.string()
    }.getOrNull().orEmpty()

    val backendMessage = Regex(
        """"message"\s*:\s*"([^"]+)""""
    ).find(content)?.groupValues?.getOrNull(1)

    return backendMessage ?: "$defaultMessage (${code()})"
}

package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.NotificationDTO
import javax.inject.Inject

data class NotificationUiState(
    val isLoading: Boolean = false,
    override val isRefreshing: Boolean = false,
    val notifications: List<NotificationDTO> = emptyList(),
    val error: String? = null
) : com.elias.mismonedas.ui.viewmodel.RefreshableUiState

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    var notificationState by mutableStateOf(NotificationUiState())
        private set

    val badgeCount: Int
        get() = notificationState.notifications.count {
            it.state == NotificationDTO.State.SCHEDULED ||
                    it.state == NotificationDTO.State.SENT
        }

    fun fetchAll(userId: Long, force: Boolean = false) {
        if (!force && (notificationState.isLoading || notificationState.notifications.isNotEmpty())) return
        viewModelScope.launch {
            // if we already have notifications, treat this as a refresh
            val hasData = notificationState.notifications.isNotEmpty()
            notificationState = if (hasData) notificationState.copy(isRefreshing = true, error = null)
            else notificationState.copy(isLoading = true, error = null)

            try {
                val response = repository.getNotificationsByUser(userId)

                if (response.isSuccessful) {
                    notificationState = notificationState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        notifications = response.body() ?: emptyList(),
                        error = null
                    )
                } else {
                    notificationState = notificationState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = "Error al cargar notificaciones: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                notificationState = notificationState.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = "Error de red: ${e.message}"
                )
            }
        }
    }

    fun discardNotification(userId: Long, notificationId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.discardNotification(notificationId)

                if (response.isSuccessful) {
                    fetchAll(userId, force = true)
                } else {
                    notificationState = notificationState.copy(
                        error = "No se pudo descartar la notificación: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                notificationState = notificationState.copy(
                    error = "Error de red: ${e.message}"
                )
            }
        }
    }

    fun clearRead(userId: Long) {
        viewModelScope.launch {
            try {
                val readNotifications = notificationState.notifications
                    .filter { it.state == NotificationDTO.State.READ }

                readNotifications.forEach { notification ->
                    notification.id?.let { id ->
                        repository.discardNotification(id)
                    }
                }

                fetchAll(userId, force = true)
            } catch (e: Exception) {
                notificationState = notificationState.copy(
                    error = "No se pudieron limpiar las notificaciones leídas."
                )
            }
        }
    }

    fun markAsRead(userId: Long, id: Long): Unit {
        viewModelScope.launch {
            try {
                val response = repository.markAsRead(id)

                if (response.isSuccessful) {
                    fetchAll(userId, force = true)
                } else {
                    notificationState = notificationState.copy(
                        error = "No se pudo marcar como leída la notificación: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                notificationState = notificationState.copy(
                    error = "Error de red: ${e.message}"
                )
            }
        }
    }
}

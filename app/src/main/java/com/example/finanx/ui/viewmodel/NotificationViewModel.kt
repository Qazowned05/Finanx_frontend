package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.repository.NotificationRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.NotificationDTO

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationDTO> = emptyList(),
    val error: String? = null
)

class NotificationViewModel : ViewModel() {

    private val repository = NotificationRepository(
        RetrofitClient.notificationApi
    )

    var notificationState by mutableStateOf(NotificationUiState())
        private set

    fun fetchAll(userId: Long) {
        viewModelScope.launch {
            notificationState = notificationState.copy(
                isLoading = true,
                error = null
            )

            try {
                val response = repository.getNotificationsByUser(userId)

                if (response.isSuccessful) {
                    notificationState = notificationState.copy(
                        isLoading = false,
                        notifications = response.body() ?: emptyList(),
                        error = null
                    )
                } else {
                    notificationState = notificationState.copy(
                        isLoading = false,
                        error = "Error al cargar notificaciones: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                notificationState = notificationState.copy(
                    isLoading = false,
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
                    fetchAll(userId)
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

                fetchAll(userId)
            } catch (e: Exception) {
                notificationState = notificationState.copy(
                    error = "No se pudieron limpiar las notificaciones leídas."
                )
            }
        }
    }
}
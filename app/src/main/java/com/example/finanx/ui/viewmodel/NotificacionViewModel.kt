package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import kotlinx.coroutines.launch
import org.openapitools.client.models.NotificationDTO
import java.time.LocalDateTime

//Estado de pantalla de notificaciones.
//Guarda la carga, la lista de notificaciones y posibles errores.
data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationDTO> = emptyList(),
    val error: String? = null
)
// Es
class NotificationViewModel : ViewModel() {
git
    var notificationState by mutableStateOf(NotificationUiState())
        private set
//Obtiene todas las notificaciones del usuario y las ordena por fecha descendente
    fun fetchAll(userId: Long) {
        viewModelScope.launch {
            notificationState = notificationState.copy(isLoading = true, error = null)

            try {
                val response = RetrofitClient.notificationApi.findAllByUser2(userId)

                if (response.isSuccessful) {
                    val orderedNotifications = response.body()
                        .orEmpty()
                        .filter { it.state != NotificationDTO.State.DISCARD }
                        .sortedByDescending { it.createdAt ?: LocalDateTime.MIN }

                    notificationState = NotificationUiState(
                        isLoading = false,
                        notifications = orderedNotifications
                    )
                } else {
                    notificationState = NotificationUiState(
                        isLoading = false,
                        error = "No se pudieron cargar las notificaciones."
                    )
                }
            } catch (e: Exception) {
                notificationState = NotificationUiState(
                    isLoading = false,
                    error = "Error de conexión: ${e.message}"
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
                        RetrofitClient.notificationApi.discard(id)
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

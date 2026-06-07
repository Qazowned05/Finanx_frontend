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

    var notificationState by mutableStateOf(NotificationUiState())
        private set
//Obtiene todas las notificaciones del usuario y las ordena por fecha descendente
// Obtiene las notificaciones del usuario actual.
// Actualiza el estado de la pantalla según carga, éxito o error.
fun fetchAll(userId: Long) {
    viewModelScope.launch {
        notificationState = notificationState.copy(
            isLoading = true,
            error = null
        )

        try {
            // Por ahora se deja una lista vacía hasta conectar con el API/repositorio.
            notificationState = notificationState.copy(
                isLoading = false,
                notifications = emptyList(),
                error = null
            )

        } catch (e: Exception) {
            notificationState = notificationState.copy(
                isLoading = false,
                error = e.message
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

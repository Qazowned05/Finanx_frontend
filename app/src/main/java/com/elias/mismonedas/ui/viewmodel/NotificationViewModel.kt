package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.openapitools.client.models.NotificationDTO
import org.openapitools.client.models.NotificationState
import org.openapitools.client.models.PageResponseNotificationDTO
import javax.inject.Inject

private const val SEARCH_DEBOUNCE_MS = 350L

data class NotificationUiState(
    val isLoading: Boolean = false,
    override val isRefreshing: Boolean = false,
    val notifications: List<NotificationDTO> = emptyList(),
    val error: String? = null,
    val search: String = "",
    val filterState: NotificationState? = null,
    val page: Int = 0,
    val hasMore: Boolean = true,
    val isLoadingMore: Boolean = false
) : RefreshableUiState

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    var notificationState by mutableStateOf(NotificationUiState())
        private set

    private var currentUserId: Long? = null
    private var searchJob: Job? = null

    val badgeCount: Int
        get() = notificationState.notifications.count {
            it.state == NotificationState.SCHEDULED ||
                    it.state == NotificationState.SENT
        }

    /**
     * Carga la primera página respetando los filtros activos.
     */
    fun load(userId: Long, force: Boolean = false) {
        currentUserId = userId
        if (!force && notificationState.notifications.isNotEmpty()) return
        reload()
    }

    /**
     * Se mantiene por compatibilidad con los llamados de sesión (badge).
     */
    fun fetchAll(userId: Long, force: Boolean = false) {
        currentUserId = userId
        if (!force && (notificationState.isLoading || notificationState.notifications.isNotEmpty())) return
        reload()
    }

    fun refresh() {
        if (currentUserId == null) return
        reload()
    }

    fun loadMore() {
        if (currentUserId == null) return
        if (!notificationState.hasMore ||
            notificationState.isLoadingMore ||
            notificationState.isLoading ||
            notificationState.isRefreshing
        ) {
            return
        }

        viewModelScope.launch {
            notificationState = notificationState.copy(isLoadingMore = true, error = null)

            try {
                val nextPage = notificationState.page + 1
                val response = repository.listNotificationPage(
                    state = notificationState.filterState,
                    search = notificationState.search.trim().ifBlank { null },
                    page = nextPage
                )

                notificationState = notificationState.copy(
                    notifications = notificationState.notifications + (response.items ?: emptyList()),
                    page = response.pagination?.page ?: nextPage,
                    hasMore = response.hasMore(),
                    isLoadingMore = false,
                    error = null
                )
            } catch (e: Exception) {
                notificationState = notificationState.copy(
                    isLoadingMore = false,
                    error = e.message ?: "No se pudieron cargar más notificaciones"
                )
            }
        }
    }

    fun setStateFilter(state: NotificationState?) {
        if (notificationState.filterState == state) return
        searchJob?.cancel()
        notificationState = notificationState.copy(filterState = state)
        currentUserId?.let { applyFilters(it) }
    }

    fun setSearch(query: String) {
        notificationState = notificationState.copy(search = query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            val userId = currentUserId ?: return@launch
            applyFilters(userId)
        }
    }

    fun discardNotification(userId: Long, notificationId: Long) {
        currentUserId = userId
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
        currentUserId = userId
        viewModelScope.launch {
            try {
                val readNotifications = notificationState.notifications
                    .filter { it.state == NotificationState.READ }

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
        currentUserId = userId
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

    private fun reload() {
        viewModelScope.launch {
            val hasData = notificationState.notifications.isNotEmpty()
            notificationState = notificationState.copy(
                isLoading = !hasData,
                isRefreshing = hasData,
                error = null
            )

            try {
                val response = repository.listNotificationPage(
                    state = notificationState.filterState,
                    search = notificationState.search.trim().ifBlank { null },
                    page = 0
                )

                notificationState = notificationState.copy(
                    isLoading = false,
                    isRefreshing = false,
                    notifications = response.items ?: emptyList(),
                    page = response.pagination?.page ?: 0,
                    hasMore = response.hasMore(),
                    isLoadingMore = false,
                    error = null
                )
            } catch (e: Exception) {
                notificationState = notificationState.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = e.message ?: "Error al cargar notificaciones"
                )
            }
        }
    }

    private fun applyFilters(userId: Long) {
        notificationState = notificationState.copy(
            notifications = emptyList(),
            page = 0,
            hasMore = true,
            isLoading = true,
            isRefreshing = false,
            isLoadingMore = false,
            error = null
        )
        load(userId, force = true)
    }

    private fun PageResponseNotificationDTO.hasMore(): Boolean {
        val page = pagination?.page ?: 0
        val totalPages = pagination?.totalPages ?: 1
        return page + 1 < totalPages
    }
}

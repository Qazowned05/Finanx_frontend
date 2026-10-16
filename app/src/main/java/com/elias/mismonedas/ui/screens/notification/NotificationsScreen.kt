package com.elias.mismonedas.ui.screens.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.components.ErrorMessage
import com.elias.mismonedas.ui.components.LoadMoreEffect
import com.elias.mismonedas.ui.components.LoadMoreFooter
import com.elias.mismonedas.ui.components.RefreshableList
import com.elias.mismonedas.ui.theme.BrownPrimary
import com.elias.mismonedas.ui.viewmodel.NotificationUiState
import org.openapitools.client.models.NotificationState

@Composable
fun NotificationsScreen(
    state: NotificationUiState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onSearchChange: (String) -> Unit,
    onStateFilter: (NotificationState?) -> Unit,
    onDiscardNotification: (Long) -> Unit,
    onMarkAsRead: (Long) -> Unit
) {
    val listState = rememberLazyListState()

    val visibleNotifications = state.notifications
        .filter { it.state != NotificationState.DISCARD }

    val pendingNotifications = visibleNotifications
        .filter { it.state != NotificationState.READ }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${pendingNotifications.size} sin leer",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Actualizar",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        NotificationFilterBar(
            query = state.search,
            onQueryChange = onSearchChange,
            selectedState = state.filterState,
            onStateSelected = onStateFilter
        )

        Spacer(modifier = Modifier.height(18.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BrownPrimary)
                }
            }

            state.error != null && visibleNotifications.isEmpty() -> {
                ErrorMessage(
                    message = state.error,
                    onRetry = onRefresh
                )
            }

            visibleNotifications.isEmpty() -> {
                EmptyNotifications()
            }

            else -> {
                state.error?.let { error ->
                    ErrorMessage(
                        message = error,
                        onRetry = onRefresh
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                LoadMoreEffect(
                    listState = listState,
                    enabled = state.hasMore && visibleNotifications.isNotEmpty(),
                    itemCount = visibleNotifications.size,
                    onLoadMore = onLoadMore
                )

                RefreshableList(
                    isRefreshing = state.isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = visibleNotifications,
                            key = { it.id ?: 0L }
                        ) { notification ->
                            NotificationItem(
                                notification = notification,
                                onDiscard = {
                                    notification.id?.let { id -> onDiscardNotification(id) }
                                },
                                onMarkAsRead = {
                                    notification.id?.let { id -> onMarkAsRead(id) }
                                }
                            )
                        }

                        item {
                            LoadMoreFooter(isLoadingMore = state.isLoadingMore)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    selectedState: NotificationState?,
    onStateSelected: (NotificationState?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Buscar notificaciones") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = selectedState == null,
                onClick = { onStateSelected(null) },
                label = { Text("Todos") }
            )
            FilterChip(
                selected = selectedState == NotificationState.READ,
                onClick = { onStateSelected(NotificationState.READ) },
                label = { Text("Leídas") }
            )
            FilterChip(
                selected = selectedState == NotificationState.SENT,
                onClick = { onStateSelected(NotificationState.SENT) },
                label = { Text("No leídas") }
            )
        }
    }
}

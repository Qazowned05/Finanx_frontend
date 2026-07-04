package com.example.finanx.ui.screens.notification

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.components.ErrorMessage
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.viewmodel.NotificationViewModel
import org.openapitools.client.models.NotificationDTO

@Composable
fun NotificationsScreen(
    userId: Long,
    onBack: () -> Unit,
    viewModel: NotificationViewModel = viewModel()
) {
    val state = viewModel.notificationState

    LaunchedEffect(userId) {
        viewModel.fetchAll(userId)
    }

    val visibleNotifications = state.notifications
        .filter { it.state != NotificationDTO.State.DISCARD }

    val pendingNotifications = visibleNotifications
        .filter { it.state != NotificationDTO.State.READ }

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

            IconButton(onClick = { viewModel.fetchAll(userId) }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Actualizar",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BrownPrimary)
            }
        }

        state.error?.let { error ->
            ErrorMessage(
                message = error,
                onRetry = { viewModel.fetchAll(userId) }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (!state.isLoading && visibleNotifications.isEmpty()) {
            EmptyNotifications()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(visibleNotifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        onDiscard = {
                            notification.id?.let { notificationId ->
                                viewModel.discardNotification(userId, notificationId)
                            }
                        },
                        onMarkAsRead = {
                            notification.id?.let { id ->
                                viewModel.markAsRead(userId, id)
                            }
                        }
                    )
                }
            }
        }
    }
}

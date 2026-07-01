package com.example.finanx.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.BrownSecondary
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.StoneGray
import com.example.finanx.ui.theme.SurfaceWhite
import com.example.finanx.ui.theme.WarmBackground
import com.example.finanx.ui.viewmodel.NotificationViewModel
import org.openapitools.client.models.NotificationDTO

@Composable
fun NotificacionesScreen(
    userId: Long,
    onBack: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit,
    viewModel: NotificationViewModel = viewModel()
) {
    val state = viewModel.notificationState

    LaunchedEffect(userId) {
        viewModel.fetchAll(userId)
    }

    val visibleNotifications = state.notifications
        .filter { it.state != NotificationDTO.State.DISCARD }

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
                text = "${visibleNotifications.size} pendientes",
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
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: NotificationDTO,
    onDiscard: () -> Unit
) {
    val style = notificationStyle(notification)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(style.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = style.icon,
                    contentDescription = null,
                    tint = style.color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, end = 4.dp)
            ) {
                Text(
                    text = notification.message ?: "Sin mensaje",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = notificationStateText(notification.state),
                            color = style.color,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = style.color,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            IconButton(onClick = onDiscard) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Descartar notificación",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ExpenseRed.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = ExpenseRed
            )

            Text(
                text = message,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                color = ExpenseRed,
                style = MaterialTheme.typography.bodySmall
            )

            TextButton(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun EmptyNotifications() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(BrownSecondary.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = BrownPrimary,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "No tienes notificaciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = BrownPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Cuando haya novedades aparecerán aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = StoneGray
        )
    }
}

private data class NotificationVisualStyle(
    val icon: ImageVector,
    val color: Color,
    val background: Color
)

private fun notificationStyle(notification: NotificationDTO): NotificationVisualStyle {
    return when (notification.type) {
        NotificationDTO.Type.WARNING -> NotificationVisualStyle(
            icon = Icons.Default.Warning,
            color = GoldAccent,
            background = GoldAccent.copy(alpha = 0.12f)
        )

        NotificationDTO.Type.SUCCESS -> NotificationVisualStyle(
            icon = Icons.Default.CheckCircle,
            color = IncomeGreen,
            background = IncomeGreen.copy(alpha = 0.12f)
        )

        NotificationDTO.Type.ERROR -> NotificationVisualStyle(
            icon = Icons.Default.Error,
            color = ExpenseRed,
            background = ExpenseRed.copy(alpha = 0.12f)
        )

        NotificationDTO.Type.INFO, null -> NotificationVisualStyle(
            icon = Icons.Default.Info,
            color = BrownPrimary,
            background = BrownSecondary.copy(alpha = 0.14f)
        )
    }
}

private fun notificationStateText(state: NotificationDTO.State?): String {
    return when (state) {
        NotificationDTO.State.SENT -> "Nueva"
        NotificationDTO.State.READ -> "Leída"
        NotificationDTO.State.SCHEDULED -> "Nueva"
        NotificationDTO.State.DISCARD -> "Descartada"
        null -> "Sin estado"
    }
}

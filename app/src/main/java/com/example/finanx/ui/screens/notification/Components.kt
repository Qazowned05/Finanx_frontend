package com.example.finanx.ui.screens.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.components.ErrorMessage
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.BrownSecondary
import org.openapitools.client.models.NotificationDTO


@Composable
fun NotificationItem(
    notification: NotificationDTO,
    onDiscard: () -> Unit,
    onMarkAsRead: () -> Unit
) {
    val style = notificationStyle(notification)
    val isRead = notification.state == NotificationDTO.State.READ

    val cardContainer = if (isRead) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface
    val cardElevation = if (isRead) 0.dp else 2.dp
    val cardBorder = if (isRead) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
    val titleColor = if (isRead) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
    val iconTint = if (isRead) MaterialTheme.colorScheme.onSurfaceVariant else style.color
    val iconBackground = if (isRead) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f) else style.background

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        border = cardBorder
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
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = style.icon,
                    contentDescription = null,
                    tint = iconTint,
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
                    color = titleColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = notificationStateText(notification.state),
                                color = if (isRead) MaterialTheme.colorScheme.onSurfaceVariant else style.color,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = if (isRead) MaterialTheme.colorScheme.onSurfaceVariant else style.color,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    if (!isRead) {
                        AssistChip(
                            onClick = onMarkAsRead,
                            label = {
                                Text(
                                    text = "Marcar leído",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }

            IconButton(onClick = onDiscard) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Descartar notificación",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
 fun EmptyNotifications() {
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
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "No tienes notificaciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Cuando haya novedades aparecerán aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

data class NotificationVisualStyle(
    val icon: ImageVector,
    val color: Color,
    val background: Color
)

fun notificationStyle(notification: NotificationDTO): NotificationVisualStyle {
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

fun notificationStateText(state: NotificationDTO.State?): String {
    return when (state) {
        NotificationDTO.State.SENT -> "Nueva"
        NotificationDTO.State.READ -> "Leída"
        NotificationDTO.State.SCHEDULED -> "Nueva"
        NotificationDTO.State.DISCARD -> "Descartada"
        null -> "Sin estado"
    }
}

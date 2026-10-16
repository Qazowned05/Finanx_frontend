package com.elias.mismonedas.ui.screens.notification

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.theme.ExpenseRed
import com.elias.mismonedas.ui.theme.IncomeGreen
import com.elias.mismonedas.ui.theme.BrownPrimary
import com.elias.mismonedas.ui.theme.BrownSecondary
import org.openapitools.client.models.NotificationDTO
import org.openapitools.client.models.NotificationState
import org.openapitools.client.models.NotificationType


@Composable
fun NotificationItem(
    notification: NotificationDTO,
    onDiscard: () -> Unit,
    onMarkAsRead: () -> Unit
) {
    val style = notificationStyle(notification)
    val isRead = notification.state == NotificationState.READ
    var expanded by remember { mutableStateOf(false) }

    val cardContainer = MaterialTheme.colorScheme.surface
    val cardElevation = if (isRead) 0.dp else 2.dp
    val cardBorder = if (isRead) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
    val titleColor = if (isRead) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
    val iconTint = if (isRead) MaterialTheme.colorScheme.onSurfaceVariant else style.color
    val iconBackground = if (isRead) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f) else style.background

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (!expanded) Modifier.height(80.dp) else Modifier)
            .animateContentSize()
            .clickable { expanded = !expanded },
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
                if (expanded) {
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
                                    imageVector = if (isRead) Icons.Default.CheckCircle else Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = if (isRead) MaterialTheme.colorScheme.onSurfaceVariant else style.color,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                } else {
                    Text(
                        text = notification.message ?: "Sin mensaje",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = titleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Ver más",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onDiscard,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Descartar notificación",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!isRead) {
                    IconButton(
                        onClick = onMarkAsRead,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Marcar leído",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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

@Composable
fun notificationStyle(notification: NotificationDTO): NotificationVisualStyle {
    return when (notification.type) {
        NotificationType.WARNING -> NotificationVisualStyle(
            icon = Icons.Default.Warning,
            color = MaterialTheme.colorScheme.tertiary,
            background = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f)
        )

        NotificationType.SUCCESS -> NotificationVisualStyle(
            icon = Icons.Default.CheckCircle,
            color = IncomeGreen,
            background = IncomeGreen.copy(alpha = 0.12f)
        )

        NotificationType.ERROR -> NotificationVisualStyle(
            icon = Icons.Default.Error,
            color = ExpenseRed,
            background = ExpenseRed.copy(alpha = 0.12f)
        )

        NotificationType.INFO, null -> NotificationVisualStyle(
            icon = Icons.Default.Info,
            color = BrownPrimary,
            background = BrownSecondary.copy(alpha = 0.14f)
        )
    }
}

fun notificationStateText(state: NotificationState?): String {
    return when (state) {
        NotificationState.SENT -> "Nueva"
        NotificationState.READ -> "Leída"
        NotificationState.SCHEDULED -> "Nueva"
        NotificationState.DISCARD -> "Descartada"
        null -> "Sin estado"
    }
}

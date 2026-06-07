package com.example.finanx.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.viewmodel.NotificationViewModel
import org.openapitools.client.models.NotificationDTO

@Composable
// Pantalla principal de notificaciones.
// Permite ver todas las notificaciones, limpiar las leídas y mostrarlas ordenadas por fecha.
fun NotificacionesScreen(
    userId: Long,
    viewModel: NotificationViewModel
) {
    val state = viewModel.notificationState

    LaunchedEffect(userId) {
        viewModel.fetchAll(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
            .padding(16.dp)
    ) {
        Text(
            text = "Notificaciones",
            style = MaterialTheme.typography.headlineSmall,
            color = BluePrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.fetchAll(userId) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Ver todo")
            }

            OutlinedButton(
                onClick = { viewModel.clearRead(userId) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpiar leídos")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.error != null -> {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            state.notifications.isEmpty() -> {
                Text("No hay notificaciones disponibles.")
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.notifications) { notification ->
                        NotificationCard(notification)
                    }
                }
            }
        }
    }
}

@Composable
// Componente visual para mostrar una notificación individual.
fun NotificationCard(notification: NotificationDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = notification.type?.value ?: "INFO",
                style = MaterialTheme.typography.labelMedium,
                color = BluePrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = notification.message ?: "Sin mensaje",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Fecha: ${notification.createdAt?.toString()?.replace("T", " ") ?: "Sin fecha"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Text(
                text = "Estado: ${notification.state?.value ?: "Sin estado"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
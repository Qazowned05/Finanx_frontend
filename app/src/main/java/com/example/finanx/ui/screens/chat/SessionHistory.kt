package com.example.finanx.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.theme.BorderSoft
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.StoneGray
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.theme.TextLight
import org.openapitools.client.models.ChatSessionResponse
import kotlin.collections.orEmpty


@Composable
fun SessionHistoryView(
    session: ChatSessionResponse,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header read-only
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrownPrimary)
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextLight
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Conversación anterior",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextLight,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = sessionStateLabel(session.state),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextLight.copy(alpha = 0.7f)
                )
            }
        }

        // Aviso read-only
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            color = BorderSoft
        ) {
            Text(
                text = "Esta sesión está cerrada. Solo puedes visualizar el historial.",
                style = MaterialTheme.typography.labelSmall,
                color = StoneGray,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        // Historial de mensajes
        val messages = session.history.orEmpty()
        if (messages.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Sin mensajes en esta sesión.", color = TextGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(message = msg)
                }
            }
        }
    }
}

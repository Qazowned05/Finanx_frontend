package com.example.finanx.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.screens.analytics.components.SectionTitle
import org.openapitools.client.models.UserResponse


@Composable
fun ProfileMenuSection(
    user: UserResponse,
    onEditName: () -> Unit,
    onChangePassword: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            SectionTitle("Mi Perfil")
            ProfileMenuItem(
                icon = Icons.Default.Edit,
                title = "Cambiar Nombre",
                subtitle = user.name ?: "",
                onClick = onEditName
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
            ProfileMenuItem(
                icon = Icons.Default.Lock,
                title = "Cambiar Contraseña",
                subtitle = "••••••••",
                onClick = onChangePassword
            )
        }
    }
}
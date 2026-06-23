package com.example.finanx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.theme.TextLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanxTopAppBar(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    onNotifications: (() -> Unit)? = null,
    onProfile: () -> Unit,
    onLogout: () -> Unit,
    onRefresh: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextLight
                )
                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextLight.copy(alpha = 0.7f)
                    )
                }
            }
        },
        navigationIcon = {
            onBack?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = TextLight
                    )
                }
            }
        },
        actions = {
            onRefresh?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar",
                        tint = GoldAccent
                    )
                }
            }

            onNotifications?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = TextLight
                    )
                }
            }

            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = TextLight
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Mi Perfil") },
                    onClick = {
                        expanded = false
                        onProfile()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Cerrar Sesión") },
                    onClick = {
                        expanded = false
                        onLogout()
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BrownPrimary,
            titleContentColor = TextLight,
            actionIconContentColor = TextLight
        )
    )
}

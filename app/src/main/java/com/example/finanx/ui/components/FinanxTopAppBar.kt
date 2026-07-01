package com.example.finanx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.theme.TextLight

/**
 * TopAppBar compartido por todas las screens a través del Scaffold principal de AppNavigation.
 * Las screens NO instancian su propio Scaffold ni su propio TopAppBar.
 *
 * Orden de acciones: Refresh? | Finn | Notificaciones | Perfil(dropdown)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanxTopAppBar(
    title: String,
    subtitle: String? = null,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    onRefresh: (() -> Unit)? = null,
    onFinn: () -> Unit,
    onNotifications: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    var profileMenuExpanded by remember { mutableStateOf(false) }

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
            if (showBack) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = TextLight
                    )
                }
            }
        },
        actions = {
            // Refresh opcional — solo pantallas que lo necesiten
            onRefresh?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar",
                        tint = GoldAccent
                    )
                }
            }

            // Finn — siempre visible, igual que Copilot en IntelliJ
            IconButton(onClick = onFinn) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Abrir Finn",
                    tint = GoldAccent
                )
            }

            // Notificaciones
            IconButton(onClick = onNotifications) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = TextLight
                )
            }

            // Perfil con dropdown
            IconButton(onClick = { profileMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = TextLight
                )
            }
            DropdownMenu(
                expanded = profileMenuExpanded,
                onDismissRequest = { profileMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Mi Perfil") },
                    onClick = {
                        profileMenuExpanded = false
                        onProfile()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Cerrar Sesión") },
                    onClick = {
                        profileMenuExpanded = false
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
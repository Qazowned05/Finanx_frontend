package com.example.finanx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    notificationCount: Int = 0
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
            onRefresh?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            IconButton(onClick = onFinn) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Abrir Finn",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
            BadgedBox(
                badge = {
                    if (notificationCount > 0) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ) {
                            Text(
                                text = notificationCount.toString(),
                                fontSize = 10.sp,
                                modifier = Modifier.scale(0.9f)
                            )
                        }
                    }
                }
            ) {
                IconButton(onClick = onNotifications) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            IconButton(onClick = { profileMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = MaterialTheme.colorScheme.onPrimary
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
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

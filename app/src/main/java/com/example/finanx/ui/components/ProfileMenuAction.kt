package com.example.finanx.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color


@Composable
fun ProfileMenuAction(
    onMyProfile: () -> Unit,
    onLogout: () -> Unit,
    iconTint: Color = LocalContentColor.current
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Perfil",
            tint = iconTint
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Mi Perfil") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            onClick = {
                expanded = false
                onMyProfile()
            }
        )
        DropdownMenuItem(
            text = { Text("Cerrar Sesión") },
            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
            onClick = {
                expanded = false
                onLogout()
            }
        )
    }
}

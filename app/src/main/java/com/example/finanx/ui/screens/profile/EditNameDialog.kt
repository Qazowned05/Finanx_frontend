package com.example.finanx.ui.screens.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.FinanxEditDialog
import com.example.finanx.ui.viewmodel.UserActionState
import org.openapitools.client.models.UserResponse


@Composable
fun EditNameDialog(
    user: UserResponse,
    state: UserActionState,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    onReset: () -> Unit
) {
    var newName by remember { mutableStateOf(user.name ?: "") }

    LaunchedEffect(state) {
        if (state is UserActionState.Success) onReset()
    }

    FinanxEditDialog(
        title = "Cambiar Nombre",
        confirmText = "Guardar",
        onConfirm = {
            if (newName.isNotBlank()) {
                onConfirm(newName.trim())
            }
        },
        onDismiss = onDismiss,
        enabled = newName.isNotBlank() && state !is UserActionState.Loading,
        content = {
            if (state is UserActionState.Error) {
                Text(state.message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Nuevo nombre") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )

            if (state is UserActionState.Loading) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

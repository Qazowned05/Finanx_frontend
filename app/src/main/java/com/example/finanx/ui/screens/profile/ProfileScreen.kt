package com.example.finanx.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.finanx.ui.components.FinanxCard
import com.example.finanx.ui.components.FinanxConfirmDialog
import com.example.finanx.ui.components.FinanxEditDialog
import com.example.finanx.ui.screens.analytics.components.SectionTitle
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.viewmodel.UserActionState
import com.example.finanx.ui.viewmodel.UserViewModel
import org.openapitools.client.models.UserResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: UserResponse,
    userId: Long,
    viewModel: UserViewModel,
    onOpenResetData: () -> Unit,
    onOpenDeleteAccount: () -> Unit,
    onLogout: () -> Unit
) {
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }
    var showDetailsSection by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeader(user)
        ProfileMenuSection(
            user = user,
            onEditName = { showEditNameDialog = true },
            onChangePassword = { showChangePasswordDialog = true }
        )
        AccountDetailsSection(user, showDetailsSection, onToggle = { showDetailsSection = !showDetailsSection })
        DangerSection(
            onDeactivate = { showDeactivateDialog = true },
            onResetData = onOpenResetData,
            onDeleteAccount = onOpenDeleteAccount
        )
    }

    if (showEditNameDialog) {
        EditNameDialog(
            user = user,
            state = viewModel.updateNameState,
            onConfirm = { newName ->
                viewModel.updateName(userId, newName, user)
            },
            onDismiss = {
                showEditNameDialog = false
                viewModel.resetUpdateNameState()
            },
            onReset = { viewModel.resetUpdateNameState() }
        )
    }

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            state = viewModel.updatePasswordState,
            onConfirm = { newPassword ->
                viewModel.updatePassword(userId, newPassword, user)
            },
            onDismiss = {
                showChangePasswordDialog = false
                viewModel.resetUpdatePasswordState()
            },
            onReset = { viewModel.resetUpdatePasswordState() }
        )
    }

    if (showDeactivateDialog) {
        FinanxConfirmDialog(
            title = "Deshabilitar Cuenta",
            message = "¿Estás seguro de deshabilitar tu cuenta? Podrás reactivarla después contactando con el administrador.",
            confirmText = "Deshabilitar",
            isDanger = true,
            onConfirm = {
                viewModel.deactivateAccount(userId)
                showDeactivateDialog = false
            },
            onDismiss = { showDeactivateDialog = false }
        )
    }

    when (val deactivateState = viewModel.deactivateState) {
        is UserActionState.Success -> {
            AlertDialog(
                onDismissRequest = { viewModel.resetDeactivateState() },
                title = { Text("Cuenta Deshabilitada", fontWeight = FontWeight.Bold) },
                text = { Text(deactivateState.message) },
                confirmButton = {
                    TextButton(onClick = { viewModel.resetDeactivateState() }) {
                        Text("Aceptar")
                    }
                }
            )
        }
        is UserActionState.Error -> {
            AlertDialog(
                onDismissRequest = { viewModel.resetDeactivateState() },
                title = { Text("Error", fontWeight = FontWeight.Bold) },
                text = { Text(deactivateState.message) },
                confirmButton = {
                    TextButton(onClick = { viewModel.resetDeactivateState() }) {
                        Text("Aceptar")
                    }
                }
            )
        }
        else -> {}
    }
}

@Composable
private fun DangerSection(
    onDeactivate: () -> Unit,
    onResetData: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    FinanxCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = null
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            SectionTitle("Zona de Peligro", isDanger = true)
            Surface(
                onClick = onDeactivate,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Deshabilitar Cuenta", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.error)
                        Text("Desactivar mi cuenta temporalmente", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
            Surface(
                onClick = onResetData,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Empezar de Cero", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.error)
                        Text("Borrar todos mis datos y reiniciar", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
            Surface(
                onClick = onDeleteAccount,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Eliminar mi Cuenta", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.error)
                        Text("Borrar cuenta y datos de forma permanente", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

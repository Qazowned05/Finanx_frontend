package com.example.finanx.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.components.FinanxConfirmDialog
import com.example.finanx.ui.components.FinanxEditDialog
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.util.toReadableDate
import com.example.finanx.ui.viewmodel.UserActionState
import com.example.finanx.ui.viewmodel.UserViewModel
import org.openapitools.client.models.UserResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: UserResponse,
    userId: Long,
    viewModel: UserViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }
    var showDetailsSection by remember { mutableStateOf(false) }
    var showResetDataScreen by remember { mutableStateOf(false) }
    var showDeleteAccountScreen by remember { mutableStateOf(false) }

    // Navegar a sub-screens de peligro
    if (showResetDataScreen) {
        ResetDataScreen(
            userId = userId,
            viewModel = viewModel,
            onBack = { showResetDataScreen = false },
            onLogout = onLogout
        )
        return
    }

    if (showDeleteAccountScreen) {
        DeleteAccountScreen(
            userId = userId,
            viewModel = viewModel,
            onBack = { showDeleteAccountScreen = false },
            onLogout = onLogout
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = LightBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
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
                onResetData = { showResetDataScreen = true },
                onDeleteAccount = { showDeleteAccountScreen = true }
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
}

@Composable
private fun DangerSection(
    onDeactivate: () -> Unit,
    onResetData: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            SectionTitle("Zona de Peligro", isDanger = true)
            Surface(
                onClick = onDeactivate,
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = ExpenseRed, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Deshabilitar Cuenta", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = ExpenseRed)
                        Text("Desactivar mi cuenta temporalmente", fontSize = 13.sp, color = TextGray)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = ExpenseRed)
                }
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = GrayBorder)
            Surface(
                onClick = onResetData,
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, tint = ExpenseRed, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Empezar de Cero", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = ExpenseRed)
                        Text("Borrar todos mis datos y reiniciar", fontSize = 13.sp, color = TextGray)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = ExpenseRed)
                }
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = GrayBorder)
            Surface(
                onClick = onDeleteAccount,
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null, tint = ExpenseRed, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Eliminar mi Cuenta", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = ExpenseRed)
                        Text("Borrar cuenta y datos de forma permanente", fontSize = 13.sp, color = TextGray)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = ExpenseRed)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, isDanger: Boolean = false) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = if (isDanger) ExpenseRed else TextGray,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
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
import com.example.finanx.ui.viewmodel.UserActionState
import com.example.finanx.ui.viewmodel.UserViewModel
import org.openapitools.client.models.UserResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: UserResponse,
    userId: Long,
    viewModel: UserViewModel,
    onBack: () -> Unit
) {
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }
    var showDetailsSection by remember { mutableStateOf(false) }

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
                    DangerSection(onDeactivate = { showDeactivateDialog = true })
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
private fun ProfileHeader(user: UserResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(BluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (user.name?.firstOrNull()?.uppercase() ?: "U"),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = user.fullName ?: user.name ?: "Usuario",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BluePrimary
            )
            Text(
                text = user.email ?: "",
                fontSize = 14.sp,
                color = TextGray
            )
        }
    }
}

@Composable
private fun ProfileMenuSection(
    user: UserResponse,
    onEditName: () -> Unit,
    onChangePassword: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = GrayBorder)
            ProfileMenuItem(
                icon = Icons.Default.Lock,
                title = "Cambiar Contraseña",
                subtitle = "••••••••",
                onClick = onChangePassword
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = BlueSecondary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextDark)
                Text(subtitle, fontSize = 13.sp, color = TextGray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray)
        }
    }
}

@Composable
private fun AccountDetailsSection(
    user: UserResponse,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Surface(
                onClick = onToggle,
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = BlueSecondary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Ver otros datos", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextDark, modifier = Modifier.weight(1f))
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextGray
                    )
                }
            }
            if (expanded) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = GrayBorder)
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    DetailRow("ID", user.id?.toString() ?: "-")
                    DetailRow("Usuario", user.username ?: "-")
                    DetailRow("Rol", user.role?.value ?: "-")
                    DetailRow("Estado", user.state?.value ?: "-")
                    DetailRow("Balance", "PEN ${user.moneyBalance ?: "0.00"}")
                    DetailRow("Zona Horaria", user.timeZone?.value?.replace("_", " ")?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "-")
                    DetailRow("Creado", user.createdAt?.toString()?.take(10) ?: "-")
                }
            }
        }
    }
}

@Composable
private fun DangerSection(onDeactivate: () -> Unit) {
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
                    Icon(Icons.Default.Delete, contentDescription = null, tint = ExpenseRed, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Deshabilitar Cuenta", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = ExpenseRed)
                        Text("Desactivar mi cuenta temporalmente", fontSize = 13.sp, color = TextGray)
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

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = TextGray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextDark)
    }
}

@Composable
private fun EditNameDialog(
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
                Text(state.message, color = ExpenseRed, fontSize = 14.sp)
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
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = BluePrimary)
            }
        }
    )
}

@Composable
private fun ChangePasswordDialog(
    state: UserActionState,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    onReset: () -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is UserActionState.Success) onReset()
    }

    val passwordsMatch = newPassword == confirmPassword
    val isValid = newPassword.isNotBlank() && confirmPassword.isNotBlank() && passwordsMatch

    FinanxEditDialog(
        title = "Cambiar Contraseña",
        confirmText = "Guardar",
        onConfirm = {
            if (isValid) {
                onConfirm(newPassword)
            }
        },
        onDismiss = onDismiss,
        enabled = isValid && state !is UserActionState.Loading,
        content = {
            if (state is UserActionState.Error) {
                Text(state.message, color = ExpenseRed, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nueva contraseña") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                isError = confirmPassword.isNotEmpty() && !passwordsMatch
            )

            if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                Text("Las contraseñas no coinciden", color = ExpenseRed, fontSize = 12.sp)
            }

            if (state is UserActionState.Loading) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = BluePrimary)
            }
        }
    )
}

package com.elias.mismonedas.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.elias.mismonedas.data.auth.SecurityStore
import com.elias.mismonedas.ui.components.FinanxCard
import com.elias.mismonedas.ui.components.FinanxConfirmDialog
import com.elias.mismonedas.ui.screens.analytics.components.SectionTitle
import com.elias.mismonedas.ui.viewmodel.UserActionState
import com.elias.mismonedas.ui.viewmodel.UserViewModel
import org.openapitools.client.models.UserPreferencesRequest
import org.openapitools.client.models.UserPreferencesResponse
import org.openapitools.client.models.UserResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: UserResponse,
    userId: Long,
    viewModel: UserViewModel,
    onOpenResetData: () -> Unit,
    onOpenDeleteAccount: () -> Unit,
    onOpenSubscription: () -> Unit,
    onLogout: () -> Unit
) {
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }
    var showDetailsSection by remember { mutableStateOf(false) }
    var showPreferencesDialog by remember { mutableStateOf(false) }
    var showSetupPinDialog by remember { mutableStateOf(false) }
    var showChangePinDialog by remember { mutableStateOf(false) }
    var showDisablePinDialog by remember { mutableStateOf(false) }
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) { viewModel.loadPreferences(userId) }
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
        SubscriptionSection(onOpenSubscription = onOpenSubscription)
        PreferencesSection(
            preferences = viewModel.preferences,
            onClick = { showPreferencesDialog = true }
        )
        SecuritySection(
            hasPin = SecurityStore.isLockEnabledForUser(userId),
            onSetupPin = { showSetupPinDialog = true },
            onChangePin = { showChangePinDialog = true },
            onDisablePin = { showDisablePinDialog = true }
        )
        AccountDetailsSection(user, showDetailsSection, onToggle = { showDetailsSection = !showDetailsSection })
        // TODO: DangerSection temporalmente deshabilitada — reactivar cuando se defina el flujo de seguridad
        // DangerSection(
        //     onDeactivate = { showDeactivateDialog = true },
        //     onResetData = onOpenResetData,
        //     onDeleteAccount = onOpenDeleteAccount
        // )
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

    if (showPreferencesDialog) {
        EditPreferencesDialog(
            current = viewModel.preferences,
            onConfirm = { currency, timeZone, emailNotif ->
                viewModel.updatePreferences(userId, currency, timeZone, emailNotif)
                showPreferencesDialog = false
            },
            onDismiss = { showPreferencesDialog = false }
        )
    }

    if (showSetupPinDialog) {
        PinSetupDialog(
            title = "Configurar PIN",
            onConfirm = { pin ->
                SecurityStore.setPin(pin, userId)
                showSetupPinDialog = false
            },
            onDismiss = { showSetupPinDialog = false }
        )
    }

    if (showChangePinDialog) {
        PinSetupDialog(
            title = "Cambiar PIN",
            onConfirm = { pin ->
                SecurityStore.setPin(pin, userId)
                showChangePinDialog = false
            },
            onDismiss = { showChangePinDialog = false }
        )
    }

    if (showDisablePinDialog) {
        FinanxConfirmDialog(
            title = "Desactivar bloqueo",
            message = "¿Estás seguro de desactivar el bloqueo de pantalla?",
            confirmText = "Desactivar",
            isDanger = true,
            onConfirm = {
                SecurityStore.clearPin()
                showDisablePinDialog = false
            },
            onDismiss = { showDisablePinDialog = false }
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

@Composable
private fun PreferencesSection(
    preferences: UserPreferencesResponse?,
    onClick: () -> Unit
) {
    FinanxCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = null
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            SectionTitle("Preferencias")

            // Moneda
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AttachMoney, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Moneda", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                        Text(preferences?.currencyCode ?: "—", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

            // Zona Horaria
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Zona Horaria", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                        val tz = preferences?.timeZone?.value?.replace("_", " ")?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "—"
                        Text(tz, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

            // Notificaciones por email
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Notificaciones por email", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                        Text(
                            if (preferences?.emailNotifications == true) "Activadas" else "Desactivadas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

            // Botón editar
            Surface(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Editar preferencias", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun SubscriptionSection(onOpenSubscription: () -> Unit) {
    FinanxCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = null
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            SectionTitle("Suscripción")
            Surface(
                onClick = onOpenSubscription,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Gestionar suscripción", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                        Text("Ver plan, renovación y beneficios", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun SecuritySection(
    hasPin: Boolean,
    onSetupPin: () -> Unit,
    onChangePin: () -> Unit,
    onDisablePin: () -> Unit
) {
    FinanxCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = null
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            SectionTitle("Seguridad")
            Surface(
                onClick = if (hasPin) onChangePin else onSetupPin,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Bloqueo de pantalla", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                        Text(if (hasPin) "PIN configurado" else "Proteger la app con PIN", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (hasPin) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                Surface(
                    onClick = onDisablePin,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LockOpen, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Desactivar PIN", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.error)
                            Text("Eliminar bloqueo de pantalla", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
private fun PinSetupDialog(
    title: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (step == 0) {
                    Text("Ingresa un PIN de 4 a 6 dígitos", style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { if (it.length <= 6) { pin = it; error = null } },
                        label = { Text("Nuevo PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        isError = error != null,
                        supportingText = error?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text("Confirma tu PIN", style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(
                        value = confirmPin,
                        onValueChange = { if (it.length <= 6) { confirmPin = it; error = null } },
                        label = { Text("Confirmar PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        isError = error != null,
                        supportingText = error?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (step == 0) {
                    if (pin.length < 4) {
                        error = "El PIN debe tener al menos 4 dígitos"
                    } else {
                        step = 1
                    }
                } else {
                    if (pin == confirmPin) {
                        onConfirm(pin)
                    } else {
                        error = "Los PIN no coinciden"
                        confirmPin = ""
                    }
                }
            }) {
                Text(if (step == 0) "Siguiente" else "Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                if (step == 1) step = 0 else onDismiss()
            }) { Text(if (step == 1) "Atrás" else "Cancelar") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPreferencesDialog(
    current: UserPreferencesResponse?,
    onConfirm: (currencyCode: String, timeZone: UserPreferencesRequest.TimeZone, emailNotifications: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val timeZones = UserPreferencesRequest.TimeZone.values()
    var selectedCurrency by remember { mutableStateOf(current?.currencyCode ?: "PEN") }
    var selectedTimeZone by remember {
        val initial = current?.timeZone?.name?.let {
            try { UserPreferencesRequest.TimeZone.valueOf(it) } catch (_: Exception) { null }
        } ?: UserPreferencesRequest.TimeZone.AMERICA_LIMA
        mutableStateOf(initial)
    }
    var expandedTimeZone by remember { mutableStateOf(false) }
    var emailNotifications by remember { mutableStateOf(current?.emailNotifications ?: true) }
    fun displayName(zone: UserPreferencesRequest.TimeZone): String =
        zone.value.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Preferencias", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Moneda predeterminada", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = selectedCurrency,
                    onValueChange = { selectedCurrency = it },
                    label = { Text("Código de moneda") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Zona Horaria", style = MaterialTheme.typography.bodyMedium)
                ExposedDropdownMenuBox(
                    expanded = expandedTimeZone,
                    onExpandedChange = { expandedTimeZone = it }
                ) {
                    OutlinedTextField(
                        value = displayName(selectedTimeZone),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTimeZone) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTimeZone,
                        onDismissRequest = { expandedTimeZone = false }
                    ) {
                        timeZones.forEach { zone ->
                            DropdownMenuItem(
                                text = { Text(displayName(zone)) },
                                onClick = {
                                    selectedTimeZone = zone
                                    expandedTimeZone = false
                                }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Notificaciones por email", modifier = Modifier.weight(1f))
                    Switch(checked = emailNotifications, onCheckedChange = { emailNotifications = it })
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedCurrency, selectedTimeZone, emailNotifications) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

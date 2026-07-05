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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.viewmodel.UserActionState
import com.example.finanx.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetDataScreen(
    userId: Long,
    viewModel: UserViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    val resetState = viewModel.resetDataState

    LaunchedEffect(resetState) {
        if (resetState is UserActionState.Success) {
            viewModel.resetResetDataState()
            onLogout()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            // Hero warning
            DangerHeroCard(
                icon = Icons.Default.RestartAlt,
                title = "¿Seguro que quieres empezar de cero?",
                subtitle = "Esta acción borrará permanentemente toda tu información financiera.",
                iconBackgroundColor = Color(0xFFFFF3E0),
                iconTint = Color(0xFFE65100)
            )

            // Qué se borra
            DangerConsequencesCard(
                title = "Esto es lo que se eliminará",
                items = listOf(
                    Icons.Default.SwapVert to "Todos tus movimientos e ingresos registrados",
                    Icons.Default.AccountBalanceWallet to "Tus presupuestos activos e históricos",
                    Icons.Default.Savings to "Todas tus metas de ahorro",
                    Icons.Default.Category to "Las categorías personalizadas que creaste",
                    Icons.Default.Schedule to "Tus programaciones de transacciones y presupuestos",
                    Icons.Default.Notifications to "Todas las notificaciones"
                )
            )

            // Qué pasa después
            DangerAfterCard(
                title = "¿Qué pasa después?",
                items = listOf(
                    "Tu cuenta se mantiene activa, solo se limpian los datos.",
                    "Se restaurarán las categorías por defecto para que puedas empezar de inmediato.",
                    "Por seguridad, se cerrará tu sesión automáticamente al confirmar."
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Error si falla
            if (resetState is UserActionState.Error) {
                Text(
                    text = resetState.message,
                    color = ExpenseRed,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // CTA
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE65100),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                enabled = resetState !is UserActionState.Loading
            ) {
                if (resetState is UserActionState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.RestartAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Quiero Empezar de Cero", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar, mantener mis datos", color = TextGray)
        }
    }

    if (showConfirmDialog) {
        DangerConfirmDialog(
            title = "Confirmación final",
            message = "Se borrarán todos tus datos financieros y se cerrará tu sesión. Esta acción no se puede deshacer.",
            confirmText = "Sí, borrar todo",
            onConfirm = {
                showConfirmDialog = false
                viewModel.resetData(userId)
            },
            onDismiss = { showConfirmDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(
    userId: Long,
    viewModel: UserViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    val deleteState = viewModel.deleteAccountState

    LaunchedEffect(deleteState) {
        if (deleteState is UserActionState.Success) {
            viewModel.resetDeleteAccountState()
            onLogout()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            // Hero warning
            DangerHeroCard(
                icon = Icons.Default.DeleteForever,
                title = "Esta acción es permanente",
                subtitle = "Tu cuenta y todos tus datos serán eliminados para siempre. No hay vuelta atrás.",
                iconBackgroundColor = Color(0xFFFFEBEE),
                iconTint = ExpenseRed
            )

            // Qué se borra
            DangerConsequencesCard(
                title = "Se eliminará de forma definitiva",
                items = listOf(
                    Icons.Default.Person to "Tu cuenta y credenciales de acceso",
                    Icons.Default.SwapVert to "Todos tus movimientos e historial financiero",
                    Icons.Default.AccountBalanceWallet to "Presupuestos y metas de ahorro",
                    Icons.Default.Category to "Todas tus categorías personalizadas",
                    Icons.Default.Schedule to "Programaciones y automatizaciones",
                    Icons.Default.Notifications to "Notificaciones y configuraciones"
                )
            )

            // Qué pasa después
            DangerAfterCard(
                title = "Antes de continuar, ten en cuenta que...",
                items = listOf(
                    "No podrás recuperar tu cuenta ni tus datos una vez eliminados.",
                    "Tampoco podrás usar el mismo correo electrónico para registrarte de nuevo.",
                    "Se cerrará tu sesión inmediatamente al confirmar."
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Error si falla
            if (deleteState is UserActionState.Error) {
                Text(
                    text = deleteState.message,
                    color = ExpenseRed,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // CTA
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ExpenseRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                enabled = deleteState !is UserActionState.Loading
            ) {
                if (deleteState is UserActionState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.DeleteForever, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eliminar mi cuenta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar, conservar mi cuenta", color = TextGray)
        }
    }

    if (showConfirmDialog) {
        DangerConfirmDialog(
            title = "Eliminar cuenta definitivamente",
            message = "Esta acción no se puede deshacer. Tu cuenta y todos tus datos serán eliminados para siempre.",
            confirmText = "Sí, eliminar mi cuenta",
            onConfirm = {
                showConfirmDialog = false
                viewModel.deleteAccount(userId)
            },
            onDismiss = { showConfirmDialog = false }
        )
    }
}

@Composable
private fun DangerHeroCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconBackgroundColor: Color,
    iconTint: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(iconBackgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = TextGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DangerConsequencesCard(
    title: String,
    items: List<Pair<ImageVector, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            items.forEach { (icon, text) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = ExpenseRed.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(text, fontSize = 14.sp, color = TextDark)
                }
            }
        }
    }
}

@Composable
private fun DangerAfterCard(
    title: String,
    items: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F0)),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGray
                )
            }
            items.forEachIndexed { _, text ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text, fontSize = 13.sp, color = TextDark, lineHeight = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun DangerConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        icon = {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = ExpenseRed,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 17.sp, textAlign = TextAlign.Center)
        },
        text = {
            Text(message, fontSize = 14.sp, color = TextGray, textAlign = TextAlign.Center)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(confirmText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextGray)
            }
        }
    )
}

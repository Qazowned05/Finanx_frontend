package com.example.finanx.ui.screens.saving

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.components.ProfileMenuAction
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.SavingGoalState
import com.example.finanx.ui.viewmodel.SavingGoalViewModel
import org.openapitools.client.models.SavingGoalResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Star


val goalColors = listOf(
    Color(0xFF6C63FF), Color(0xFFFF6584), Color(0xFF43C6AC),
    Color(0xFFF7971E), Color(0xFF4776E6), Color(0xFFFF5F6D),
    Color(0xFF11998E), Color(0xFFFC5C7D), Color(0xFF6A82FB)
)

val goalIcons = listOf(
    Icons.Default.DirectionsCar, Icons.Default.Home, Icons.Default.Flight,
    Icons.Default.School, Icons.Default.HealthAndSafety, Icons.Default.ShoppingCart,
    Icons.Default.Savings, Icons.Default.Star, Icons.Default.Category
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingGoalScreen(
    userId: Long,
    viewModel: SavingGoalViewModel,
    authViewModel: AuthViewModel
) {
    LaunchedEffect(userId) {
        viewModel.fetchSavingGoals(userId)
    }

    val state = viewModel.savingGoalState
    val context = LocalContext.current
    val activity = context as? Activity

    var showCreateDialog by remember { mutableStateOf(false) }
    var goalToEdit by remember { mutableStateOf<SavingGoalResponse?>(null) }
    var goalToDelete by remember { mutableStateOf<SavingGoalResponse?>(null) }
    var goalSelected by remember { mutableStateOf<SavingGoalResponse?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val actionSuccess = viewModel.actionSuccess
    LaunchedEffect(actionSuccess) {
        if (actionSuccess != null) {
            snackbarHostState.showSnackbar(actionSuccess)
            viewModel.clearActionSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFEEF2FF),
        topBar = {
            TopAppBar(
                title = { Text("Metas de Ahorro", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6B8FD6)
                ),
                actions = {
                    ProfileMenuAction(
                        onMyProfile = { },
                        onLogout = { authViewModel.logout(activity) }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = BluePrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva meta", tint = Color.White)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (state) {
                is SavingGoalState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SavingGoalState.Success -> {
                    if (state.goals.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Savings,
                                contentDescription = null,
                                tint = Color(0xFFCBD5E0),
                                modifier = Modifier.size(64.dp)
                            )
                            Text("Sin metas de ahorro", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Toca + para agregar una", color = Color.Gray, fontSize = 13.sp)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(count = state.goals.size) { index ->
                                val goal = state.goals[index]
                                SavingGoalCard(
                                    goal = goal,
                                    colorIndex = index % goalColors.size,
                                    iconIndex = index % goalIcons.size,
                                    isSelected = goalSelected?.id == goal.id,
                                    onLongPress = { goalSelected = goal },
                                    onDismiss = { goalSelected = null }
                                )
                            }
                        }
                    }
                }
                is SavingGoalState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
            }
        }
    }

    // Menu de opciones al mantener presionado
    goalSelected?.let { goal ->
        AlertDialog(
            onDismissRequest = { goalSelected = null },
            title = { Text(goal.description.orEmpty(), fontWeight = FontWeight.Bold) },
            text = { Text("¿Qué deseas hacer con esta meta?") },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            goalToDelete = goal
                            goalSelected = null
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Eliminar")
                    }
                    Button(
                        onClick = {
                            goalToEdit = goal
                            goalSelected = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { goalSelected = null }) { Text("Cancelar") }
            }
        )
    }

    // Dialogo de confirmacion de eliminacion
    goalToDelete?.let { goal ->
        AlertDialog(
            onDismissRequest = { goalToDelete = null },
            containerColor = Color.White,
            title = {
                Text(
                    "¿Eliminar esta meta?",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            text = {
                Text(
                    "Estás a punto de eliminar \"${goal.description.orEmpty()}\". " +
                            "Esta acción no se puede deshacer y se perderán todos los registros " +
                            "de progreso y transacciones asociadas a esta meta.",
                    color = Color(0xFF4A5568)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSavingGoal(userId, goal.id!!)
                        goalToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Sí, eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { goalToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showCreateDialog) {
        SavingGoalFormDialog(
            title = "Nueva Meta de Ahorro",
            onConfirm = { description, amount, deadline ->
                viewModel.createSavingGoal(userId, description, amount, deadline)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }

    goalToEdit?.let { goal ->
        SavingGoalFormDialog(
            title = "Editar Meta",
            initialDescription = goal.description.orEmpty(),
            initialAmount = goal.targetAmount?.toPlainString().orEmpty(),
            initialDeadline = goal.deadline?.toLocalDate()?.toString().orEmpty(),
            onConfirm = { description, amount, deadline ->
                viewModel.updateSavingGoal(userId, goal.id!!, description, amount, deadline)
                goalToEdit = null
            },
            onDismiss = { goalToEdit = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavingGoalCard(
    goal: SavingGoalResponse,
    colorIndex: Int,
    iconIndex: Int,
    isSelected: Boolean,
    onLongPress: () -> Unit,
    onDismiss: () -> Unit
) {
    val accentColor = goalColors[colorIndex]
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFFFEBEB) else Color.White,
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { if (isSelected) onDismiss() },
                onLongClick = onLongPress
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFCDD2) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = goalIcons[iconIndex],
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        goal.description.orEmpty(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = if (isSelected) Color.Red else Color(0xFF1A202C)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Meta: PEN ${goal.targetAmount ?: 0}", color = Color.Gray, fontSize = 13.sp)
                }
                val stateValue = goal.state?.value
                Text(
                    stateValue.orEmpty(),
                    fontSize = 11.sp,
                    color = when (stateValue) {
                        "RUNNING" -> Color(0xFF2F855A)
                        "COMPLETED" -> BluePrimary
                        else -> Color.Gray
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Acumulado: PEN ${goal.accumulated ?: 0}", color = Color.Gray, fontSize = 12.sp)
                goal.deadline?.let {
                    Text("Hasta: ${it.toLocalDate()}", color = Color.Gray, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            val progress = (goal.progressPercentage ?: 0.0).toFloat() / 100f
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
                color = if (isSelected) Color.Red else accentColor,
                trackColor = accentColor.copy(alpha = 0.15f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${goal.progressPercentage?.toInt() ?: 0}% completado",
                fontSize = 12.sp,
                color = if (isSelected) Color.Red else accentColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingGoalFormDialog(
    title: String,
    initialDescription: String = "",
    initialAmount: String = "",
    initialDeadline: String = "",
    onConfirm: (String, BigDecimal, LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    var description by remember { mutableStateOf(initialDescription) }
    var amount by remember { mutableStateOf(initialAmount) }
    var amountError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember {
        mutableStateOf<LocalDate?>(
            if (initialDeadline.isNotBlank()) LocalDate.parse(initialDeadline) else null
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it; amountError = false },
                    label = { Text("Monto objetivo (PEN)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = amountError,
                    supportingText = { if (amountError) Text("Ingresa un monto válido") }
                )
                OutlinedTextField(
                    value = selectedDate?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Fecha límite") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedAmount = amount.toBigDecimalOrNull()
                    if (description.isNotBlank() && parsedAmount != null) {
                        val deadline: LocalDate? = selectedDate
                        onConfirm(description, parsedAmount, deadline)
                    } else {
                        amountError = parsedAmount == null
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}


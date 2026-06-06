package com.example.finanx.ui.screens.saving

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import java.time.OffsetDateTime

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
        topBar = {
            TopAppBar(
                title = { Text("Metas de Ahorro", fontWeight = FontWeight.Bold) },
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
                                SwipeableSavingGoalCard(
                                    goal = state.goals[index],
                                    onEdit = { goalToEdit = state.goals[index] },
                                    onDelete = { viewModel.deleteSavingGoal(userId, state.goals[index].id!!) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableSavingGoalCard(
    goal: SavingGoalResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> { onDelete(); false }
                SwipeToDismissBoxValue.StartToEnd -> { onEdit(); false }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.StartToEnd -> Color(0xFF3B82F6)
                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFEF4444)
                    else -> Color.Transparent
                }, label = ""
            )
            val alignment = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                else -> Alignment.Center
            }
            val icon = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                else -> Icons.Default.Edit
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
        }
    ) {
        SavingGoalCard(goal)
    }
}

@Composable
fun SavingGoalCard(goal: SavingGoalResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                goal.description.orEmpty(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF1A202C)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Meta: PEN ${goal.targetAmount ?: 0}", color = Color.Gray, fontSize = 13.sp)
            Text("Acumulado: PEN ${goal.accumulated ?: 0}", color = Color.Gray, fontSize = 13.sp)
            goal.deadline?.let {
                Text("Fecha límite: ${it.toLocalDate()}", color = Color.Gray, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            val progress = (goal.progressPercentage ?: 0.0).toFloat() / 100f
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
                color = BluePrimary,
                trackColor = Color(0xFFE2E8F0)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${goal.progressPercentage?.toInt() ?: 0}%", fontSize = 12.sp, color = BluePrimary)
                val state = goal.state?.value
                Text(
                    state.orEmpty(),
                    fontSize = 12.sp,
                    color = when (state) {
                        "RUNNING" -> Color(0xFF2F855A)
                        "COMPLETED" -> BluePrimary
                        else -> Color.Gray
                    }
                )
            }
        }
    }
}

@Composable
fun SavingGoalFormDialog(
    title: String,
    initialDescription: String = "",
    initialAmount: String = "",
    initialDeadline: String = "",
    onConfirm: (String, BigDecimal, OffsetDateTime?) -> Unit,
    onDismiss: () -> Unit
) {
    var description by remember { mutableStateOf(initialDescription) }
    var amount by remember { mutableStateOf(initialAmount) }
    var deadline by remember { mutableStateOf(initialDeadline) }
    var amountError by remember { mutableStateOf(false) }
    var deadlineError by remember { mutableStateOf(false) }

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
                    value = deadline,
                    onValueChange = { deadline = it; deadlineError = false },
                    label = { Text("Fecha límite (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = deadlineError,
                    supportingText = { if (deadlineError) Text("Formato: 2025-12-31") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedAmount = amount.toBigDecimalOrNull()
                    val parsedDeadline = try {
                        if (deadline.isBlank()) null
                        else java.time.LocalDate.parse(deadline)
                            .atStartOfDay()
                            .atOffset(java.time.ZoneOffset.UTC)
                    } catch (e: Exception) {
                        deadlineError = true
                        null
                    }
                    if (description.isNotBlank() && parsedAmount != null && !deadlineError) {
                        onConfirm(description, parsedAmount, parsedDeadline)
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

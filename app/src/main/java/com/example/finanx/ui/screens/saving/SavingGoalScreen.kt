package com.example.finanx.ui.screens.saving

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.DetailRow
import com.example.finanx.ui.components.EmptyStateCard
import com.example.finanx.ui.components.SpotlightOverlay
import com.example.finanx.ui.components.TutorialPreferences
import com.example.finanx.ui.components.tutorialTarget
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.SavingGoalState
import com.example.finanx.ui.viewmodel.SavingGoalViewModel
import com.example.finanx.ui.components.tutorial.TutorialController
import com.example.finanx.ui.components.tutorial.TutorialStep
import org.openapitools.client.models.SavingGoalResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val goalIcons = listOf(
    Icons.Default.DirectionsCar, Icons.Default.Home, Icons.Default.Flight,
    Icons.Default.School, Icons.Default.HealthAndSafety, Icons.Default.ShoppingCart,
    Icons.Default.Savings, Icons.Default.Star, Icons.Default.Category
)

private fun SavingGoalResponse.State?.toSavingGoalStateLabel(): String =
    when (this) {
        SavingGoalResponse.State.RUNNING -> "En progreso"
        SavingGoalResponse.State.COMPLETED -> "Completada"
        SavingGoalResponse.State.CANCELLED -> "Cancelada"
        null -> "Sin estado"
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingGoalScreen(
    userId: Long,
    viewModel: SavingGoalViewModel,
    onCreateGoal: () -> Unit
) {
    LaunchedEffect(userId) {
        viewModel.fetchSavingGoals(userId)
    }

    val state = viewModel.savingGoalState

    var goalToEdit by remember { mutableStateOf<SavingGoalResponse?>(null) }
    var goalToDelete by remember { mutableStateOf<SavingGoalResponse?>(null) }
    var goalSelected by remember { mutableStateOf<SavingGoalResponse?>(null) }
    var goalDetail by remember { mutableStateOf<SavingGoalResponse?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val targetBounds = remember { mutableStateMapOf<String, Rect>() }
    var tutorialController by remember { mutableStateOf<TutorialController?>(null) }

    LaunchedEffect(Unit) {
        val completed = TutorialPreferences.isCompleted(context, "tutorial_metas_ahorro").first()
        if (!completed) {
            tutorialController = TutorialController(
                listOf(
                    TutorialStep("new_goal_button", "Define cuánto quieres ahorrar y para cuándo."),
                    TutorialStep("goal_progress_bar", "Así ves qué tan cerca estás de cada meta.")
                )
            ) {
                coroutineScope.launch { TutorialPreferences.markCompleted(context, "tutorial_metas_ahorro") }
                tutorialController = null
            }
        }
    }


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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateGoal,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.tutorialTarget("new_goal_button", targetBounds)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva meta")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (state) {
                is SavingGoalState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
                }
                is SavingGoalState.Success -> {
                    if (state.goals.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp)
                        ) {
                            EmptyStateCard(
                                icon = Icons.Default.Savings,
                                title = "Sin metas de ahorro",
                                subtitle = "Toca + para agregar una",
                                actionLabel = "Nueva meta",
                                onAction = onCreateGoal
                            )
                        }
                    } else {
                        com.example.finanx.ui.components.RefreshableList(
                            isRefreshing = (state as? com.example.finanx.ui.viewmodel.SavingGoalState.Success)?.isRefreshing ?: false,
                            onRefresh = { viewModel.fetchSavingGoals(userId) },
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val groupedGoals = state.goals.groupBy { it.state }

                                groupedGoals.forEach { (status, goals) ->
                                    item {
                                        Text(
                                            text = status.toSavingGoalStateLabel(),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = when (status) {
                                                SavingGoalResponse.State.COMPLETED -> IncomeGreen
                                                SavingGoalResponse.State.RUNNING -> MaterialTheme.colorScheme.primary
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    when (status) {
                                                        SavingGoalResponse.State.COMPLETED -> IncomeGreen.copy(alpha = 0.08f)
                                                        SavingGoalResponse.State.RUNNING -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f)
                                                        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f)
                                                    },
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(vertical = 6.dp, horizontal = 12.dp)
                                        )
                                    }

                                    items(goals.size) { index ->
                                        val goal = goals[index]
                                        SavingGoalCard(
                                            goal = goal,
                                            iconIndex = index % goalIcons.size,
                                            isSelected = goalSelected?.id == goal.id,
                                            onLongPress = { goalSelected = goal },
                                            onDismiss = { goalSelected = null },
                                            onClick = { goalDetail = goal },
                                            progressModifier = if (index == 0) {
                                                Modifier.tutorialTarget("goal_progress_bar", targetBounds)
                                            } else {
                                                Modifier
                                            }
                                        )
                                    }
                                }

                                item { Spacer(modifier = Modifier.height(72.dp)) }
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

    if (tutorialController != null) {
        val current = tutorialController!!.currentStep
        SpotlightOverlay(
            targetBounds = current?.let { targetBounds[it.targetKey] },
            message = current?.message ?: "",
            isLastStep = tutorialController?.isLastStep ?: false,
            onNext = { tutorialController?.next() },
            onSkip = {
                coroutineScope.launch { TutorialPreferences.markCompleted(context, "tutorial_metas_ahorro") }
                tutorialController = null
            }
        )
    }

    goalSelected?.let { goal ->
        AlertDialog(
            onDismissRequest = { goalSelected = null },
            title = { Text(goal.description.orEmpty(), color = MaterialTheme.colorScheme.primary) },
            text = { Text("¿Qué deseas hacer con esta meta?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            goalToDelete = goal
                            goalSelected = null
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ExpenseRed)
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
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { goalSelected = null }) { Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        )
    }

    goalToDelete?.let { goal ->
        AlertDialog(
            onDismissRequest = { goalToDelete = null },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            title = { Text("¿Eliminar esta meta?", color = ExpenseRed) },
            text = {
                Text(
                    "Estás a punto de eliminar \"${goal.description.orEmpty()}\". " +
                            "Esta acción no se puede deshacer y se perderán todos los registros " +
                            "de progreso y transacciones asociadas a esta meta.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSavingGoal(userId, goal.id!!)
                        goalToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed)
                ) {
                    Text("Sí, eliminar", color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = { goalToDelete = null }) { Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        )
    }

    goalToEdit?.let { goal ->
        SavingGoalFormDialog(
            title = "Editar meta",
            initialDescription = goal.description.orEmpty(),
            initialAmount = goal.targetAmount?.toPlainString().orEmpty(),
            initialDeadline = goal.deadline?.toLocalDate()?.toString().orEmpty(),
            formError = viewModel.formError,
            onConfirm = { description, amount, deadline ->
                viewModel.updateSavingGoal(userId, goal.id!!, description, amount, deadline)
            },
            onDismiss = { goalToEdit = null; viewModel.clearFormError() },
            onSuccessDismiss = { goalToEdit = null }
        )
    }
    goalDetail?.let { goal ->
        AlertDialog(
            onDismissRequest = { goalDetail = null },
            title = { Text("Detalles de la meta", color = MaterialTheme.colorScheme.primary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow(Icons.Default.Description, "Descripción", goal.description.orEmpty())
                    DetailRow(Icons.Default.AttachMoney, "Monto objetivo", "S/ ${goal.targetAmount ?: BigDecimal.ZERO}")
                    DetailRow(Icons.Default.Savings, "Acumulado", "S/ ${goal.accumulated ?: BigDecimal.ZERO}")
                    DetailRow(Icons.Default.Flag, "Estado", goal.state?.name ?: "Sin estado")
                    DetailRow(Icons.Default.TrendingUp, "Progreso", "${goal.progressPercentage?.toInt() ?: 0}%")
                    DetailRow(Icons.Default.List, "Movimientos", "${goal.transactionsCount ?: 0}")
                    DetailRow(Icons.Default.Today, "Días restantes", "${goal.daysRemaining ?: 0}")
                    DetailRow(Icons.Default.Calculate, "Aporte promedio", "S/ ${goal.averageContribution ?: BigDecimal.ZERO}")
                    goal.deadline?.let { DetailRow(Icons.Default.DateRange, "Fecha límite", it.toLocalDate().toString()) }
                    goal.createdAt?.let { DetailRow(Icons.Default.Event, "Fecha creación", it.toLocalDate().toString()) }
                    goal.completedAt?.let { DetailRow(Icons.Default.CheckCircle, "Completada", it.toLocalDate().toString()) }
                    goal.disabledAt?.let { DetailRow(Icons.Default.Block, "Deshabilitada", it.toLocalDate().toString()) }
                    goal.estimatedCompletionDate?.let { DetailRow(Icons.Default.Schedule, "Estimación", it.toLocalDate().toString()) }
                }
            },
            confirmButton = {
                TextButton(onClick = { goalDetail = null }) {
                    Text("Cerrar", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }



    LaunchedEffect(actionSuccess) {
        if (actionSuccess != null) {
            goalToEdit = null
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavingGoalCard(
    goal: SavingGoalResponse,
    iconIndex: Int,
    isSelected: Boolean,
    onLongPress: () -> Unit,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    progressModifier: Modifier = Modifier
) {
    val accentColor = MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                ExpenseRed.copy(alpha = 0.08f)
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            }
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = goalIcons[iconIndex],
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        goal.description.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSelected) ExpenseRed else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Meta: S/ ${goal.targetAmount ?: BigDecimal.ZERO}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                }
                Box(
                    modifier = Modifier
                        .background(
                            when (goal.state) {
                                SavingGoalResponse.State.COMPLETED -> IncomeGreen.copy(alpha = 0.12f)
                                SavingGoalResponse.State.RUNNING -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.18f)
                                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
                            },
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        goal.state.toSavingGoalStateLabel(),
                        style = MaterialTheme.typography.labelSmall,
                        color = when (goal.state) {
                            SavingGoalResponse.State.COMPLETED -> IncomeGreen
                            SavingGoalResponse.State.RUNNING -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Acumulado: S/ ${goal.accumulated ?: BigDecimal.ZERO}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                goal.deadline?.let {
                    Text("Hasta: ${it.toLocalDate()}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            val progress = (goal.progressPercentage ?: 0.0).toFloat() / 100f
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = progressModifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                color = if (isSelected) ExpenseRed else MaterialTheme.colorScheme.tertiary,
                trackColor = accentColor.copy(alpha = 0.12f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${goal.progressPercentage?.toInt() ?: 0}% completado",
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) ExpenseRed else MaterialTheme.colorScheme.primary
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
    formError: String? = null,
    onConfirm: (String, BigDecimal, LocalDate?) -> Unit,
    onDismiss: () -> Unit,
    onSuccessDismiss: () -> Unit = {}
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
        title = { Text(title, color = MaterialTheme.colorScheme.primary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it; amountError = false },
                    label = { Text("Monto objetivo (S/)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = amountError,
                    supportingText = { if (amountError) Text("Ingresa un monto válido") },
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = selectedDate?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Fecha límite") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    }
                )
                if (formError != null) {
                    Text(formError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedAmount = amount.toBigDecimalOrNull()
                    if (description.isNotBlank() && parsedAmount != null) {
                        onConfirm(description, parsedAmount, selectedDate)
                    } else {
                        amountError = parsedAmount == null
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    )
}

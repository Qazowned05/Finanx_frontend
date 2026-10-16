package com.elias.mismonedas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.components.ErrorMessage
import com.elias.mismonedas.ui.components.FinanxCard
import com.elias.mismonedas.ui.components.SpotlightOverlay
import com.elias.mismonedas.ui.components.TimeLineChartCard
import com.elias.mismonedas.ui.components.TransactionRecordItem
import com.elias.mismonedas.ui.components.TutorialController
import com.elias.mismonedas.ui.components.TutorialPreferences
import com.elias.mismonedas.ui.components.TutorialStep
import com.elias.mismonedas.ui.components.tutorialTarget
import com.elias.mismonedas.ui.screens.analytics.components.SectionTitle
import com.elias.mismonedas.ui.theme.ExpenseRed
import com.elias.mismonedas.ui.theme.IncomeGreen
import com.elias.mismonedas.ui.util.toCurrency
import com.elias.mismonedas.ui.viewmodel.HomeAlert
import com.elias.mismonedas.ui.viewmodel.HomeUiState
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetHealth

@Composable
fun HomeScreen(
    userId: Long,
    state: HomeUiState,
    onNewTransaction: () -> Unit,
    onScanReceipt: () -> Unit,
    onOpenTransactions: () -> Unit,
    onRetry: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val targetBounds = remember { mutableStateMapOf<String, Rect>() }
    var tutorialController by remember { mutableStateOf<TutorialController?>(null) }

    LaunchedEffect(Unit) {
        val completed = TutorialPreferences.isCompleted(userId, "tutorial_home").first()
        if (!completed) {
            tutorialController = TutorialController(
                listOf(
                    TutorialStep("home_overview", "Bienvenido a tu panel financiero. Aquí ves tu saldo disponible, tus últimos movimientos y un resumen de tus finanzas."),
                    TutorialStep("home_add_button", "Toca el botón + para registrar un nuevo ingreso o gasto.")
                )
            ) {
                coroutineScope.launch { TutorialPreferences.markCompleted(userId, "tutorial_home") }
                tutorialController = null
            }
        }
    }

    when {
        state.isLoading -> LoadingContent()
        state.error != null -> ErrorContent(
            message = state.error,
            onRetry = onRetry
        )
        else -> HomeContent(
            state = state,
            onNewTransaction = onNewTransaction,
            onScanReceipt = onScanReceipt,
            onOpenTransactions = onOpenTransactions,
            onRetry = onRetry,
            targetBounds = targetBounds
        )
    }

    LaunchedEffect(tutorialController, tutorialController?.currentIndex) {
        if (tutorialController == null) return@LaunchedEffect
        snapshotFlow { tutorialController?.currentStep?.canAdvance() ?: false }
            .drop(1)
            .distinctUntilChanged()
            .filter { it }
            .collect { tutorialController?.next() }
    }

    tutorialController?.let { controller ->
        val current = controller.currentStep
        SpotlightOverlay(
            targetBounds = current?.let { targetBounds[it.targetKey] },
            message = current?.message ?: "",
            isLastStep = controller.isLastStep,
            showNextButton = !(current?.isAutoGated ?: false),
            onNext = { controller.next() },
            onSkip = {
                coroutineScope.launch { TutorialPreferences.markCompleted(userId, "tutorial_home") }
                tutorialController = null
            }
        )
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        ErrorMessage(
            message = message,
            onRetry = onRetry
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onNewTransaction: () -> Unit,
    onScanReceipt: () -> Unit,
    onOpenTransactions: () -> Unit,
    onRetry: () -> Unit,
    targetBounds: MutableMap<String, Rect> = mutableMapOf(),
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        com.elias.mismonedas.ui.components.RefreshableList(
            isRefreshing = state.isRefreshing,
            onRefresh = onRetry,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DecisionSurface(
                        userName = state.userName,
                        balance = state.primaryBalance.toCurrency(state.primaryCurrency),
                        alert = state.primaryAlert,
                        targetBounds = targetBounds
                    )
                }

                // FIX-005: Cards aleatorias de presupuesto y meta
                if (state.randomBudget != null) {
                    item {
                        RandomBudgetCard(budget = state.randomBudget, currencyCode = state.primaryCurrency)
                    }
                }
                if (state.randomSavingGoal != null) {
                    item {
                        RandomSavingGoalCard(goal = state.randomSavingGoal, currencyCode = state.primaryCurrency)
                    }
                }

                item {
                    TimeLineChartCard(
                        title = "Línea de tiempo de Ingresos",
                        points = state.dailyIncomePoints,
                        pointLabels = state.dailyIncomeLabels,
                        lineColor = IncomeGreen
                    )
                }

                item {
                    TimeLineChartCard(
                        title = "Línea de tiempo de Gastos",
                        points = state.dailyExpensePoints,
                        pointLabels = state.dailyExpenseLabels,
                        lineColor = ExpenseRed
                    )
                }

                item {
                    SectionTitle("Movimientos recientes")
                }

                if (state.recentTransactions.isEmpty()) {
                    item {
                        Text(
                            text = "Todavía no tienes movimientos.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(
                        items = state.recentTransactions,
                        key = { transaction -> transaction.id ?: 0 }
                    ) { transaction ->
                        TransactionRecordItem(transaction = transaction)
                    }
                }
            }
        }

        // FIX-006: FABs de cámara y "+"
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FloatingActionButton(
                onClick = onScanReceipt,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Escanear recibo",
                    modifier = Modifier.size(24.dp),
                )
            }
            FloatingActionButton(
                onClick = onNewTransaction,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.tutorialTarget("home_add_button", targetBounds)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Nuevo",
                )
            }
        }
    }
}

@Composable
private fun DecisionSurface(
    userName: String,
    balance: String,
    alert: HomeAlert?,
    targetBounds: MutableMap<String, Rect> = mutableMapOf()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Hola, $userName",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        FinanxCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            border = null
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Balance actual",
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = balance,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                if (alert == null) {
                    Text(
                        text = "Una vista clara para decidir rápido qué hacer después.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }

        if (alert != null) {
            FinanxCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                border = null
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = alert.title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = alert.message,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

// FIX-005: Card de presupuesto aleatorio
@Composable
private fun RandomBudgetCard(
    budget: org.openapitools.client.models.BudgetExecutionResponse,
    currencyCode: String
) {
    val progress = (budget.executionPercentage?.toFloat() ?: 0f) / 100f
    val healthColor = when (budget.health) {
        BudgetHealth.EXCEEDED -> MaterialTheme.colorScheme.error
        BudgetHealth.NEAR_LIMIT -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.primary
    }

    FinanxCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = null
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Presupuesto",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                budget.description ?: "Sin nombre",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Límite: ${(budget.limitAmount ?: java.math.BigDecimal.ZERO).toCurrency(currencyCode)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            val spent = budget.spentAmount
            if (spent != null) {
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                    color = healthColor,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                )
                Text(
                    "Gastado: ${spent!!.toCurrency(currencyCode)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// FIX-005: Card de meta de ahorro aleatoria
@Composable
private fun RandomSavingGoalCard(
    goal: org.openapitools.client.models.SavingGoalResponse,
    currencyCode: String
) {
    val target = goal.targetAmount ?: java.math.BigDecimal.ZERO
    val current = goal.accumulated ?: java.math.BigDecimal.ZERO
    val progress = if (target > java.math.BigDecimal.ZERO) {
        current.toFloat() / target.toFloat()
    } else 0f

    FinanxCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = null
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Savings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Meta de ahorro",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                goal.description ?: "Sin nombre",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Meta: ${target.toCurrency(currencyCode)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            )
            Text(
                "Ahorrado: ${current.toCurrency(currencyCode)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


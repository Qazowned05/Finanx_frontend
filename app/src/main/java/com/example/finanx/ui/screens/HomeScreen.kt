package com.example.finanx.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.ErrorMessage
import com.example.finanx.ui.components.FinanxCard
import com.example.finanx.ui.components.SpotlightOverlay
import com.example.finanx.ui.components.TimeLineChartCard
import com.example.finanx.ui.components.TransactionRecordItem
import com.example.finanx.ui.components.TutorialController
import com.example.finanx.ui.components.TutorialPreferences
import com.example.finanx.ui.components.TutorialStep
import com.example.finanx.ui.components.tutorialTarget
import com.example.finanx.ui.screens.analytics.components.SectionTitle
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.viewmodel.HomeAlert
import com.example.finanx.ui.viewmodel.HomeUiState
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    userId: Long,
    state: HomeUiState,
    onNewTransaction: () -> Unit,
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
    onOpenTransactions: () -> Unit,
    onRetry: () -> Unit,
    targetBounds: MutableMap<String, Rect> = mutableMapOf(),
    modifier: Modifier = Modifier
) {
    com.example.finanx.ui.components.RefreshableList(
        isRefreshing = state.isRefreshing,
        onRefresh = onRetry,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DecisionSurface(
                    userName = state.userName,
                    balance = state.primaryBalance.toPen(),
                    alert = state.primaryAlert,
                    actionLabel = state.primaryActionLabel,
                    onNewTransaction = onNewTransaction,
                    onOpenTransactions = onOpenTransactions,
                    targetBounds = targetBounds
                )
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
                    key = { transaction -> transaction.id }
                ) { transaction ->
                    TransactionRecordItem(transaction = transaction)
                }
            }
        }
    }
}

@Composable
private fun DecisionSurface(
    userName: String,
    balance: String,
    alert: HomeAlert?,
    actionLabel: String,
    onNewTransaction: () -> Unit,
    onOpenTransactions: () -> Unit,
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onOpenTransactions,
                modifier = Modifier.weight(1f)
            ) {
                Text("Ver movimientos")
            }
            Button(
                onClick = onNewTransaction,
                modifier = Modifier
                    .weight(1f)
                    .tutorialTarget("home_add_button", targetBounds)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(actionLabel)
            }
        }
    }
}


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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.ErrorMessage
import com.example.finanx.ui.components.TimeLineChartCard
import com.example.finanx.ui.components.TransactionRecordItem
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.viewmodel.HomeAlert
import com.example.finanx.ui.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    state: HomeUiState,
    onNewTransaction: () -> Unit,
    onOpenTransactions: () -> Unit,
    onRetry: () -> Unit
) {
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
            onRetry = onRetry
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
                    onOpenTransactions = onOpenTransactions
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
    onOpenTransactions: () -> Unit
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

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                modifier = Modifier.weight(1f)
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

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

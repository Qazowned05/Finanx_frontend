package com.example.finanx.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.FinanxTopAppBar
import com.example.finanx.ui.components.TimeLineChartCard
import com.example.finanx.ui.components.TransactionRecordItem
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.theme.BlueSecondary
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.GrayBorder
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.theme.TextLight
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    state: HomeUiState,
    onNewTransaction: () -> Unit,
    onOpenTransactions: () -> Unit,
    onNotifications: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            FinanxTopAppBar(
                title = "Mis finanzas",
                onNotifications = onNotifications,
                onProfile = onProfile,
                onLogout = onLogout
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                LoadingContent(
                    modifier = Modifier.padding(paddingValues)
                )
            }

            state.error != null -> {
                ErrorContent(
                    message = state.error.orEmpty(),
                    onRetry = onRetry,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                HomeContent(
                    state = state,
                    onNewTransaction = onNewTransaction,
                    onOpenTransactions = onOpenTransactions,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onRetry
        ) {
            Text("Reintentar")
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onNewTransaction: () -> Unit,
    onOpenTransactions: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeSummaryHeader(
                userName = state.userName,
                balance = state.balance.toPen(),
                monthlyIncome = state.monthlyIncome.toPen(),
                monthlyExpenses = state.monthlyExpenses.toPen(),
                incomePoints = state.dailyIncomePoints,
                expensePoints = state.dailyExpensePoints
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
            HomeActions(
                onNewTransaction = onNewTransaction,
                onOpenTransactions = onOpenTransactions
            )
        }

        item {
            Text(
                text = "Movimientos recientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (state.recentTransactions.isEmpty()) {
            item {
                Text(
                    text = "Todavía no tienes movimientos.",
                    color = TextGray
                )
            }
        } else {
            items(
                items = state.recentTransactions,
                key = { transaction ->
                    transaction.id
                }
            ) { transaction ->
                TransactionRecordItem(
                    transaction = transaction
                )
            }
        }
    }
}

@Composable
private fun HomeSummaryHeader(
    userName: String,
    balance: String,
    monthlyIncome: String,
    monthlyExpenses: String,
    incomePoints: List<Float>,
    expensePoints: List<Float>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BlueSecondary,
                        BluePrimary
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Hola, $userName",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextLight
        )

        BalanceCard(
            balance = balance,
            incomePoints = incomePoints,
            expensePoints = expensePoints
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeSummaryCard(
                title = "Ingresos del mes",
                amount = monthlyIncome,
                amountColor = IncomeGreen,
                modifier = Modifier.weight(1f)
            )

            HomeSummaryCard(
                title = "Gastos del mes",
                amount = monthlyExpenses,
                amountColor = ExpenseRed,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BalanceCard(
    balance: String,
    incomePoints: List<Float>,
    expensePoints: List<Float>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Balance actual",
                    color = TextGray
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = balance,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Calculado con tus movimientos activos",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray
                )
            }

            MiniChartPlaceholder(
                incomePoints = incomePoints,
                expensePoints = expensePoints,
                modifier = Modifier
                    .width(92.dp)
                    .height(62.dp)
            )
        }
    }
}

@Composable
private fun MiniChartPlaceholder(
    incomePoints: List<Float>,
    expensePoints: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val safeIncome = incomePoints.ifEmpty { listOf(0f) }
        val safeExpenses = expensePoints.ifEmpty { listOf(0f) }
        val maxPoint = (safeIncome + safeExpenses)
            .maxOrNull()
            ?.takeIf { it > 0f }
            ?: 1f

        fun pathFor(points: List<Float>): Path {
            return Path().apply {
                points.forEachIndexed { index, value ->
                    val x = if (points.size > 1) width * index / (points.lastIndex) else width / 2f
                    val y = height - (height * (value / maxPoint))
                    if (index == 0) {
                        moveTo(x, y)
                    } else {
                        lineTo(x, y)
                    }
                }
            }
        }

        drawPath(
            path = pathFor(safeIncome),
            color = IncomeGreen,
            style = Stroke(width = 3.dp.toPx())
        )

        drawPath(
            path = pathFor(safeExpenses),
            color = ExpenseRed,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}


@Composable
private fun HomeActions(
    onNewTransaction: () -> Unit,
    onOpenTransactions: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onNewTransaction,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text("Nuevo")
        }

        OutlinedButton(
            onClick = onOpenTransactions,
            modifier = Modifier.weight(1f)
        ) {
            Text("Ver movimientos")
        }
    }
}

@Composable
private fun HomeSummaryCard(
    title: String,
    amount: String,
    amountColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = amount,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}

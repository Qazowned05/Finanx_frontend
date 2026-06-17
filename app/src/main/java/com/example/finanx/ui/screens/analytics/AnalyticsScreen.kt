package com.example.finanx.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.viewmodel.AnalyticsViewModel
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.Insight
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnalyticsScreen(
    userId: Long,
    viewModel: AnalyticsViewModel = viewModel()
) {/*
    val state = viewModel.analyticsState

    LaunchedEffect(userId) {
        viewModel.loadDashboard(userId)
    }

    val dashboard = state.dashboard
    val transactions = dashboard?.transactions
    val budgets = dashboard?.budgets
    val categories = dashboard?.categories
    val insights = dashboard?.insights.orEmpty()
    val executions = budgets?.budgetExecutions.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Resumen Financiero",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF20232D)
                )
                Text(
                    text = "Resumen de los últimos 30 días",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
            }

            IconButton(onClick = { viewModel.loadDashboard(userId) }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Actualizar",
                    tint = Color(0xFF2563EB)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2563EB))
                }
            }

            state.error != null -> {
                ErrorPanel(
                    message = state.error,
                    onRetry = { viewModel.loadDashboard(userId) }
                )
            }

            dashboard == null -> {
                EmptyPanel()
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = "Ingresos",
                                value = money(transactions?.totalIncomeAmount),
                                icon = Icons.Default.ArrowUpward,
                                color = Color(0xFF16A34A),
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = "Gastos",
                                value = money(transactions?.totalSpentAmount),
                                icon = Icons.Default.ArrowDownward,
                                color = Color(0xFFDC2626),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = "Balance",
                                value = money(transactions?.netAmount),
                                icon = Icons.Default.TrendingUp,
                                color = Color(0xFF2563EB),
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = "Disponible",
                                value = money(budgets?.totalBudgetRemainingAmount),
                                icon = Icons.Default.AccountBalanceWallet,
                                color = Color(0xFF7C3AED),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        SectionTitle("Presupuestos")
                    }

                    if (executions.isEmpty()) {
                        item {
                            SmallMessage("No hay ejecución de presupuestos para este periodo.")
                        }
                    } else {
                        items(executions.take(5)) { budget ->
                            BudgetExecutionItem(budget)
                        }
                    }

                    item {
                        SectionTitle("Mayores Movimientos")
                    }

                    val topCategories = categories?.topSpentCategories.orEmpty()
                    if (topCategories.isEmpty()) {
                        item {
                            SmallMessage("No hay categorías con movimientos todavía.")
                        }
                    } else {
                        items(topCategories.take(5)) { item ->
                            SimpleRankItem(
                                title = item.item?.categoryName ?: "Categoría",
                                value = money(item.metricValue),
                                detail = "${item.occurrenceCount ?: 0} movimientos"
                            )
                        }
                    }

                    item {
                        SectionTitle("Recomendaciones")
                    }

                    if (insights.isEmpty()) {
                        item {
                            SmallMessage("Aún no hay recomendaciones para mostrar.")
                        }
                    } else {
                        items(insights.take(4)) { insight ->
                            InsightItem(insight)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }*/
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, color = Color(0xFF6B7280), style = MaterialTheme.typography.bodySmall)
            Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF20232D))
        }
    }
}

@Composable
private fun BudgetExecutionItem(budget: BudgetExecutionResponse) {
    val percentage = budget.executionPercentage?.toFloat() ?: 0f
    val progress = (percentage / 100f).coerceIn(0f, 1f)
    val color = when (budget.health) {
        BudgetExecutionResponse.Health.EXCEEDED -> Color(0xFFDC2626)
        BudgetExecutionResponse.Health.NEAR_LIMIT -> Color(0xFFD97706)
        BudgetExecutionResponse.Health.OK -> Color(0xFF16A34A)
        else -> Color(0xFF6B7280)
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = budget.description ?: "Presupuesto",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF20232D)
                    )
                    Text(
                        text = "${money(budget.spentAmount)} de ${money(budget.limitAmount)}",
                        color = Color(0xFF6B7280),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = "${percentage.toInt()}%",
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = color,
                trackColor = Color(0xFFE5E7EB)
            )
        }
    }
}

@Composable
private fun SimpleRankItem(
    title: String,
    value: String,
    detail: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.PieChart, contentDescription = null, tint = Color(0xFF2563EB))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(detail, color = Color(0xFF6B7280), style = MaterialTheme.typography.bodySmall)
            }
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun InsightItem(insight: Insight) {
    val color = when (insight.severity) {
        Insight.Severity.CRITICAL -> Color(0xFFDC2626)
        Insight.Severity.WARNING -> Color(0xFFD97706)
        Insight.Severity.INFO -> Color(0xFF2563EB)
        null -> Color(0xFF2563EB)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(Icons.Default.Insights, contentDescription = null, tint = color)
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = insight.title ?: "Insight",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF20232D)
                )
                Text(
                    text = insight.description ?: insight.action ?: "Sin detalle",
                    color = Color(0xFF6B7280),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF20232D),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 6.dp)
    )
}

@Composable
private fun SmallMessage(text: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(14.dp),
            color = Color(0xFF6B7280),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ErrorPanel(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFDC2626))
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = Color(0xFF991B1B))
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun EmptyPanel() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No hay datos de analíticas disponibles.", color = Color(0xFF6B7280))
    }
}

private fun money(value: BigDecimal?): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
    return format.format(value ?: BigDecimal.ZERO)
}
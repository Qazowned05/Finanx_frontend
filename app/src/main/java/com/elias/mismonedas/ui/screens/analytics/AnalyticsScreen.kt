package com.elias.mismonedas.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elias.mismonedas.ui.components.AppBarState
import com.elias.mismonedas.ui.components.FinanxCard
import com.elias.mismonedas.ui.screens.analytics.components.*
import com.elias.mismonedas.ui.theme.*
import com.elias.mismonedas.ui.viewmodel.AnalyticsViewModel
import org.openapitools.client.models.AggregateByBudgetHealth
import org.openapitools.client.models.AggregateByTransactionType
import org.openapitools.client.models.BudgetHealth
import org.openapitools.client.models.BudgetsDashboard
import org.openapitools.client.models.TransactionType
import org.openapitools.client.models.TransactionsDashboard
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnalyticsScreen(
    userId: Long,
    viewModel: AnalyticsViewModel = viewModel(),
    onAppBarState: (AppBarState) -> Unit = {}
) {
    val state = viewModel.state

    LaunchedEffect(userId) {
        viewModel.loadDashboard(userId)
        viewModel.loadBudgetsDashboard(userId)
    }

    LaunchedEffect(state.dashboard) {
        onAppBarState(
            AppBarState(
                title = "Análisis",
                subtitle = state.dashboard?.periodResponse?.readableString ?: "Últimos 30 días",
                onRefresh = { viewModel.refresh(userId) }
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.isLoading -> LoadingPanel()
            state.error != null -> ErrorPanel(
                message = state.error,
                onRetry = { viewModel.refresh(userId) }
            )
            state.dashboard == null && state.budgetsDashboard == null -> EmptyPanel()
            else -> DashboardContent(
                dashboard = state.dashboard,
                budgetsDashboard = state.budgetsDashboard,
                isBudgetsLoading = state.isBudgetsLoading,
                budgetsError = state.budgetsError,
                startDate = state.startDate,
                endDate = state.endDate,
                onQuickSelect = { days ->
                    val end = java.time.LocalDate.now()
                    val start = end.minusDays(days.toLong())
                    viewModel.setStartDate(start)
                    viewModel.setEndDate(end)
                    viewModel.loadDashboard(userId)
                    viewModel.loadBudgetsDashboard(userId)
                }
            )
        }
    }
}

@Composable
private fun DashboardContent(
    dashboard: TransactionsDashboard?,
    budgetsDashboard: BudgetsDashboard?,
    isBudgetsLoading: Boolean,
    budgetsError: String?,
    startDate: java.time.LocalDate,
    endDate: java.time.LocalDate,
    onQuickSelect: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 100.dp)
    ) {
        item {
            PeriodSelectorRow(start = startDate, end = endDate, onQuickSelect = onQuickSelect)
        }

        if (budgetsDashboard != null) {
            item { SectionTitle("Presupuestos") }
            item {
                FinanxCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        budgetsDashboard.totalBudgetLimitAmount?.let { limit ->
                            budgetsDashboard.totalBudgetSpentAmount?.let { spent ->
                                val remaining = budgetsDashboard.totalBudgetRemainingAmount ?: limit.subtract(spent)
                                MetricCard(title = "Límite total", value = money(limit), icon = Icons.Default.AccountBalance, accentColor = MaterialTheme.colorScheme.primary)
                                MetricCard(title = "Gastado", value = money(spent), icon = Icons.Default.ShoppingCart, accentColor = ExpenseRed)
                                MetricCard(title = "Restante", value = money(remaining), icon = Icons.Default.Savings, accentColor = IncomeGreen)
                            }
                        }
                        budgetsDashboard.byHealth?.let { byHealth ->
                            val slices = byHealth.mapNotNull { health ->
                        val color = when (health.factor) {
                                BudgetHealth.OK -> IncomeGreen
                                BudgetHealth.NEAR_LIMIT -> ExpenseRed
                                BudgetHealth.EXCEEDED -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.outline
                            }
                            DonutSlice(
                                label = health.factor?.value ?: "Desconocido",
                                    share = (health.sharePercentage?.toFloat() ?: 0f) / 100f,
                                    color = color
                                )
                            }
                            if (slices.isNotEmpty()) {
                                DonutChartCard(title = "Salud de presupuestos", slices = slices)
                            }
                        }
                    }
                }
            }
        } else if (isBudgetsLoading) {
            item {
                Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (budgetsError != null) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(budgetsError, modifier = Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }

        if (dashboard != null) {
            val agg = dashboard.aggregate
            val typeRanking = dashboard.summaryByType
            val catRanking = dashboard.topCategories ?: dashboard.summaryByCategory
            val spentReasons = dashboard.topSpentReasons
            val incReasons = dashboard.topIncomeReasons
            val pmRanking = dashboard.summaryByPaymentMethod
            val expenses = dashboard.dailyExpenses
            val income = dashboard.dailyIncome
            val insights = dashboard.insightSummary

            if (agg != null) {
                item { SectionTitle("Resumen del período") }
                item {
                    val incomeItem = typeRanking?.items?.firstOrNull {
                        it.aggregate?.factor == TransactionType.INCOME
                    }
                    val spentItem = typeRanking?.items?.firstOrNull {
                        it.aggregate?.factor == TransactionType.SPENT
                    }
                    val incomeVal = incomeItem?.aggregate?.aggregate?.totalAmount ?: BigDecimal.ZERO
                    val spentVal = spentItem?.aggregate?.aggregate?.totalAmount ?: BigDecimal.ZERO
                    val net = incomeVal.subtract(spentVal)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            title = "Ingresos", value = money(incomeVal),
                            icon = Icons.Default.ArrowUpward,
                            accentColor = IncomeGreen,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "Gastos", value = money(spentVal),
                            icon = Icons.Default.ArrowDownward,
                            accentColor = ExpenseRed,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    MetricCard(
                        title = "Balance neto", value = money(net),
                        icon = Icons.AutoMirrored.Filled.TrendingUp, accentColor = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item { AggregateCard(aggregate = agg) }
            }

            if (typeRanking != null) {
                item { SectionTitle("Distribución por tipo") }
                item {
                    val slices = typeRanking.items.orEmpty().mapNotNull { rankItem ->
                        val factor = rankItem.aggregate?.factor ?: return@mapNotNull null
                        val share = rankItem.aggregate?.sharePercentage?.toFloat()?.div(100f) ?: 0f
                        val label = when (factor) {
                            TransactionType.INCOME -> "Ingresos"
                            TransactionType.SPENT -> "Gastos"
                        }
                        val color = if (factor == TransactionType.INCOME) IncomeGreen else ExpenseRed
                        DonutSlice(label, share, color)
                    }
                    DonutChartCard(title = "Ingresos vs Gastos", slices = slices)
                }
            }

            if (catRanking != null) {
                item { SectionTitle("Top categorías") }
                item {
                    val bars = catRanking.items.orEmpty().map { rankItem ->
                        BarItem(
                            label = rankItem.aggregate?.factor?.name ?: "Categoría",
                            value = rankItem.metricValue ?: BigDecimal.ZERO,
                            share = (rankItem.aggregate?.sharePercentage?.toFloat() ?: 0f) / 100f,
                            badge = "${rankItem.rank}°"
                        )
                    }
                    HorizontalBarChartCard(title = catRanking.title ?: "Categorías", items = bars, barColor = ExpenseRed)
                }
            }

            if (pmRanking != null) {
                item { SectionTitle("Métodos de pago") }
                item {
                    val bars = pmRanking.items.orEmpty().map { rankItem ->
                        BarItem(
                            label = rankItem.aggregate?.factorName ?: "Método",
                            value = rankItem.aggregate?.aggregate?.totalAmount ?: BigDecimal.ZERO,
                            share = (rankItem.aggregate?.sharePercentage?.toFloat() ?: 0f) / 100f
                        )
                    }
                    HorizontalBarChartCard(title = pmRanking.title ?: "Métodos de pago", items = bars, barColor = MaterialTheme.colorScheme.tertiary)
                }
            }

            if (expenses != null) {
                item { SectionTitle("Evolución de gastos") }
                item { LineChartCard(timeline = expenses, lineColor = ExpenseRed) }
            }

            if (income != null) {
                item { SectionTitle("Evolución de ingresos") }
                item { LineChartCard(timeline = income, lineColor = IncomeGreen) }
            }

            if (spentReasons != null) {
                item { SectionTitle("Principales conceptos de gasto") }
                item {
                    val bars = spentReasons.items.orEmpty().map { rankItem ->
                        BarItem(
                            label = rankItem.aggregate?.factorName ?: "Concepto",
                            value = rankItem.aggregate?.aggregate?.totalAmount ?: BigDecimal.ZERO,
                            share = (rankItem.aggregate?.sharePercentage?.toFloat() ?: 0f) / 100f,
                            badge = "${rankItem.aggregate?.aggregate?.occurrenceCount ?: 0}x"
                        )
                    }
                    HorizontalBarChartCard(title = spentReasons.title ?: "Conceptos de gasto", items = bars, barColor = ExpenseRed)
                }
            }

            if (incReasons != null) {
                item { SectionTitle("Principales fuentes de ingreso") }
                item {
                    val bars = incReasons.items.orEmpty().map { rankItem ->
                        BarItem(
                            label = rankItem.aggregate?.factorName ?: "Fuente",
                            value = rankItem.aggregate?.aggregate?.totalAmount ?: BigDecimal.ZERO,
                            share = (rankItem.aggregate?.sharePercentage?.toFloat() ?: 0f) / 100f,
                            badge = "${rankItem.aggregate?.aggregate?.occurrenceCount ?: 0}x"
                        )
                    }
                    HorizontalBarChartCard(title = incReasons.title ?: "Fuentes de ingreso", items = bars, barColor = IncomeGreen)
                }
            }

            if (insights != null) {
                item { SectionTitle("Recomendaciones") }
                item { InsightSummaryCard(insightSummary = insights) }
            }
        }
    }
}

@Composable
private fun LoadingPanel() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
private fun ErrorPanel(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, null, tint = ExpenseRed, modifier = Modifier.size(40.dp))
            Spacer(Modifier.height(12.dp))
            Text(message, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Text("Reintentar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
private fun EmptyPanel() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No hay datos para este período.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun money(value: BigDecimal): String =
    NumberFormat.getCurrencyInstance(Locale("es", "PE")).format(value)
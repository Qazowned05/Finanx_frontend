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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.screens.analytics.components.AggregateCard
import com.example.finanx.ui.screens.analytics.components.AiFab
import com.example.finanx.ui.screens.analytics.components.BarItem
import com.example.finanx.ui.screens.analytics.components.ChatBotPromptDialog
import com.example.finanx.ui.screens.analytics.components.DonutChartCard
import com.example.finanx.ui.screens.analytics.components.DonutSlice
import com.example.finanx.ui.screens.analytics.components.HorizontalBarChartCard
import com.example.finanx.ui.screens.analytics.components.InsightSummaryCard
import com.example.finanx.ui.screens.analytics.components.LineChartCard
import com.example.finanx.ui.screens.analytics.components.MetricCard
import com.example.finanx.ui.screens.analytics.components.PeriodSelectorRow
import com.example.finanx.ui.screens.analytics.components.SectionTitle
import com.example.finanx.ui.viewmodel.AnalyticsViewModel
import org.openapitools.client.models.AggregateByTransactionType
import org.openapitools.client.models.TransactionsDashboard
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    userId: Long,
    viewModel: AnalyticsViewModel = viewModel()
) {
    val state = viewModel.state

    LaunchedEffect(userId) {
        viewModel.loadDashboard(userId)
    }
    if (state.showChatDialog) {
        ChatBotPromptDialog(
            prompt      = state.pendingPrompt,
            isLoading   = state.isAiLoading,
            onPromptChange = viewModel::onPromptChange,
            onSubmit    = { viewModel.submitPrompt(userId) },
            onSkip      = {
                viewModel.closeChatDialog()
                viewModel.loadDashboard(userId, prompt = null)
            },
            onDismiss   = viewModel::closeChatDialog
        )
    }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Análisis",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextLight
                        )
                        Text(
                            text = state.dashboard?.periodResponse?.readableString
                                ?: "Últimos 30 días",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextLight.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh(userId) }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = GoldAccent
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrownPrimary,
                    titleContentColor = TextLight
                )
            )
        },
        floatingActionButton = {
            AiFab(
                isAiLoading = state.isAiLoading,
                onClick     = viewModel::openChatDialog
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmBackground)
        ) {
            when {
                state.isLoading -> LoadingPanel()
                state.error != null -> ErrorPanel(
                    message = state.error,
                    onRetry = { viewModel.refresh(userId) }
                )
                state.dashboard == null -> EmptyPanel()
                else -> DashboardContent(
                    dashboard   = state.dashboard,
                    startDate   = state.startDate,
                    endDate     = state.endDate,
                    isAiLoading = state.isAiLoading,
                    onQuickSelect = { days ->
                        val end   = java.time.LocalDate.now()
                        val start = end.minusDays(days.toLong())
                        viewModel.setStartDate(start)
                        viewModel.setEndDate(end)
                        viewModel.loadDashboard(userId)
                    }
                )
            }
        }
    }
}
@Composable
private fun DashboardContent(
    dashboard: TransactionsDashboard,
    startDate: java.time.LocalDate,
    endDate: java.time.LocalDate,
    isAiLoading: Boolean,
    onQuickSelect: (Int) -> Unit
) {
    val agg          = dashboard.aggregate
    val typeRanking  = dashboard.summaryByType
    val catRanking   = dashboard.topCategories ?: dashboard.summaryByCategory
    val spentReasons = dashboard.topSpentReasons
    val incReasons   = dashboard.topIncomeReasons
    val pmRanking    = dashboard.summaryByPaymentMethod
    val expenses     = dashboard.dailyExpenses
    val income       = dashboard.dailyIncome
    val insights     = dashboard.insightSummary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = 14.dp,
            bottom = 100.dp
        )
    ) {
        item {
            PeriodSelectorRow(
                start = startDate,
                end   = endDate,
                onQuickSelect = onQuickSelect
            )
        }

        if (isAiLoading) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrownPrimary, androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CircularProgressIndicator(color = GoldAccent, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Text("Generando análisis con IA…", color = TextLight,
                        style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        if (agg != null) {
            item {
                SectionTitle("Resumen del período")
            }
            item {
                val incomeItem = typeRanking?.items?.firstOrNull {
                    it.aggregate?.factor == AggregateByTransactionType.Factor.INCOME
                }
                val spentItem = typeRanking?.items?.firstOrNull {
                    it.aggregate?.factor == AggregateByTransactionType.Factor.SPENT
                }
                val incomeVal = incomeItem?.aggregate?.aggregate?.totalAmount ?: BigDecimal.ZERO
                val spentVal  = spentItem?.aggregate?.aggregate?.totalAmount  ?: BigDecimal.ZERO
                val net       = incomeVal.subtract(spentVal)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title       = "Ingresos",
                        value       = money(incomeVal),
                        icon        = Icons.Default.ArrowUpward,
                        accentColor = IncomeGreen,
                        modifier    = Modifier.weight(1f)
                    )
                    MetricCard(
                        title       = "Gastos",
                        value       = money(spentVal),
                        icon        = Icons.Default.ArrowDownward,
                        accentColor = ExpenseRed,
                        modifier    = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(10.dp))
                MetricCard(
                    title       = "Balance neto",
                    value       = money(net),
                    icon        = Icons.Default.TrendingUp,
                    accentColor = GoldAccent,
                    modifier    = Modifier.fillMaxWidth()
                )
            }

            item { AggregateCard(aggregate = agg) }
        }

        if (typeRanking != null) {
            item { SectionTitle("Distribución por tipo") }
            item {
                val slices = typeRanking.items.orEmpty().mapNotNull { rankItem ->
                    val factor = rankItem.aggregate?.factor ?: return@mapNotNull null
                    val share  = rankItem.aggregate?.sharePercentage
                        ?.toFloat()?.div(100f) ?: 0f
                    val label  = when (factor) {
                        AggregateByTransactionType.Factor.INCOME -> "Ingresos"
                        AggregateByTransactionType.Factor.SPENT  -> "Gastos"
                    }
                    val color  = if (factor == AggregateByTransactionType.Factor.INCOME)
                        IncomeGreen else ExpenseRed
                    DonutSlice(label, share, color)
                }
                DonutChartCard(title = "Ingresos vs Gastos", slices = slices)
            }
        }

        if (catRanking != null) {
            item { SectionTitle("Top categorías de gasto") }
            item {
                val bars = catRanking.items.orEmpty().map { rankItem ->
                    BarItem(
                        label = rankItem.aggregate?.factor?.name ?: "Categoría",
                        value = rankItem.metricValue ?: BigDecimal.ZERO,
                        share = (rankItem.aggregate?.sharePercentage
                            ?.toFloat() ?: 0f) / 100f,
                        badge = "${rankItem.rank}°"
                    )
                }
                HorizontalBarChartCard(
                    title    = catRanking.title ?: "Categorías",
                    items    = bars,
                    barColor = ExpenseRed
                )
            }
        }

        if (pmRanking != null) {
            item { SectionTitle("Métodos de pago") }
            item {
                val bars = pmRanking.items.orEmpty().map { rankItem ->
                    BarItem(
                        label = rankItem.aggregate?.factorName ?: "Método",
                        value = rankItem.aggregate?.aggregate?.totalAmount ?: BigDecimal.ZERO,
                        share = (rankItem.aggregate?.sharePercentage
                            ?.toFloat() ?: 0f) / 100f
                    )
                }
                HorizontalBarChartCard(
                    title    = pmRanking.title ?: "Métodos de pago",
                    items    = bars,
                    barColor = GoldAccent
                )
            }
        }

        if (expenses != null) {
            item { SectionTitle("Evolución de gastos") }
            item {
                LineChartCard(
                    timeline  = expenses,
                    lineColor = ExpenseRed
                )
            }
        }

        if (income != null) {
            item { SectionTitle("Evolución de ingresos") }
            item {
                LineChartCard(
                    timeline  = income,
                    lineColor = IncomeGreen
                )
            }
        }

        if (spentReasons != null) {
            item { SectionTitle("Principales conceptos de gasto") }
            item {
                val bars = spentReasons.items.orEmpty().map { rankItem ->
                    BarItem(
                        label = rankItem.aggregate?.factorName ?: "Concepto",
                        value = rankItem.aggregate?.aggregate?.totalAmount ?: BigDecimal.ZERO,
                        share = (rankItem.aggregate?.sharePercentage
                            ?.toFloat() ?: 0f) / 100f,
                        badge = "${rankItem.aggregate?.aggregate?.occurrenceCount ?: 0}x"
                    )
                }
                HorizontalBarChartCard(
                    title    = spentReasons.title ?: "Conceptos de gasto",
                    items    = bars,
                    barColor = ExpenseRed
                )
            }
        }

        if (incReasons != null) {
            item { SectionTitle("Principales fuentes de ingreso") }
            item {
                val bars = incReasons.items.orEmpty().map { rankItem ->
                    BarItem(
                        label = rankItem.aggregate?.factorName ?: "Fuente",
                        value = rankItem.aggregate?.aggregate?.totalAmount ?: BigDecimal.ZERO,
                        share = (rankItem.aggregate?.sharePercentage
                            ?.toFloat() ?: 0f) / 100f,
                        badge = "${rankItem.aggregate?.aggregate?.occurrenceCount ?: 0}x"
                    )
                }
                HorizontalBarChartCard(
                    title    = incReasons.title ?: "Fuentes de ingreso",
                    items    = bars,
                    barColor = IncomeGreen
                )
            }
        }

        if (insights != null) {
            item { SectionTitle("Recomendaciones") }
            item {
                InsightSummaryCard(
                    insightSummary = insights,
                    aiGenerated    = true
                )
            }
        }
    }
}

@Composable
private fun LoadingPanel() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = GoldAccent)
    }
}

@Composable
private fun ErrorPanel(message: String, onRetry: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, null, tint = ExpenseRed,
                modifier = Modifier.size(40.dp))
            Spacer(Modifier.height(12.dp))
            Text(message, color = TextDark, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
            ) {
                Text("Reintentar", color = TextLight)
            }
        }
    }
}

@Composable
private fun EmptyPanel() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No hay datos de analíticas para este período.", color = TextGray)
    }
}

private fun money(value: BigDecimal): String =
    NumberFormat.getCurrencyInstance(Locale("es", "PE")).format(value)

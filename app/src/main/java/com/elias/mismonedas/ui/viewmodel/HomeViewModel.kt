package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.BudgetRepository
import com.elias.mismonedas.data.repository.SavingGoalRepository
import com.elias.mismonedas.data.repository.TransactionRepository
import com.elias.mismonedas.ui.util.buildCurrentMonthPeriod
import com.elias.mismonedas.ui.util.toReadableDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetState
import org.openapitools.client.models.SavingGoalResponse
import org.openapitools.client.models.SavingGoalState
import org.openapitools.client.models.TransactionResponse
import org.openapitools.client.models.TransactionType
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

data class HomeAlert(
    val title: String,
    val message: String
)

data class HomeUiState(
    val isLoading: Boolean = false,
    override val isRefreshing: Boolean = false,
    val userName: String = "",
    val primaryBalance: BigDecimal = BigDecimal.ZERO,
    val primaryCurrency: String = "PEN",
    val primaryAlert: HomeAlert? = null,
    val primaryActionLabel: String = "Registrar movimiento",
    val monthlyIncome: BigDecimal = BigDecimal.ZERO,
    val monthlyExpenses: BigDecimal = BigDecimal.ZERO,
    val recentTransactions: List<TransactionResponse> =
        emptyList(),
    val error: String? = null,
    val dailyIncomePoints: List<Float> = emptyList(),
    val dailyIncomeLabels: List<String> = emptyList(),
    val dailyExpensePoints: List<Float> = emptyList(),
    val dailyExpenseLabels: List<String> = emptyList(),
    val featuredBudget: BudgetExecutionResponse? = null,
    val featuredSavingGoal: SavingGoalResponse? = null
) : com.elias.mismonedas.ui.viewmodel.RefreshableUiState

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
    private val savingGoalRepository: SavingGoalRepository
) : ViewModel() {

    var state by mutableStateOf(HomeUiState())
        private set

    private var currentUserId: Long? = null
    private var currentUserName: String = ""

    fun load(
        userId: Long,
        userName: String
    ) {
        currentUserId = userId
        currentUserName = userName

        viewModelScope.launch {
            val hasData = state.recentTransactions.isNotEmpty()
            state = state.copy(
                isLoading = !hasData,
                isRefreshing = hasData,
                userName = userName,
                error = null
            )

            try {
                val data =
                    repository.loadTransactionData()

                val primaryCurrency =
                    repository.getPrimaryWalletCurrency()

                val currentMonth = YearMonth.now()

                val monthlyTransactions =
                    data.transactions.filter { transaction ->
                        transaction.createdAt?.let {
                            YearMonth.from(it) == currentMonth
                        } == true
                    }

                val balance = data.transactions.fold(
                    BigDecimal.ZERO
                ) { accumulated, transaction ->
                    when (transaction.type) {
                        TransactionType.INCOME ->
                            accumulated + (transaction.amount ?: BigDecimal.ZERO)

                        TransactionType.SPENT ->
                            accumulated - (transaction.amount ?: BigDecimal.ZERO)

                        else -> accumulated
                    }
                }

                val monthlyIncome = monthlyTransactions
                    .filter { it.type == TransactionType.INCOME }
                    .fold(BigDecimal.ZERO) { total, item ->
                        total + (item.amount ?: BigDecimal.ZERO)
                    }

                val monthlyExpenses = monthlyTransactions
                    .filter { it.type == TransactionType.SPENT }
                    .fold(BigDecimal.ZERO) { total, item ->
                        total + (item.amount ?: BigDecimal.ZERO)
                    }

                val incomeSeries = monthlyTransactions
                    .filter { it.type == TransactionType.INCOME }
                    .toDailySeries()

                val expenseSeries = monthlyTransactions
                    .filter { it.type == TransactionType.SPENT }
                    .toDailySeries()

                val primaryAlert = when {
                    balance < BigDecimal.ZERO -> HomeAlert(
                        title = "Tu saldo está en rojo",
                        message = "Registra un ingreso o revisa tus gastos recientes para recuperar el equilibrio."
                    )
                    monthlyExpenses > monthlyIncome -> HomeAlert(
                        title = "Este mes gastaste más de lo que ingresaste",
                        message = "Revisa tus movimientos recientes para ver qué categoría pesa más."
                    )
                    else -> null
                }

                // Card de presupuesto y meta destacados (deterministas: no aleatorios)
                val featuredBudget = fetchFeaturedBudget()
                val featuredSavingGoal = fetchFeaturedSavingGoal(userId)

                state = HomeUiState(
                    isLoading = false,
                    isRefreshing = false,
                    userName = userName,
                    primaryBalance = balance,
                    primaryCurrency = primaryCurrency,
                    primaryAlert = primaryAlert,
                    primaryActionLabel = "Registrar movimiento",
                    monthlyIncome = monthlyIncome,
                    monthlyExpenses = monthlyExpenses,
                    recentTransactions = data.transactions.take(5),
                    dailyIncomePoints = incomeSeries.points,
                    dailyIncomeLabels = incomeSeries.labels,
                    dailyExpensePoints = expenseSeries.points,
                    dailyExpenseLabels = expenseSeries.labels,
                    featuredBudget = featuredBudget,
                    featuredSavingGoal = featuredSavingGoal
                )
            } catch (exception: Exception) {
                state = state.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = exception.message
                        ?: "No se pudo cargar el inicio"
                )
            }
        }
    }

    fun refresh() {
        val userId = currentUserId ?: return
        load(userId, currentUserName)
    }

    /**
     * Elige un presupuesto activo de forma determinista (el que está más cerca
     * de excederse). Antes se elegía uno al azar, lo que hacía que la card
     * cambiara en cada refresco.
     */
    private suspend fun fetchFeaturedBudget(): BudgetExecutionResponse? {
        return try {
            val currentMonthPeriod = buildCurrentMonthPeriod()
            val response = budgetRepository.listDetailed(currentMonthPeriod.start, currentMonthPeriod.end)
            val budgets = response.body()?.items?.filter {
                it.state == BudgetState.ACTIVE
            } ?: emptyList()
            budgets.maxByOrNull { it.executionPercentage ?: BigDecimal.ZERO }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Elige una meta de ahorro en curso de forma determinista (la más avanzada).
     * Antes se elegía una al azar.
     */
    private suspend fun fetchFeaturedSavingGoal(userId: Long): SavingGoalResponse? {
        return try {
            val response = savingGoalRepository.list(null)
            val goals = response.body()?.items?.filter {
                it.state == SavingGoalState.RUNNING
            } ?: emptyList()
            goals.maxByOrNull { it.accumulated ?: BigDecimal.ZERO }
        } catch (_: Exception) {
            null
        }
    }
}

private data class DailySeries(
    val points: List<Float>,
    val labels: List<String>
)

private fun List<TransactionResponse>.toDailySeries(): DailySeries {
    val dailyTotals = groupBy {
        it.createdAt?.toLocalDate()
    }
        .mapNotNull { (date, transactions) ->
            date ?: return@mapNotNull null
            date to transactions.fold(BigDecimal.ZERO) { total, item ->
                total + (item.amount ?: BigDecimal.ZERO)
            }
        }
        .sortedBy { it.first }

    return DailySeries(
        points = dailyTotals.map { it.second.toFloat() },
        labels = dailyTotals.map { it.first.toReadableDate() }
    )
}

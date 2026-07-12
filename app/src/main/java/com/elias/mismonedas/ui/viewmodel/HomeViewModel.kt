package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.BudgetRepository
import com.elias.mismonedas.data.repository.SavingGoalRepository
import com.elias.mismonedas.data.repository.TransactionRepository
import com.elias.mismonedas.ui.util.toReadableDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.SavingGoalResponse
import org.openapitools.client.models.TransactionResponse
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
    val randomBudget: BudgetExecutionResponse? = null,
    val randomSavingGoal: SavingGoalResponse? = null
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
                    repository.loadTransactionData(userId)

                val primaryCurrency =
                    repository.getPrimaryWalletCurrency(userId)

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
                        TransactionResponse.Type.INCOME ->
                            accumulated + (transaction.amount ?: BigDecimal.ZERO)

                        TransactionResponse.Type.SPENT ->
                            accumulated - (transaction.amount ?: BigDecimal.ZERO)

                        else -> accumulated
                    }
                }

                val monthlyIncome = monthlyTransactions
                    .filter { it.type == TransactionResponse.Type.INCOME }
                    .fold(BigDecimal.ZERO) { total, item ->
                        total + (item.amount ?: BigDecimal.ZERO)
                    }

                val monthlyExpenses = monthlyTransactions
                    .filter { it.type == TransactionResponse.Type.SPENT }
                    .fold(BigDecimal.ZERO) { total, item ->
                        total + (item.amount ?: BigDecimal.ZERO)
                    }

                val incomeSeries = monthlyTransactions
                    .filter { it.type == TransactionResponse.Type.INCOME }
                    .toDailySeries()

                val expenseSeries = monthlyTransactions
                    .filter { it.type == TransactionResponse.Type.SPENT }
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

                // FIX-005: Cargar presupuestos y metas aleatorios
                val randomBudget = fetchRandomBudget(userId)
                val randomSavingGoal = fetchRandomSavingGoal(userId)

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
                    randomBudget = randomBudget,
                    randomSavingGoal = randomSavingGoal
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

    private suspend fun fetchRandomBudget(userId: Long): BudgetExecutionResponse? {
        return try {
            val period = com.elias.mismonedas.ui.util.buildCurrentMonthPeriod(userId)
            val response = budgetRepository.getDetailedBudgets(period)
            val budgets = response.body()?.filter {
                it.state == BudgetExecutionResponse.State.ACTIVE
            } ?: emptyList()
            if (budgets.isNotEmpty()) budgets.random() else null
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun fetchRandomSavingGoal(userId: Long): SavingGoalResponse? {
        return try {
            val response = savingGoalRepository.getSavingGoals(userId)
            val goals = response.body()?.filter {
                it.state == SavingGoalResponse.State.RUNNING
            } ?: emptyList()
            if (goals.isNotEmpty()) goals.random() else null
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

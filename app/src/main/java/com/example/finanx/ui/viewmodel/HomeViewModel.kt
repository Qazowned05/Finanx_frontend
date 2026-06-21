package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.model.MovementType
import com.example.finanx.data.model.TransactionRecord
import com.example.finanx.data.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val balance: BigDecimal = BigDecimal.ZERO,
    val monthlyIncome: BigDecimal = BigDecimal.ZERO,
    val monthlyExpenses: BigDecimal = BigDecimal.ZERO,
    val recentTransactions: List<TransactionRecord> =
        emptyList(),
    val error: String? = null,
    val dailyIncomePoints: List<Float> = emptyList(),
    val dailyExpensePoints: List<Float> = emptyList()
)

class HomeViewModel(
    private val repository: TransactionRepository =
        TransactionRepository()
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
            state = state.copy(
                isLoading = true,
                userName = userName,
                error = null
            )

            try {
                val data =
                    repository.loadTransactionData(userId)

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
                        MovementType.INCOME ->
                            accumulated + transaction.amount

                        MovementType.SPENT ->
                            accumulated - transaction.amount
                    }
                }

                val monthlyIncome = monthlyTransactions
                    .filter { it.type == MovementType.INCOME }
                    .fold(BigDecimal.ZERO) { total, item ->
                        total + item.amount
                    }

                val monthlyExpenses = monthlyTransactions
                    .filter { it.type == MovementType.SPENT }
                    .fold(BigDecimal.ZERO) { total, item ->
                        total + item.amount
                    }

                val incomePointsList = monthlyTransactions
                    .filter { it.type == MovementType.INCOME }
                    .map { it.amount.toFloat() }

                val expensePointsList = monthlyTransactions
                    .filter { it.type == MovementType.SPENT }
                    .map { it.amount.toFloat() }

                state = HomeUiState(
                    isLoading = false,
                    userName = userName,
                    balance = balance,
                    monthlyIncome = monthlyIncome,
                    monthlyExpenses = monthlyExpenses,
                    recentTransactions = data.transactions.take(5),
                    dailyIncomePoints = incomePointsList,
                    dailyExpensePoints = expensePointsList
                )
            } catch (exception: Exception) {
                state = state.copy(
                    isLoading = false,
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
}
package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.models.Transaction
import com.example.finanx.data.repositories.TransactionRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.OffsetDateTime

sealed class TransactionState {
    object Loading : TransactionState()
    data class Success(val income: List<Transaction>, val expenses: List<Transaction>) : TransactionState()
    data class Error(val message: String) : TransactionState()
}

class TransactionViewModel : ViewModel() {
    var transactionState by mutableStateOf<TransactionState>(TransactionState.Loading)
        private set

    fun fetchTransactions(userId: Long) {
        viewModelScope.launch {
            transactionState = TransactionState.Loading
            try {
                val transactions = TransactionRepository.getTransactions(userId)
                val income = transactions.filter { it.type == "INCOME" }
                val expenses = transactions.filter { it.type == "EXPENSE" }
                transactionState = TransactionState.Success(income, expenses)
            } catch (e: Exception) {
                transactionState = TransactionState.Error("Error al cargar las transacciones")
            }
        }
    }

    fun createTransaction(
        userId: Long,
        description: String,
        amount: Double,
        type: String,
        categoryId: Long? // Optional categoryId
    ) {
        viewModelScope.launch {
            try {
                val newTransaction = Transaction(
                    id = 0,
                    userId = userId,
                    description = description,
                    amount = BigDecimal.valueOf(amount),
                    type = type,
                    categoryId = categoryId ?: 1,
                    reasonId = null,
                    transactionScheduleId = null,
                    budgetId = null,
                    savingGoalId = null,
                    paymentMethod = "CASH",
                    createdAt = OffsetDateTime.now(),
                    active = true,
                    disabledAt = null
                )
                TransactionRepository.createTransaction(newTransaction)
                fetchTransactions(userId)
            } catch (e: Exception) {
            }
        }
    }
}

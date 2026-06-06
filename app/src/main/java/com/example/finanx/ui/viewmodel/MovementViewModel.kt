package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.finanx.dto.category.CategoryResponse
import com.elias.finanx.dto.transaction.TransactionRequest
import com.elias.finanx.dto.transaction.TransactionResponse
import com.example.finanx.data.repository.CategoryRepository
import com.example.finanx.data.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal

sealed class MovementState {
    object Loading : MovementState()
    data class Success(val income: List<TransactionResponse>, val expenses: List<TransactionResponse>) : MovementState()
    data class Error(val message: String) : MovementState()
}

sealed class CategoryState {
    object Loading : CategoryState()
    data class Success(val categories: List<CategoryResponse>) : CategoryState()
    data class Error(val message: String) : CategoryState()
}

class MovementViewModel : ViewModel() {
    private val transactionRepository = TransactionRepository()
    private val categoryRepository = CategoryRepository()

    var movementState by mutableStateOf<MovementState>(MovementState.Loading)
        private set

    var categoryState by mutableStateOf<CategoryState>(CategoryState.Loading)
        private set

    fun fetchMovements(userId: Long) {
        viewModelScope.launch {
            movementState = MovementState.Loading
            try {
                val response = transactionRepository.getTransactions(userId)
                if (response.isSuccessful && response.body() != null) {
                    val transactions = response.body()!!
                    val income = transactions.filter { it.type == "INCOME" }
                    val expenses = transactions.filter { it.type == "EXPENSE" }
                    movementState = MovementState.Success(income, expenses)
                } else {
                    movementState = MovementState.Error("Error al obtener los movimientos")
                }
            } catch (e: Exception) {
                movementState = MovementState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun fetchCategories(userId: Long) {
        viewModelScope.launch {
            categoryState = CategoryState.Loading
            try {
                val response = categoryRepository.getCategories(userId)
                if (response.isSuccessful && response.body() != null) {
                    categoryState = CategoryState.Success(response.body()!!)
                } else {
                    categoryState = CategoryState.Error("Error al obtener las categorías")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createMovement(
        userId: Long,
        description: String,
        amount: BigDecimal,
        type: String,
        categoryId: Long,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            try {
                val request = TransactionRequest(
                    description = description,
                    amount = amount,
                    type = type,
                    userId = userId,
                    categoryId = categoryId,
                    paymentMethod = paymentMethod
                )
                val response = transactionRepository.createTransaction(request)
                if (response.isSuccessful) {
                    fetchMovements(userId)
                } else {
                }
            } catch (e: Exception) {
            }
        }
    }
}

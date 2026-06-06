package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.finanx.dto.transaction.TransactionRequest
import com.elias.finanx.dto.transaction.TransactionResponse
import com.example.finanx.data.repositories.TransactionRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.OffsetDateTime

// Cambiando el nombre para reflejar "Movimientos"
sealed class MovementState {
    object Loading : MovementState()
    data class Success(val income: List<TransactionResponse>, val expenses: List<TransactionResponse>) : MovementState()
    data class Error(val message: String) : MovementState()
}

// Cambiando el nombre a MovementViewModel
class MovementViewModel : ViewModel() {
    private val repository = TransactionRepository()

    var movementState by mutableStateOf<MovementState>(MovementState.Loading)
        private set

    fun fetchMovements(userId: Long) {
        viewModelScope.launch {
            movementState = MovementState.Loading
            try {
                val response = repository.getTransactions(userId)
                if (response.isSuccessful && response.body() != null) {
                    val movements = response.body()!!
                    val income = movements.filter { it.type == "INCOME" }
                    val expenses = movements.filter { it.type == "EXPENSE" }
                    movementState = MovementState.Success(income, expenses)
                } else {
                    movementState = MovementState.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                movementState = MovementState.Error("Error al cargar los movimientos: ${e.message}")
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
                val newMovementRequest = TransactionRequest(
                    userId = userId,
                    description = description,
                    amount = amount,
                    type = type,
                    categoryId = categoryId,
                    paymentMethod = paymentMethod,
                    issueDate = OffsetDateTime.now()
                )
                val response = repository.createTransaction(newMovementRequest)
                if (response.isSuccessful) {
                    fetchMovements(userId)

                }
            } catch (e: Exception) {

            }
        }
    }
}

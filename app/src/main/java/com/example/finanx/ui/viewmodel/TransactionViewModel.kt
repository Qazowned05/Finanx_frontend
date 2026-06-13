package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.repository.CategoryRepository
import com.example.finanx.data.repository.TransactionRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse
import java.math.BigDecimal

// Estados para la UI, ahora usando los modelos de OpenAPI
sealed class TransactionState {
    object Loading : TransactionState()
    data class Success(val income: List<TransactionResponse>, val expenses: List<TransactionResponse>) : TransactionState()
    data class Error(val message: String) : TransactionState()
}

class TransactionViewModel : ViewModel() {
    private val transactionRepository = TransactionRepository()
    private val categoryRepository = CategoryRepository()

    var transactionState by mutableStateOf<TransactionState>(TransactionState.Loading)
        private set

    var categoryState by mutableStateOf<CategoryState>(CategoryState.Loading)
        private set

    fun fetchTransactions(userId: Long) {
        viewModelScope.launch {
            transactionState = TransactionState.Loading
            try {
                val response = transactionRepository.getTransactions(userId)
                if (response.isSuccessful && response.body() != null) {
                    val transactions = response.body()!!
                    val income = transactions.filter { it.type == TransactionResponse.Type.INCOME }
                    val expenses = transactions.filter { it.type == TransactionResponse.Type.SPENT }
                    transactionState = TransactionState.Success(income, expenses)
                } else {
                    transactionState = TransactionState.Error("Error al obtener las transacciones: ${response.message()}")
                }
            } catch (e: Exception) {
                transactionState = TransactionState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun fetchCategories(userId: Long) {
        viewModelScope.launch {
            categoryState = CategoryState.Loading
            try {
                val response = categoryRepository.getActiveCategories(userId)
                if (response.isSuccessful && response.body() != null) {
                    categoryState = CategoryState.Success(response.body()!!)
                } else {
                    categoryState = CategoryState.Error("Error al obtener las categorías: ${response.message()}")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createTransaction(
        userId: Long,
        description: String,
        amount: BigDecimal,
        type: String, // "SPENT" o "INCOME"
        categoryId: Long,
        paymentMethod: String // "CASH", "CREDIT_CARD", etc.
    ) {
        viewModelScope.launch {
            try {
                val transactionType = TransactionRequest.Type.valueOf(type)
                val transactionPaymentMethod = TransactionRequest.PaymentMethod.valueOf(paymentMethod)

                val request = TransactionRequest(
                    description = description,
                    amount = amount,
                    type = transactionType,
                    userId = userId,
                    categoryId = categoryId,
                    paymentMethod = transactionPaymentMethod
                )
                val response = transactionRepository.createTransaction(request)
                if (response.isSuccessful) {
                    fetchTransactions(userId)
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}

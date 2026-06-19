package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.repository.BudgetRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse

sealed class BudgetState {
    object Loading : BudgetState()
    data class Success(val budgets: List<BudgetResponse>) : BudgetState()
    data class Error(val message: String) : BudgetState()
}

sealed class BudgetActionState {
    object Idle : BudgetActionState()
    object Loading : BudgetActionState()
    object Success : BudgetActionState()
    data class Error(val message: String) : BudgetActionState()
}

class BudgetViewModel : ViewModel() {
    private val repository = BudgetRepository()

    var budgetState by mutableStateOf<BudgetState>(BudgetState.Loading)
        private set

    var actionState by mutableStateOf<BudgetActionState>(BudgetActionState.Idle)
        private set

    fun fetchBudgets(userId: Long) {
        viewModelScope.launch {
            budgetState = BudgetState.Loading
            try {
                val response = repository.getBudgets(userId)
                if (response.isSuccessful && response.body() != null) {
                    budgetState = BudgetState.Success(response.body()!!)
                } else {
                    budgetState = BudgetState.Error("Error al cargar presupuestos: ${response.code()}")
                }
            } catch (e: Exception) {
                budgetState = BudgetState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createBudget(request: BudgetRequest, userId: Long) {
        viewModelScope.launch {
            actionState = BudgetActionState.Loading
            try {
                val response = repository.createBudget(request)
                if (response.isSuccessful) {
                    actionState = BudgetActionState.Success
                    fetchBudgets(userId)
                } else {
                    actionState = BudgetActionState.Error("No se pudo crear: ${response.message()}")
                }
            } catch (e: Exception) {
                actionState = BudgetActionState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun cancelBudget(id: Long, userId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.cancelBudget(id)
                if (response.isSuccessful) fetchBudgets(userId)
            } catch (e: Exception) {
                budgetState = BudgetState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun disableBudget(id: Long, userId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.disableBudget(id)
                if (response.isSuccessful) fetchBudgets(userId)
            } catch (e: Exception) {
                budgetState = BudgetState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun resetActionState() {
        actionState = BudgetActionState.Idle
    }
}

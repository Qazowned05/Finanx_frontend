package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.repository.BudgetRepository
import com.example.finanx.ui.util.buildCurrentMonthPeriod
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse

sealed class BudgetState : com.example.finanx.ui.viewmodel.RefreshableUiState {
    object Loading : BudgetState() { override val isRefreshing: Boolean = false }
    data class Success(val budgets: List<BudgetExecutionResponse>, override val isRefreshing: Boolean = false) : BudgetState()
    data class Error(val message: String) : BudgetState() { override val isRefreshing: Boolean = false }
}

sealed class SimpleBudgetState : com.example.finanx.ui.viewmodel.RefreshableUiState {
    object Loading : SimpleBudgetState() { override val isRefreshing: Boolean = false }
    data class Success(val budgets: List<BudgetResponse>, override val isRefreshing: Boolean = false) : SimpleBudgetState()
    data class Error(val message: String) : SimpleBudgetState() { override val isRefreshing: Boolean = false }
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

    var simpleBudgetState by mutableStateOf<SimpleBudgetState>(SimpleBudgetState.Loading)
        private set

    var actionState by mutableStateOf<BudgetActionState>(BudgetActionState.Idle)
        private set

    fun fetchDetailedBudgets(userId: Long, force: Boolean = false) {
        if (!force && budgetState is BudgetState.Success) return
        viewModelScope.launch {
            val hasData = budgetState is BudgetState.Success && (budgetState as BudgetState.Success).budgets.isNotEmpty()
            budgetState = if (hasData) (budgetState as BudgetState.Success).copy(isRefreshing = true)
            else BudgetState.Loading
            budgetState = try {
                val response = repository.getDetailedBudgets(buildCurrentMonthPeriod(userId))
                if (response.isSuccessful && response.body() != null) {
                    BudgetState.Success(response.body()!!, isRefreshing = false)
                } else {
                    BudgetState.Error("Error al cargar presupuestos: ${response.code()}")
                }
            } catch (e: Exception) {
                BudgetState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun fetchBudgets(userId: Long) {
        viewModelScope.launch {
            val hasData = simpleBudgetState is SimpleBudgetState.Success && (simpleBudgetState as SimpleBudgetState.Success).budgets.isNotEmpty()
            simpleBudgetState = if (hasData) (simpleBudgetState as SimpleBudgetState.Success).copy(isRefreshing = true)
            else SimpleBudgetState.Loading
            simpleBudgetState = try {
                val response = repository.getBudgets(userId)
                if (response.isSuccessful && response.body() != null) {
                    SimpleBudgetState.Success(response.body()!!, isRefreshing = false)
                } else {
                    SimpleBudgetState.Error("Error al cargar presupuestos: ${response.code()}")
                }
            } catch (e: Exception) {
                SimpleBudgetState.Error("Error de red: ${e.message}")
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
                    fetchDetailedBudgets(userId, force = true)
                } else {
                    actionState = BudgetActionState.Error("No se pudo crear: ${response.message()}")
                }
            } catch (e: Exception) {
                actionState = BudgetActionState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun updateBudget(id: Long, request: BudgetRequest, userId: Long) {
        viewModelScope.launch {
            actionState = BudgetActionState.Loading
            try {
                val response = repository.updateBudget(id, request)
                if (response.isSuccessful) {
                    actionState = BudgetActionState.Success
                    fetchDetailedBudgets(userId, force = true)
                } else {
                    actionState = BudgetActionState.Error("No se pudo actualizar: ${response.message()}")
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
                if (response.isSuccessful) fetchDetailedBudgets(userId, force = true)
            } catch (e: Exception) {
                budgetState = BudgetState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun disableBudget(id: Long, userId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.disableBudget(id)
                if (response.isSuccessful) fetchDetailedBudgets(userId, force = true)
            } catch (e: Exception) {
                budgetState = BudgetState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun resetActionState() {
        actionState = BudgetActionState.Idle
    }
}
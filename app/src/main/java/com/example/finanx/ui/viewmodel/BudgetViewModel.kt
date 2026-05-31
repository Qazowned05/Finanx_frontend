package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.model.BudgetResponse
import com.example.finanx.data.repository.BudgetRepository
import kotlinx.coroutines.launch

sealed class BudgetState {
    object Loading : BudgetState()
    data class Success(val budgets: List<BudgetResponse>) : BudgetState()
    data class Error(val message: String) : BudgetState()
}

class BudgetViewModel : ViewModel() {
    private val repository = BudgetRepository(RetrofitClient.api)

    var budgetState by mutableStateOf<BudgetState>(BudgetState.Loading)
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
}

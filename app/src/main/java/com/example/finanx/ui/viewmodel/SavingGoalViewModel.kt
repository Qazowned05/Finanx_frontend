package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.repository.SavingGoalRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

sealed class SavingGoalState {
    object Loading : SavingGoalState()
    data class Success(val goals: List<SavingGoalResponse>) : SavingGoalState()
    data class Error(val message: String) : SavingGoalState()
}

class SavingGoalViewModel : ViewModel() {
    private val repository = SavingGoalRepository(RetrofitClient.savingGoalApi)

    var savingGoalState by mutableStateOf<SavingGoalState>(SavingGoalState.Loading)
        private set

    var actionSuccess by mutableStateOf<String?>(null)
        private set

    fun fetchSavingGoals(userId: Long) {
        viewModelScope.launch {
            savingGoalState = SavingGoalState.Loading
            try {
                val response = repository.getSavingGoals(userId)
                if (response.isSuccessful && response.body() != null) {
                    savingGoalState = SavingGoalState.Success(response.body()!!)
                } else {
                    savingGoalState = SavingGoalState.Error("Error al cargar metas: ${response.code()}")
                }
            } catch (e: Exception) {
                savingGoalState = SavingGoalState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createSavingGoal(userId: Long, description: String, targetAmount: BigDecimal, deadline: OffsetDateTime?) {
        viewModelScope.launch {
            try {
                val request = SavingGoalRequest(
                    userId = userId,
                    description = description,
                    targetAmount = targetAmount,
                    deadline = deadline?.toLocalDate() ?: LocalDate.now()
                )
                val response = repository.createSavingGoal(request)
                if (response.isSuccessful) {
                    actionSuccess = "Meta creada"
                    fetchSavingGoals(userId)
                } else {
                    savingGoalState = SavingGoalState.Error("Error al crear: ${response.code()}")
                }
            } catch (e: Exception) {
                savingGoalState = SavingGoalState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun updateSavingGoal(userId: Long, goalId: Long, description: String, targetAmount: BigDecimal, deadline: OffsetDateTime?) {
        viewModelScope.launch {
            try {
                val request = SavingGoalRequest(
                    userId = userId,
                    description = description,
                    targetAmount = targetAmount,
                    deadline = deadline?.toLocalDate() ?: LocalDate.now()
                )
                val response = repository.updateSavingGoal(goalId, request)
                if (response.isSuccessful) {
                    actionSuccess = "Meta actualizada"
                    fetchSavingGoals(userId)
                } else {
                    savingGoalState = SavingGoalState.Error("Error al actualizar: ${response.code()}")
                }
            } catch (e: Exception) {
                savingGoalState = SavingGoalState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun deleteSavingGoal(userId: Long, goalId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.deleteSavingGoal(goalId)
                if (response.isSuccessful) {
                    actionSuccess = "Meta eliminada"
                    fetchSavingGoals(userId)
                } else {
                    savingGoalState = SavingGoalState.Error("Error al eliminar: ${response.code()}")
                }
            } catch (e: Exception) {
                savingGoalState = SavingGoalState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun clearActionSuccess() {
        actionSuccess = null
    }
}
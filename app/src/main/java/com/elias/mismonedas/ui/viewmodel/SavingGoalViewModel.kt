package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.SavingGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

sealed class SavingGoalState : com.elias.mismonedas.ui.viewmodel.RefreshableUiState {
    object Loading : SavingGoalState() { override val isRefreshing: Boolean = false }
    data class Success(val goals: List<SavingGoalResponse>, override val isRefreshing: Boolean = false) : SavingGoalState()
    data class Error(val message: String) : SavingGoalState() { override val isRefreshing: Boolean = false }
}

@HiltViewModel
class SavingGoalViewModel @Inject constructor(
    private val repository: SavingGoalRepository
) : ViewModel() {


    var savingGoalState by mutableStateOf<SavingGoalState>(SavingGoalState.Loading)
        private set

    var actionSuccess by mutableStateOf<String?>(null)
        private set

    var formError by mutableStateOf<String?>(null)
        private set

    fun fetchSavingGoals(userId: Long) {
        viewModelScope.launch {
            val hasData = savingGoalState is SavingGoalState.Success && (savingGoalState as SavingGoalState.Success).goals.isNotEmpty()
            savingGoalState = if (hasData) (savingGoalState as SavingGoalState.Success).copy(isRefreshing = true)
            else SavingGoalState.Loading
            try {
                val response = repository.getSavingGoals(userId)
                if (response.isSuccessful && response.body() != null) {
                    savingGoalState = SavingGoalState.Success(response.body()!!, isRefreshing = false)
                } else {
                    savingGoalState = SavingGoalState.Error("Error al cargar metas: ${response.code()}")
                }
            } catch (e: Exception) {
                savingGoalState = SavingGoalState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createSavingGoal(
        userId: Long,
        description: String,
        targetAmount: BigDecimal,
        deadline: LocalDate?
    ) {
        viewModelScope.launch {
            try {
                val request = SavingGoalRequest(
                    userId = userId,
                    description = description,
                    targetAmount = targetAmount,
                    deadline = deadline ?: LocalDate.now().plusMonths(6)
                )
                val response = repository.createSavingGoal(request)
                if (response.isSuccessful) {
                    formError = null
                    actionSuccess = "Meta creada"
                    fetchSavingGoals(userId)
                } else {
                    formError = "No se pudo crear la meta: ${response.code()}"
                }
            } catch (e: Exception) {
                formError = "Error de red: ${e.message}"
            }
        }
    }

    fun updateSavingGoal(
        userId: Long,
        goalId: Long,
        description: String,
        targetAmount: BigDecimal,
        deadline: LocalDate?
    ) {
        viewModelScope.launch {
            try {
                val request = SavingGoalRequest(
                    userId = userId,
                    description = description,
                    targetAmount = targetAmount,
                    deadline = deadline ?: LocalDate.now().plusMonths(6)
                )
                val response = repository.updateSavingGoal(goalId, request)
                if (response.isSuccessful) {
                    formError = null
                    actionSuccess = "Meta actualizada"
                    fetchSavingGoals(userId)
                } else {
                    formError = "No se pudo actualizar: ${response.code()}"
                }
            } catch (e: Exception) {
                formError = "Error de red: ${e.message}"
            }
        }
    }

    fun deleteSavingGoal(userId: Long, goalId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.disableSavingGoal(goalId)
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

    fun clearFormError() {
        formError = null
    }
}

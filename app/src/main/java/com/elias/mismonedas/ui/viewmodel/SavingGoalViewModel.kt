package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.SavingGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.PageResponseSavingGoalResponse
import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse
import org.openapitools.client.models.SavingGoalState
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

sealed class SavingGoalUIState : RefreshableUiState {
    object Loading : SavingGoalUIState() { override val isRefreshing: Boolean = false }
    data class Success(
        val goals: List<SavingGoalResponse>,
        override val isRefreshing: Boolean = false,
        val page: Int = 0,
        val hasMore: Boolean = true,
        val isLoadingMore: Boolean = false
    ) : SavingGoalUIState()
    data class Error(val message: String) : SavingGoalUIState() { override val isRefreshing: Boolean = false }
}

@HiltViewModel
class SavingGoalViewModel @Inject constructor(
    private val repository: SavingGoalRepository
) : ViewModel() {


    var savingGoalUIState by mutableStateOf<SavingGoalUIState>(SavingGoalUIState.Loading)
        private set

    var actionSuccess by mutableStateOf<String?>(null)
        private set

    var formError by mutableStateOf<String?>(null)
        private set

    var stateFilter by mutableStateOf<SavingGoalState?>(null)
        private set

    private var currentUserId: Long? = null

    fun fetchSavingGoals(userId: Long) {
        currentUserId = userId
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val current = savingGoalUIState
            val hasData = current is SavingGoalUIState.Success && current.goals.isNotEmpty()
            savingGoalUIState = if (hasData) {
                (current as SavingGoalUIState.Success).copy(isRefreshing = true)
            } else {
                SavingGoalUIState.Loading
            }

            try {
                val response = repository.listSavingGoalPage(
                    active = true,
                    state = stateFilter,
                    page = 0
                )
                savingGoalUIState = SavingGoalUIState.Success(
                    goals = response.items ?: emptyList(),
                    isRefreshing = false,
                    page = response.pagination?.page ?: 0,
                    hasMore = response.hasMorePages()
                )
            } catch (e: Exception) {
                savingGoalUIState = SavingGoalUIState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun loadMore() {
        val current = savingGoalUIState as? SavingGoalUIState.Success ?: return
        if (!current.hasMore || current.isLoadingMore || current.isRefreshing) return

        viewModelScope.launch {
            savingGoalUIState = current.copy(isLoadingMore = true)
            try {
                val next = current.page + 1
                val response = repository.listSavingGoalPage(
                    active = true,
                    state = stateFilter,
                    page = next
                )
                val latest = savingGoalUIState as? SavingGoalUIState.Success ?: return@launch
                savingGoalUIState = latest.copy(
                    goals = latest.goals + (response.items ?: emptyList()),
                    page = response.pagination?.page ?: next,
                    hasMore = response.hasMorePages(),
                    isLoadingMore = false
                )
            } catch (_: Exception) {
                val latest = savingGoalUIState as? SavingGoalUIState.Success
                if (latest != null) {
                    savingGoalUIState = latest.copy(isLoadingMore = false)
                }
            }
        }
    }

    fun onStateFilterChange(state: SavingGoalState?) {
        if (stateFilter == state) return
        stateFilter = state
        savingGoalUIState = SavingGoalUIState.Loading
        load()
    }

    private fun PageResponseSavingGoalResponse.hasMorePages(): Boolean {
        val current = pagination?.page ?: 0
        val total = pagination?.totalPages ?: 1
        return current + 1 < total
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
                    savingGoalUIState = SavingGoalUIState.Error("Error al eliminar: ${response.code()}")
                }
            } catch (e: Exception) {
                savingGoalUIState = SavingGoalUIState.Error("Error de red: ${e.message}")
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

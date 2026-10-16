package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.elias.mismonedas.ui.util.buildCurrentMonthPeriod
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetHealth
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.BudgetState
import org.openapitools.client.models.PageResponseBudgetResponse
import java.time.LocalDate
import javax.inject.Inject

sealed class BudgetUIState : RefreshableUiState {
    object Loading : BudgetUIState() { override val isRefreshing: Boolean = false }
    data class Success(val budgets: List<BudgetExecutionResponse>, override val isRefreshing: Boolean = false) : BudgetUIState()
    data class Error(val message: String) : BudgetUIState() { override val isRefreshing: Boolean = false }
}

sealed class SimpleBudgetState : RefreshableUiState {
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

data class BudgetListUiState(
    val isLoading: Boolean = false,
    override val isRefreshing: Boolean = false,
    val budgets: List<BudgetResponse> = emptyList(),
    val filterState: BudgetState? = null,
    val filterHealth: BudgetHealth? = null,
    val filterFrom: LocalDate? = null,
    val filterTo: LocalDate? = null,
    val page: Int = 0,
    val hasMore: Boolean = true,
    val isLoadingMore: Boolean = false,
    val error: String? = null
) : RefreshableUiState

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    var budgetUIState by mutableStateOf<BudgetUIState>(BudgetUIState.Loading)
        private set

    var simpleBudgetState by mutableStateOf<SimpleBudgetState>(SimpleBudgetState.Loading)
        private set

    var actionState by mutableStateOf<BudgetActionState>(BudgetActionState.Idle)
        private set

    var budgetListState by mutableStateOf(BudgetListUiState())
        private set

    private var budgetListUserId: Long? = null

    fun fetchBudgetPage(userId: Long, force: Boolean = false) {
        budgetListUserId = userId
        if (!force && budgetListState.budgets.isNotEmpty()) return
        viewModelScope.launch {
            val hasData = budgetListState.budgets.isNotEmpty()
            budgetListState = budgetListState.copy(
                isLoading = !hasData,
                isRefreshing = hasData,
                error = null
            )
            try {
                val page = repository.listBudgetPage(
                    state = budgetListState.filterState,
                    health = budgetListState.filterHealth,
                    from = budgetListState.filterFrom,
                    to = budgetListState.filterTo,
                    page = 0
                )
                budgetListState = budgetListState.copy(
                    isLoading = false,
                    isRefreshing = false,
                    budgets = page.items ?: emptyList(),
                    page = page.pagination?.page ?: 0,
                    hasMore = page.hasMorePages(),
                    isLoadingMore = false,
                    error = null
                )
            } catch (exception: Exception) {
                budgetListState = budgetListState.copy(
                    isLoading = false,
                    isRefreshing = false,
                    isLoadingMore = false,
                    error = exception.message
                        ?: "No se pudieron cargar los presupuestos"
                )
            }
        }
    }

    fun refreshBudgetList() {
        budgetListUserId?.let { fetchBudgetPage(it, force = true) }
    }

    fun loadMore() {
        if (budgetListUserId == null) return
        if (!budgetListState.hasMore || budgetListState.isLoadingMore ||
            budgetListState.isLoading || budgetListState.isRefreshing
        ) return

        viewModelScope.launch {
            budgetListState = budgetListState.copy(isLoadingMore = true)
            try {
                val next = budgetListState.page + 1
                val page = repository.listBudgetPage(
                    state = budgetListState.filterState,
                    health = budgetListState.filterHealth,
                    from = budgetListState.filterFrom,
                    to = budgetListState.filterTo,
                    page = next
                )
                budgetListState = budgetListState.copy(
                    budgets = budgetListState.budgets + (page.items ?: emptyList()),
                    page = page.pagination?.page ?: next,
                    hasMore = page.hasMorePages(),
                    isLoadingMore = false,
                    error = null
                )
            } catch (exception: Exception) {
                budgetListState = budgetListState.copy(
                    isLoadingMore = false,
                    error = exception.message
                        ?: "No se pudieron cargar más presupuestos"
                )
            }
        }
    }

    fun setStateFilter(state: BudgetState?) {
        applyBudgetFilters(budgetListState.copy(filterState = state))
    }

    fun setHealthFilter(health: BudgetHealth?) {
        applyBudgetFilters(budgetListState.copy(filterHealth = health))
    }

    fun setDateFilter(from: LocalDate?, to: LocalDate?) {
        applyBudgetFilters(budgetListState.copy(filterFrom = from, filterTo = to))
    }

    fun clearFilters() {
        applyBudgetFilters(
            budgetListState.copy(
                filterState = null,
                filterHealth = null,
                filterFrom = null,
                filterTo = null
            )
        )
    }

    private fun applyBudgetFilters(newState: BudgetListUiState) {
        budgetListState = newState.copy(
            budgets = emptyList(),
            page = 0,
            hasMore = true,
            isLoading = true,
            isRefreshing = false,
            isLoadingMore = false,
            error = null
        )
        budgetListUserId?.let { fetchBudgetPage(it, force = true) }
    }

    private fun PageResponseBudgetResponse.hasMorePages(): Boolean =
        (pagination?.page ?: 0) + 1 < (pagination?.totalPages ?: 1)

    fun fetchDetailedBudgets(userId: Long, force: Boolean = false) {
        if (!force && budgetUIState is BudgetUIState.Success) return
        viewModelScope.launch {
            val hasData = budgetUIState is BudgetUIState.Success && (budgetUIState as BudgetUIState.Success).budgets.isNotEmpty()
            budgetUIState = if (hasData) (budgetUIState as BudgetUIState.Success).copy(isRefreshing = true)
            else BudgetUIState.Loading
            budgetUIState = try {
                val period = buildCurrentMonthPeriod()
                val response = repository.listDetailed(period.start, period.end)
                if (response.isSuccessful && response.body() != null) {
                    BudgetUIState.Success(response.body()!!.items ?: emptyList(), isRefreshing = false)
                } else {
                    BudgetUIState.Error("Error al cargar presupuestos: ${response.code()}")
                }
            } catch (e: Exception) {
                BudgetUIState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun fetchBudgets(userId: Long) {
        viewModelScope.launch {
            val hasData = simpleBudgetState is SimpleBudgetState.Success && (simpleBudgetState as SimpleBudgetState.Success).budgets.isNotEmpty()
            simpleBudgetState = if (hasData) (simpleBudgetState as SimpleBudgetState.Success).copy(isRefreshing = true)
            else SimpleBudgetState.Loading
            simpleBudgetState = try {
                val response = repository.list()
                if (response.isSuccessful && response.body() != null) {
                    SimpleBudgetState.Success(response.body()!!.items ?: emptyList(), isRefreshing = false)
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
                    fetchBudgetPage(userId, force = true)
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
                    fetchBudgetPage(userId, force = true)
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
                if (response.isSuccessful) {
                    fetchDetailedBudgets(userId, force = true)
                    fetchBudgetPage(userId, force = true)
                }
            } catch (e: Exception) {
                budgetUIState = BudgetUIState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun disableBudget(id: Long, userId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.disableBudget(id)
                if (response.isSuccessful) {
                    fetchDetailedBudgets(userId, force = true)
                    fetchBudgetPage(userId, force = true)
                }
            } catch (e: Exception) {
                budgetUIState = BudgetUIState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun resetActionState() {
        actionState = BudgetActionState.Idle
    }
}
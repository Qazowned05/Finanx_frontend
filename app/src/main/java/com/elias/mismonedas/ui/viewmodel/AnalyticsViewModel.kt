package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetsDashboard
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.TransactionsDashboard
import java.time.LocalDate
import javax.inject.Inject

data class AnalyticsUiState(
    val isLoading: Boolean        = false,
    val dashboard: TransactionsDashboard? = null,
    val error: String?            = null,

    val budgetsDashboard: BudgetsDashboard? = null,
    val isBudgetsLoading: Boolean = false,
    val budgetsError: String? = null,

    val startDate: LocalDate = LocalDate.now().minusDays(30),
    val endDate:   LocalDate = LocalDate.now(),
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: AnalyticsRepository
) : ViewModel() {

    var state by mutableStateOf(AnalyticsUiState())
        private set

    fun setStartDate(date: LocalDate) { state = state.copy(startDate = date) }
    fun setEndDate(date: LocalDate)   { state = state.copy(endDate   = date) }

    fun loadDashboard(userId: Long) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error     = null
            )

            val periodRequest = PeriodRequest(
                start  = state.startDate,
                end    = state.endDate,
                userId = userId
            )

            try {
                val response = repository.getBalanceDashboard(
                    period = periodRequest
                )

                val dashboard = response.body() as TransactionsDashboard

                state = if (response.isSuccessful) {
                    state.copy(
                        isLoading   = false,
                        dashboard   = dashboard,
                        error       = null
                    )
                } else {
                    state.copy(
                        isLoading   = false,
                        error       = "Error ${response.code()}: no se pudo cargar el panel."
                    )
                }

            } catch (e: Exception) {
                state = state.copy(
                    isLoading   = false,
                    error       = "Error de red: ${e.message}"
                )
            }
        }
    }

    fun loadBudgetsDashboard(userId: Long) {
        viewModelScope.launch {
            state = state.copy(isBudgetsLoading = true, budgetsError = null)
            val periodRequest = PeriodRequest(
                start = state.startDate,
                end = state.endDate,
                userId = userId
            )
            try {
                val response = repository.getBudgetsDashboard(periodRequest)
                val body = response.body()
                state = if (response.isSuccessful && body != null) {
                    state.copy(isBudgetsLoading = false, budgetsDashboard = body, budgetsError = null)
                } else {
                    state.copy(isBudgetsLoading = false, budgetsError = "Error ${response.code()}: no se pudo cargar el panel de presupuestos.")
                }
            } catch (e: Exception) {
                state = state.copy(isBudgetsLoading = false, budgetsError = "Error de red: ${e.message}")
            }
        }
    }

    fun refresh(userId: Long) = loadDashboard(userId)
}
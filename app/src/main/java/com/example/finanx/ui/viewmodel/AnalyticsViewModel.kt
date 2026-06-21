package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.repository.AnalyticsRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.DashboardResponse
import org.openapitools.client.models.PeriodRequest
import java.time.LocalDate

data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val dashboard: DashboardResponse? = null,
    val error: String? = null
)

class AnalyticsViewModel : ViewModel() {
    var dashboardResponse by mutableStateOf<DashboardResponse?>(null)

    private val repository = AnalyticsRepository(RetrofitClient.analyticsApi)

    var analyticsState by mutableStateOf(AnalyticsUiState())
        private set

    fun loadDashboard(userId: Long) {
        viewModelScope.launch {
            analyticsState = analyticsState.copy(isLoading = true, error = null)

            val end = LocalDate.now()
            val start = end.minusDays(30)

            try {
                val response = repository.getDashboard(PeriodRequest(start = start, end = end, userId = userId))

                analyticsState = if (response.isSuccessful) {
                    analyticsState.copy(
                        isLoading = false,
                        dashboard = response.body() as DashboardResponse?,
                        error = null
                    )
                } else {
                    analyticsState.copy(
                        isLoading = false,
                        error = "No se pudieron cargar las analíticas: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                analyticsState = analyticsState.copy(
                    isLoading = false,
                    error = "Error de red: ${e.message}"
                )
            }
        }
    }
}
package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.repository.AnalyticsRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.AnalyticsDashboard
import java.time.OffsetDateTime

data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val dashboard: AnalyticsDashboard? = null,
    val error: String? = null
)

class AnalyticsViewModel : ViewModel() {

    private val repository = AnalyticsRepository(RetrofitClient.analyticsApi)

    var analyticsState by mutableStateOf(AnalyticsUiState())
        private set

    fun loadDashboard(userId: Long) {
        viewModelScope.launch {
            analyticsState = analyticsState.copy(isLoading = true, error = null)

            val end = OffsetDateTime.now()
            val start = end.minusDays(30)

            try {
                val response = repository.getDashboard(userId, start, end)

                analyticsState = if (response.isSuccessful) {
                    analyticsState.copy(
                        isLoading = false,
                        dashboard = response.body(),
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
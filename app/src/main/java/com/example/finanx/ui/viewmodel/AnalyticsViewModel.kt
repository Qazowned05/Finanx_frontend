package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.repository.AnalyticsRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.TransactionsDashboard
import java.time.LocalDate

data class AnalyticsUiState(
    val isLoading: Boolean        = false,
    val dashboard: TransactionsDashboard? = null,
    val error: String?            = null,

    val startDate: LocalDate = LocalDate.now().minusDays(30),
    val endDate:   LocalDate = LocalDate.now(),

    val showChatDialog: Boolean = false,
    val pendingPrompt:  String  = "",

    val isAiLoading: Boolean = false,
)

class AnalyticsViewModel : ViewModel() {

    private val repository = AnalyticsRepository(RetrofitClient.analyticsApi)

    var state by mutableStateOf(AnalyticsUiState())
        private set

    fun setStartDate(date: LocalDate) { state = state.copy(startDate = date) }
    fun setEndDate(date: LocalDate)   { state = state.copy(endDate   = date) }

    fun openChatDialog()  { state = state.copy(showChatDialog = true,  pendingPrompt = "") }
    fun closeChatDialog() { state = state.copy(showChatDialog = false, pendingPrompt = "") }
    fun onPromptChange(text: String) { state = state.copy(pendingPrompt = text) }

    fun loadDashboard(userId: Long, prompt: String? = null) {
        val withAi = prompt != null

        viewModelScope.launch {
            state = if (withAi) {
                state.copy(
                    isAiLoading    = true,
                    showChatDialog = false,
                    error          = null
                )
            } else {
                state.copy(
                    isLoading = true,
                    error     = null
                )
            }

            val periodRequest = PeriodRequest(
                start  = state.startDate,
                end    = state.endDate,
                userId = userId
            )

            try {
                val response = repository.getBalanceDashboard(
                    period = periodRequest,
                    prompt  = prompt
                )

                val dashboard = response.body() as TransactionsDashboard

                state = if (response.isSuccessful) {
                    state.copy(
                        isLoading   = false,
                        isAiLoading = false,
                        dashboard   = dashboard,
                        error       = null
                    )
                } else {
                    state.copy(
                        isLoading   = false,
                        isAiLoading = false,
                        error       = "Error ${response.code()}: no se pudo cargar el panel."
                    )
                }

            } catch (e: Exception) {
                state = state.copy(
                    isLoading   = false,
                    isAiLoading = false,
                    error       = "Error de red: ${e.message}"
                )
            }
        }
    }

    fun submitPrompt(userId: Long) {
        val prompt = state.pendingPrompt.trim().ifEmpty { null }
        loadDashboard(userId, prompt)
    }

    fun refresh(userId: Long) = loadDashboard(userId, null)
}
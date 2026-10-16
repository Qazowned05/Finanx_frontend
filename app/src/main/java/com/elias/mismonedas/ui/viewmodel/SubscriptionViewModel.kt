package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.PageResponsePaymentResponse
import org.openapitools.client.models.PaymentResponse
import org.openapitools.client.models.PaymentState
import org.openapitools.client.models.SubscriptionResponse
import java.time.LocalDate
import javax.inject.Inject

sealed class SubscriptionUIState {
    object Loading : SubscriptionUIState()
    data class Loaded(
        val plan: SubscriptionResponse?,
        val payments: List<PaymentResponse> = emptyList(),
        val isLoadingPayments: Boolean = false,
        override val isRefreshing: Boolean = false,
        val isLoadingMore: Boolean = false,
        val page: Int = 0,
        val hasMore: Boolean = false,
        val filterState: PaymentState? = null,
        val filterFrom: LocalDate? = null,
        val filterTo: LocalDate? = null,
        val paymentsError: String? = null
    ) : SubscriptionUIState(), RefreshableUiState
    data class Error(val message: String) : SubscriptionUIState()
}

sealed class SubscriptionActionState {
    object Idle : SubscriptionActionState()
    object Loading : SubscriptionActionState()
    object Success : SubscriptionActionState()
    data class Error(val message: String) : SubscriptionActionState()
}

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val repository: SubscriptionRepository
) : ViewModel() {

    var state by mutableStateOf<SubscriptionUIState>(SubscriptionUIState.Loading)
        private set

    var actionState by mutableStateOf<SubscriptionActionState>(SubscriptionActionState.Idle)
        private set

    fun load() {
        viewModelScope.launch {
            state = SubscriptionUIState.Loading

            val plan = try {
                repository.getCurrentSubscription()
            } catch (e: Exception) {
                state = SubscriptionUIState.Error("Error: ${e.message}")
                return@launch
            }

            state = SubscriptionUIState.Loaded(plan = plan, isLoadingPayments = true)
            loadPayments(reset = true)
        }
    }

    fun refresh() {
        val current = state as? SubscriptionUIState.Loaded
        if (current == null) {
            load()
            return
        }

        viewModelScope.launch {
            val refreshedPlan = runCatching { repository.getCurrentSubscription() }.getOrNull()
            val latest = state as? SubscriptionUIState.Loaded
            if (latest != null && refreshedPlan != null) {
                state = latest.copy(plan = refreshedPlan)
            }
            loadPayments(reset = true, refreshing = true)
        }
    }

    fun loadMore() {
        val current = state as? SubscriptionUIState.Loaded ?: return
        if (current.isLoadingPayments || current.isLoadingMore || current.isRefreshing || !current.hasMore) {
            return
        }
        loadPayments(reset = false)
    }

    fun setStateFilter(paymentState: PaymentState?) {
        applyFilters { it.copy(filterState = paymentState) }
    }

    fun setDateFilter(from: LocalDate?, to: LocalDate?) {
        applyFilters { it.copy(filterFrom = from, filterTo = to) }
    }

    fun clearFilters() {
        applyFilters { it.copy(filterState = null, filterFrom = null, filterTo = null) }
    }

    private fun applyFilters(
        transform: (SubscriptionUIState.Loaded) -> SubscriptionUIState.Loaded
    ) {
        val current = state as? SubscriptionUIState.Loaded ?: return
        if (current.isLoadingPayments || current.isRefreshing) return

        state = transform(current).copy(
            page = 0,
            hasMore = false,
            paymentsError = null
        )
        loadPayments(reset = true)
    }

    private fun loadPayments(reset: Boolean, refreshing: Boolean = false) {
        val base = state as? SubscriptionUIState.Loaded ?: return
        if (!reset && (base.isLoadingMore || !base.hasMore)) return

        viewModelScope.launch {
            val current = state as? SubscriptionUIState.Loaded ?: return@launch

            state = if (reset) {
                current.copy(
                    isLoadingPayments = !refreshing,
                    isRefreshing = refreshing,
                    isLoadingMore = false,
                    paymentsError = null
                )
            } else {
                current.copy(
                    isLoadingMore = true,
                    paymentsError = null
                )
            }

            val targetPage = if (reset) 0 else current.page + 1

            try {
                val response = repository.listPaymentPage(
                    state = current.filterState,
                    from = current.filterFrom,
                    to = current.filterTo,
                    page = targetPage
                )
                val latest = state as? SubscriptionUIState.Loaded ?: return@launch
                val newItems = response.items ?: emptyList()

                state = latest.copy(
                    payments = if (reset) newItems else latest.payments + newItems,
                    page = response.pagination?.page ?: targetPage,
                    hasMore = response.hasMorePages(),
                    isLoadingPayments = false,
                    isRefreshing = false,
                    isLoadingMore = false,
                    paymentsError = null
                )
            } catch (e: Exception) {
                val latest = state as? SubscriptionUIState.Loaded ?: return@launch
                state = latest.copy(
                    isLoadingPayments = false,
                    isRefreshing = false,
                    isLoadingMore = false,
                    paymentsError = e.message ?: "No se pudo cargar el historial de pagos"
                )
            }
        }
    }

    fun cancelSubscription(subscriptionId: Long) {
        viewModelScope.launch {
            actionState = SubscriptionActionState.Loading
            try {
                repository.cancelSubscription(subscriptionId)
                actionState = SubscriptionActionState.Success
                load()
            } catch (e: Exception) {
                actionState = SubscriptionActionState.Error("Error al cancelar: ${e.message}")
            }
        }
    }

    fun resetActionState() {
        actionState = SubscriptionActionState.Idle
    }

    private fun PageResponsePaymentResponse.hasMorePages(): Boolean {
        val page = pagination?.page ?: 0
        val totalPages = pagination?.totalPages ?: 1
        return page + 1 < totalPages
    }
}

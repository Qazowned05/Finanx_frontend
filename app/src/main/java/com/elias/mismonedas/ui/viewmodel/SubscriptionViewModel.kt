package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.PaymentResponse
import org.openapitools.client.models.SubscriptionResponse
import javax.inject.Inject

sealed class SubscriptionState {
    object Loading : SubscriptionState()
    data class Loaded(
        val plan: SubscriptionResponse?,
        val payments: List<PaymentResponse>
    ) : SubscriptionState()
    data class Error(val message: String) : SubscriptionState()
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

    var state by mutableStateOf<SubscriptionState>(SubscriptionState.Loading)
        private set

    var actionState by mutableStateOf<SubscriptionActionState>(SubscriptionActionState.Idle)
        private set

    fun load(userId: Long) {
        viewModelScope.launch {
            state = SubscriptionState.Loading
            try {
                val plan = repository.getCurrentSubscription(userId)
                val payments = repository.getPaymentHistory(userId)
                state = SubscriptionState.Loaded(plan = plan, payments = payments)
            } catch (e: Exception) {
                state = SubscriptionState.Error("Error: ${e.message}")
            }
        }
    }

    fun cancelSubscription(subscriptionId: Long, userId: Long) {
        viewModelScope.launch {
            actionState = SubscriptionActionState.Loading
            try {
                repository.cancelSubscription(subscriptionId)
                actionState = SubscriptionActionState.Success
                load(userId)
            } catch (e: Exception) {
                actionState = SubscriptionActionState.Error("Error al cancelar: ${e.message}")
            }
        }
    }

    fun resetActionState() {
        actionState = SubscriptionActionState.Idle
    }
}

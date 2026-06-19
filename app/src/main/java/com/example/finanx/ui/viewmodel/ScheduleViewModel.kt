package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.repository.ScheduleRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import org.openapitools.client.models.UpdateScheduleRequest
import java.math.BigDecimal

sealed class RecurringState {
    object Loading : RecurringState()
    data class Success(
        val transactionSchedules: List<TransactionScheduleResponse>,
        val budgetSchedules: List<BudgetScheduleResponse>
    ) : RecurringState()
    data class Error(val message: String) : RecurringState()
}

sealed class RecurringActionState {
    object Idle : RecurringActionState()
    object Loading : RecurringActionState()
    object Success : RecurringActionState()
    data class Error(val message: String) : RecurringActionState()
}

class ScheduleViewModel : ViewModel() {
    private val repository = ScheduleRepository()

    var recurringState by mutableStateOf<RecurringState>(RecurringState.Loading)
        private set

    var actionState by mutableStateOf<RecurringActionState>(RecurringActionState.Idle)
        private set

    fun fetchAll(userId: Long, state: String = "RUNNING") {
        viewModelScope.launch {
            recurringState = RecurringState.Loading
            try {
                val txResponse = repository.getTransactionSchedulesByUserAndState(userId, state)
                val budgetResponse = repository.getBudgetSchedulesByUserAndState(userId, state)

                if (txResponse.isSuccessful && budgetResponse.isSuccessful) {
                    recurringState = RecurringState.Success(
                        transactionSchedules = txResponse.body() ?: emptyList(),
                        budgetSchedules = budgetResponse.body() ?: emptyList()
                    )
                } else {
                    recurringState = RecurringState.Error("Error al cargar recurrentes")
                }
            } catch (e: Exception) {
                recurringState = RecurringState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createTransactionSchedule(
        userId: Long,
        categoryId: Long,
        amount: BigDecimal,
        paymentMethod: UpdateScheduleRequest.PaymentMethod,
        transactionType: UpdateScheduleRequest.TransactionType,
        recurrenceType: RecurrenceRuleRequest.RecurrenceType,
        interval: Int,
        start: java.time.LocalDate,
        end: java.time.LocalDate,
        dayOfWeek: RecurrenceRuleRequest.DayOfWeek? = null
    ) {
        viewModelScope.launch {
            actionState = RecurringActionState.Loading
            try {
                val rule = RecurrenceRuleRequest(
                    recurrenceType = recurrenceType,
                    interval = interval,
                    start = start,
                    end = end,
                    dayOfWeek = dayOfWeek
                )
                val response = repository.createTransactionSchedule(
                    userId = userId,
                    categoryId = categoryId,
                    amount = amount,
                    paymentMethod = paymentMethod,
                    transactionType = transactionType,
                    recurrenceRule = rule
                )
                if (response.isSuccessful) {
                    actionState = RecurringActionState.Success
                    fetchAll(userId)
                } else {
                    actionState = RecurringActionState.Error("No se pudo crear: ${response.message()}")
                }
            } catch (e: Exception) {
                actionState = RecurringActionState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createBudgetSchedule(
        userId: Long,
        categoryId: Long,
        alertable: Boolean,
        percentAlert: Int,
        limitAmount: BigDecimal,
        description: String,
        recurrenceType: RecurrenceRuleRequest.RecurrenceType,
        interval: Int,
        start: java.time.LocalDate,
        end: java.time.LocalDate,
        dayOfWeek: RecurrenceRuleRequest.DayOfWeek? = null
    ) {
        viewModelScope.launch {
            actionState = RecurringActionState.Loading
            try {
                val rule = RecurrenceRuleRequest(
                    recurrenceType = recurrenceType,
                    interval = interval,
                    start = start,
                    end = end,
                    dayOfWeek = dayOfWeek
                )
                val response = repository.createBudgetSchedule(
                    userId = userId,
                    categoryId = categoryId,
                    alertable = alertable,
                    percentAlert = percentAlert,
                    limitAmount = limitAmount,
                    description = description,
                    recurrenceRule = rule
                )
                if (response.isSuccessful) {
                    actionState = RecurringActionState.Success
                    fetchAll(userId)
                } else {
                    actionState = RecurringActionState.Error("No se pudo crear: ${response.message()}")
                }
            } catch (e: Exception) {
                actionState = RecurringActionState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun pause(id: Long, userId: Long) {
        viewModelScope.launch {
            try {
                if (repository.pauseSchedule(id).isSuccessful) fetchAll(userId)
            } catch (e: Exception) { }
        }
    }

    fun resume(id: Long, userId: Long) {
        viewModelScope.launch {
            try {
                if (repository.resumeSchedule(id).isSuccessful) fetchAll(userId)
            } catch (e: Exception) { }
        }
    }

    fun cancel(id: Long, userId: Long) {
        viewModelScope.launch {
            try {
                if (repository.cancelSchedule(id).isSuccessful) fetchAll(userId)
            } catch (e: Exception) { }
        }
    }

    fun resetActionState() {
        actionState = RecurringActionState.Idle
    }
}
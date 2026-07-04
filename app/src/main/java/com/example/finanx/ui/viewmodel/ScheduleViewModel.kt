package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.model.SelectOption
import com.example.finanx.data.repository.CategoryRepository
import com.example.finanx.data.repository.ReasonRepository
import com.example.finanx.data.repository.ScheduleRepository
import com.example.finanx.data.api.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetScheduleRequest
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.ScheduleRequest
import org.openapitools.client.models.TransactionScheduleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import retrofit2.Response
import java.math.BigDecimal
import java.time.LocalDate

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

data class ScheduleFormOptions(
    val categories: List<org.openapitools.client.models.CategoryResponse> = emptyList(),
    val reasons: List<SelectOption> = emptyList()
)

sealed class ScheduleFormState {
    object Idle : ScheduleFormState()
    object Loading : ScheduleFormState()
    data class Ready(val options: ScheduleFormOptions) : ScheduleFormState()
    data class Error(val message: String) : ScheduleFormState()
}

class ScheduleViewModel(
    private val repository: ScheduleRepository = ScheduleRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository(RetrofitClient.categoryApi),
    private val reasonRepository: ReasonRepository = ReasonRepository()
) : ViewModel() {

    var recurringState by mutableStateOf<RecurringState>(RecurringState.Loading)
        private set

    var actionState by mutableStateOf<RecurringActionState>(RecurringActionState.Idle)
        private set

    var formState by mutableStateOf<ScheduleFormState>(ScheduleFormState.Idle)
        private set

    fun fetchAll(userId: Long, state: String = "RUNNING") {
        viewModelScope.launch {
            recurringState = RecurringState.Loading
            recurringState = try {
                val txResponse = repository.getTransactionSchedulesByUserAndState(userId, state)
                val budgetResponse = repository.getBudgetSchedulesByUserAndState(userId, state)
                if (txResponse.isSuccessful && budgetResponse.isSuccessful) {
                    RecurringState.Success(
                        transactionSchedules = txResponse.body() ?: emptyList(),
                        budgetSchedules = budgetResponse.body() ?: emptyList()
                    )
                } else {
                    RecurringState.Error("Error al cargar recurrentes")
                }
            } catch (e: Exception) {
                RecurringState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun loadFormOptions(userId: Long) {
        viewModelScope.launch {
            formState = ScheduleFormState.Loading
            try {
                val categoriesDeferred = async { categoryRepository.getActiveCategories(userId) }
                val reasonsDeferred = async { reasonRepository.getReasonsByUser(userId) }
                val catResponse = categoriesDeferred.await()
                val reasonResponse = reasonsDeferred.await()
                if (catResponse.isSuccessful && reasonResponse.isSuccessful) {
                    formState = ScheduleFormState.Ready(
                        ScheduleFormOptions(
                            categories = catResponse.body() ?: emptyList(),
                            reasons = (reasonResponse.body() ?: emptyList())
                                .filter { it.active == true }
                                .mapNotNull { r -> r.id?.let { SelectOption(it, r.description ?: "") } }
                        )
                    )
                } else {
                    formState = ScheduleFormState.Error("Error al cargar opciones")
                }
            } catch (e: Exception) {
                formState = ScheduleFormState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createTransactionSchedule(
        userId: Long,
        categoryId: Long,
        reasonId: Long?,
        description: String?,
        amount: BigDecimal,
        paymentMethod: TransactionScheduleRequest.PaymentMethod,
        transactionType: TransactionScheduleRequest.TransactionType,
        recurrenceType: RecurrenceRuleRequest.RecurrenceType,
        interval: Int,
        start: LocalDate,
        end: LocalDate,
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
                val request = TransactionScheduleRequest(
                    userId = userId,
                    categoryId = categoryId,
                    type = "TRANSACTION",
                    description = description,
                    amount = amount,
                    paymentMethod = paymentMethod,
                    transactionType = transactionType,
                    recurrenceRule = rule,
                    reasonId = reasonId
                )
                println("TRANSACTION SCHEDULE REQUEST: ${request}")

                val response = repository.createSchedule(request)
                if (response.isSuccessful) {
                    actionState = RecurringActionState.Success
                    fetchAll(userId)
                } else {
                    actionState = RecurringActionState.Error("No se pudo crear: ${response.message()}")
                }
            } catch (e: Exception) {
                actionState = RecurringActionState.Error("Error de red: ${e.message}")
                println("EXCEPCION CREANDO SCHEDULE: $e")
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
        start: LocalDate,
        end: LocalDate,
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
                val response = repository.createSchedule(
                    BudgetScheduleRequest(
                        userId = userId,
                        categoryId = categoryId,
                        alertable = alertable,
                        percentAlert = percentAlert,
                        limitAmount = limitAmount,
                        description = description,
                        recurrenceRule = rule,
                        type = "BUDGET"
                    )
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
            try { if (repository.pauseSchedule(id).isSuccessful) fetchAll(userId) } catch (_: Exception) {}
        }
    }

    fun resume(id: Long, userId: Long) {
        viewModelScope.launch {
            try { if (repository.resumeSchedule(id).isSuccessful) fetchAll(userId) } catch (_: Exception) {}
        }
    }

    fun cancel(id: Long, userId: Long) {
        viewModelScope.launch {
            try { if (repository.cancelSchedule(id).isSuccessful) fetchAll(userId) } catch (_: Exception) {}
        }
    }

    fun resetActionState() {
        actionState = RecurringActionState.Idle
    }
}
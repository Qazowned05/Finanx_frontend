package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.model.SelectOption
import com.elias.mismonedas.data.repository.CategoryRepository
import com.elias.mismonedas.data.repository.ReasonRepository
import com.elias.mismonedas.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetScheduleRequest
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.DayOfWeek
import org.openapitools.client.models.PageResponseBudgetScheduleResponse
import org.openapitools.client.models.PageResponseTransactionScheduleResponse
import org.openapitools.client.models.PaymentMethod
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.RecurrenceType
import org.openapitools.client.models.ScheduleState
import org.openapitools.client.models.TransactionScheduleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import org.openapitools.client.models.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

sealed class RecurringState : com.elias.mismonedas.ui.viewmodel.RefreshableUiState {
    object Loading : RecurringState() { override val isRefreshing: Boolean = false }
    data class Success(
        val transactionSchedules: List<TransactionScheduleResponse>,
        val budgetSchedules: List<BudgetScheduleResponse>,
        override val isRefreshing: Boolean = false,
        val transactionPage: Int = 0,
        val budgetPage: Int = 0,
        val hasMore: Boolean = true,
        val isLoadingMore: Boolean = false
    ) : RecurringState()
    data class Error(val message: String) : RecurringState() { override val isRefreshing: Boolean = false }
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

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val categoryRepository: CategoryRepository,
    private val reasonRepository: ReasonRepository
) : ViewModel() {

    var recurringState by mutableStateOf<RecurringState>(RecurringState.Loading)
        private set

    var actionState by mutableStateOf<RecurringActionState>(RecurringActionState.Idle)
        private set

    var formState by mutableStateOf<ScheduleFormState>(ScheduleFormState.Idle)
        private set

    var stateFilter by mutableStateOf<ScheduleState?>(ScheduleState.RUNNING)
        private set

    private var currentUserId: Long? = null

    fun fetchAll(userId: Long, state: String = "RUNNING", force: Boolean = false) {
        val parsed = ScheduleState.decode(state)
        val filterChanged = parsed != stateFilter
        stateFilter = parsed
        load(userId, force = force || filterChanged)
    }

    fun load(userId: Long, force: Boolean = false) {
        currentUserId = userId
        if (!force && recurringState is RecurringState.Success) return
        viewModelScope.launch {
            val current = recurringState
            val hasData = current is RecurringState.Success &&
                (current.transactionSchedules.isNotEmpty() || current.budgetSchedules.isNotEmpty())
            recurringState = if (hasData && current is RecurringState.Success) {
                current.copy(isRefreshing = true)
            } else {
                RecurringState.Loading
            }
            recurringState = try {
                val scheduleState = stateFilter
                val txResponse = repository.listT(scheduleState, page = 0)
                val budgetResponse = repository.listB(scheduleState, page = 0)
                if (txResponse.isSuccessful && budgetResponse.isSuccessful) {
                    val txBody = txResponse.body()
                    val budgetBody = budgetResponse.body()
                    RecurringState.Success(
                        transactionSchedules = txBody?.items ?: emptyList(),
                        budgetSchedules = budgetBody?.items ?: emptyList(),
                        isRefreshing = false,
                        transactionPage = txBody?.pagination?.page ?: 0,
                        budgetPage = budgetBody?.pagination?.page ?: 0,
                        hasMore = (txBody?.hasMorePages() ?: false) || (budgetBody?.hasMorePages() ?: false),
                        isLoadingMore = false
                    )
                } else {
                    RecurringState.Error("Error al cargar recurrentes")
                }
            } catch (e: Exception) {
                RecurringState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun loadMore() {
        val current = recurringState as? RecurringState.Success ?: return
        if (!current.hasMore || current.isLoadingMore || current.isRefreshing) return
        viewModelScope.launch {
            recurringState = current.copy(isLoadingMore = true)
            try {
                val scheduleState = stateFilter
                val nextTransactionPage = current.transactionPage + 1
                val nextBudgetPage = current.budgetPage + 1
                val txResponse = repository.listT(scheduleState, page = nextTransactionPage)
                val budgetResponse = repository.listB(scheduleState, page = nextBudgetPage)
                if (txResponse.isSuccessful && budgetResponse.isSuccessful) {
                    val txBody = txResponse.body()
                    val budgetBody = budgetResponse.body()
                    recurringState = current.copy(
                        transactionSchedules = current.transactionSchedules + (txBody?.items ?: emptyList()),
                        budgetSchedules = current.budgetSchedules + (budgetBody?.items ?: emptyList()),
                        transactionPage = txBody?.pagination?.page ?: nextTransactionPage,
                        budgetPage = budgetBody?.pagination?.page ?: nextBudgetPage,
                        hasMore = (txBody?.hasMorePages() ?: false) || (budgetBody?.hasMorePages() ?: false),
                        isLoadingMore = false
                    )
                } else {
                    recurringState = current.copy(isLoadingMore = false)
                }
            } catch (e: Exception) {
                recurringState = current.copy(isLoadingMore = false)
            }
        }
    }

    fun onStateFilterChange(filter: ScheduleState?) {
        if (stateFilter == filter) return
        stateFilter = filter
        currentUserId?.let { load(it, force = true) }
    }

    fun loadFormOptions(userId: Long, force: Boolean = false) {
        if (!force && formState is ScheduleFormState.Ready) return
        viewModelScope.launch {
            formState = ScheduleFormState.Loading
            try {
                val categoriesDeferred = async { categoryRepository.list(active = true, type = null) }
                val reasonsDeferred = async { reasonRepository.list(active = true) }
                val catResponse = categoriesDeferred.await()
                val reasonResponse = reasonsDeferred.await()
                if (catResponse.isSuccessful && reasonResponse.isSuccessful) {
                    formState = ScheduleFormState.Ready(
                        ScheduleFormOptions(
                            categories = catResponse.body()?.items ?: emptyList(),
                            reasons = (reasonResponse.body()?.items ?: emptyList())
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
        paymentMethod: PaymentMethod,
        transactionType: TransactionType,
        recurrenceType: RecurrenceType,
        interval: Int,
        start: LocalDate,
        end: LocalDate,
        dayOfWeek: DayOfWeek? = null
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
                    load(userId, force = true)
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
        recurrenceType: RecurrenceType,
        interval: Int,
        start: LocalDate,
        end: LocalDate,
        dayOfWeek: DayOfWeek? = null
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
                    load(userId, force = true)
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
            try { if (repository.pauseSchedule(id).isSuccessful) load(userId, force = true) } catch (_: Exception) {}
        }
    }

    fun resume(id: Long, userId: Long) {
        viewModelScope.launch {
            try { if (repository.resumeSchedule(id).isSuccessful) load(userId, force = true) } catch (_: Exception) {}
        }
    }

    fun cancel(id: Long, userId: Long) {
        viewModelScope.launch {
            try { if (repository.cancelSchedule(id).isSuccessful) load(userId, force = true) } catch (_: Exception) {}
        }
    }

    fun resetActionState() {
        actionState = RecurringActionState.Idle
    }

    private fun PageResponseTransactionScheduleResponse.hasMorePages(): Boolean {
        val current = pagination?.page ?: 0
        val total = pagination?.totalPages ?: 1
        return current + 1 < total
    }

    private fun PageResponseBudgetScheduleResponse.hasMorePages(): Boolean {
        val current = pagination?.page ?: 0
        val total = pagination?.totalPages ?: 1
        return current + 1 < total
    }
}
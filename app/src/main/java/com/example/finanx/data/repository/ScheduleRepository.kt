package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ScheduleApi
import org.openapitools.client.apis.ScheduleBudgetsApi
import org.openapitools.client.apis.ScheduleTransactionsApi
import org.openapitools.client.models.BudgetScheduleRequest
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.ScheduleRequest
import org.openapitools.client.models.ScheduleResponse
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionScheduleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import retrofit2.Response
import java.math.BigDecimal

class ScheduleRepository(
    private val api: ScheduleApi = RetrofitClient.scheduleApi,
    private val budgetScheduleApi: ScheduleBudgetsApi = RetrofitClient.budgetScheduleApi,
    private val transactionScheduleApi: ScheduleTransactionsApi = RetrofitClient.transactionScheduleApi
) {
    suspend fun getSchedulesByUserAndState(
        userId: Long,
        state: String
    ): Response<List<ScheduleResponse>> =
        withContext(Dispatchers.IO) { api.findAllScheduleByUserAndState(userId, state) }

    suspend fun getBudgetSchedulesByUserAndState(
        userId: Long,
        state: String
    ): Response<List<BudgetScheduleResponse>> =
        withContext(Dispatchers.IO) { budgetScheduleApi.findAllBudgetScheduleByUserAndState(userId, state) }

    suspend fun getTransactionSchedulesByUserAndState(
        userId: Long,
        state: String
    ): Response<List<TransactionScheduleResponse>> =
        withContext(Dispatchers.IO) { transactionScheduleApi.findAllTScheduleByUserAndState(userId, state) }

    suspend fun pauseSchedule(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.pauseSchedule(id) }

    suspend fun resumeSchedule(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.resumeSchedule(id) }

    suspend fun cancelSchedule(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.cancelSchedule(id) }

    suspend fun disableSchedule(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disableSchedule(id) }

    suspend fun updateSchedule(id: Long, request: ScheduleRequest): Response<ScheduleResponse> =
        withContext(Dispatchers.IO) { api.updateSchedule(id, request) }

    suspend fun createTransactionSchedule(
        userId: Long,
        categoryId: Long,
        amount: BigDecimal,
        paymentMethod: TransactionScheduleRequest.PaymentMethod,
        transactionType: TransactionScheduleRequest.TransactionType,
        recurrenceRule: RecurrenceRuleRequest,
        reasonId: Long? = null
    ): Response<ScheduleResponse> = withContext(Dispatchers.IO) {
        val request: ScheduleRequest = TransactionScheduleRequest(
            userId = userId,
            categoryId = categoryId,
            type = "TRANSACTION",
            description = "",
            amount = amount,
            paymentMethod = paymentMethod,
            transactionType = transactionType,
            recurrenceRule = recurrenceRule,
            reasonId = reasonId
        )
        api.createSchedule(request)
    }

    suspend fun createBudgetSchedule(
        userId: Long,
        categoryId: Long,
        alertable: Boolean,
        percentAlert: Int,
        limitAmount: BigDecimal,
        description: String,
        recurrenceRule: RecurrenceRuleRequest
    ): Response<ScheduleResponse> = withContext(Dispatchers.IO) {
        val request: ScheduleRequest = BudgetScheduleRequest(
            userId = userId,
            categoryId = categoryId,
            type = "BUDGET",
            alertable = alertable,
            percentAlert = percentAlert,
            limitAmount = limitAmount,
            description = description,
            recurrenceRule = recurrenceRule
        )
        api.createSchedule(request)
    }
}

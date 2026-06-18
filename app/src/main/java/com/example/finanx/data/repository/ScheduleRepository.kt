package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.BudgetScheduleControllerApi
import org.openapitools.client.apis.ScheduleControllerApi
import org.openapitools.client.apis.TransactionScheduleControllerApi
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import org.openapitools.client.models.UpdateSchedule200Response
import org.openapitools.client.models.UpdateScheduleRequest
import retrofit2.Response
import java.math.BigDecimal

class ScheduleRepository(
    private val api: ScheduleControllerApi = RetrofitClient.scheduleApi,
    private val budgetScheduleApi: BudgetScheduleControllerApi = RetrofitClient.budgetScheduleApi,
    private val transactionScheduleApi: TransactionScheduleControllerApi = RetrofitClient.transactionScheduleApi
) {
    suspend fun getSchedulesByUserAndState(
        userId: Long,
        state: String
    ): Response<List<UpdateSchedule200Response>> {
        return api.findSchedulesByUserAndState(userId, state)
    }

    suspend fun getBudgetSchedulesByUserAndState(
        userId: Long,
        state: String
    ): Response<List<BudgetScheduleResponse>> {
        return budgetScheduleApi.findBudgetSchedulesByUserAndState(userId, state)
    }

    suspend fun getTransactionSchedulesByUserAndState(
        userId: Long,
        state: String
    ): Response<List<TransactionScheduleResponse>> {
        return transactionScheduleApi.findTransactionSchedulesByUserAndState(userId, state)
    }

    suspend fun pauseSchedule(id: Long): Response<Unit> = api.pauseSchedule(id)
    suspend fun resumeSchedule(id: Long): Response<Unit> = api.resumeSchedule(id)
    suspend fun cancelSchedule(id: Long): Response<Unit> = api.cancelSchedule(id)
    suspend fun disableSchedule(id: Long): Response<Unit> = api.disableSchedule(id)

    suspend fun updateSchedule(id: Long, request: UpdateScheduleRequest): Response<UpdateSchedule200Response> {
        return api.updateSchedule(id, request)
    }

    suspend fun createTransactionSchedule(
        userId: Long,
        categoryId: Long,
        amount: BigDecimal,
        paymentMethod: UpdateScheduleRequest.PaymentMethod,
        transactionType: UpdateScheduleRequest.TransactionType,
        recurrenceRule: RecurrenceRuleRequest,
        reasonId: Long? = null
    ): Response<UpdateSchedule200Response> {
        val request = UpdateScheduleRequest(
            userId = userId,
            categoryId = categoryId,
            type = "TRANSACTION",
            alertable = false,
            percentAlert = 0,
            limitAmount = BigDecimal.ZERO,
            description = "",
            amount = amount,
            paymentMethod = paymentMethod,
            transactionType = transactionType,
            recurrenceRule = recurrenceRule,
            reasonId = reasonId
        )
        return api.createSchedule(request)
    }

    suspend fun createBudgetSchedule(
        userId: Long,
        categoryId: Long,
        alertable: Boolean,
        percentAlert: Int,
        limitAmount: BigDecimal,
        description: String,
        recurrenceRule: RecurrenceRuleRequest
    ): Response<UpdateSchedule200Response> {
        val request = UpdateScheduleRequest(
            userId = userId,
            categoryId = categoryId,
            type = "BUDGET",
            alertable = alertable,
            percentAlert = percentAlert,
            limitAmount = limitAmount,
            description = description,
            amount = BigDecimal.ZERO,
            paymentMethod = UpdateScheduleRequest.PaymentMethod.OTHER,
            transactionType = UpdateScheduleRequest.TransactionType.SPENT,
            recurrenceRule = recurrenceRule
        )
        return api.createSchedule(request)
    }
}
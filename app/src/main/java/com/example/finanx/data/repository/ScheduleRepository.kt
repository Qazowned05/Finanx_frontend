package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.BudgetScheduleControllerApi
import org.openapitools.client.apis.ScheduleControllerApi
import org.openapitools.client.apis.TransactionScheduleControllerApi
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import org.openapitools.client.models.Update2200Response
import org.openapitools.client.models.Update2Request
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
    ): Response<List<Update2200Response>> {
        return api.findAllByUserAndState(userId, state)
    }

    suspend fun getBudgetSchedulesByUserAndState(
        userId: Long,
        state: String
    ): Response<List<BudgetScheduleResponse>> {
        return budgetScheduleApi.findAllByUserAndState2(userId, state)
    }

    suspend fun getTransactionSchedulesByUserAndState(
        userId: Long,
        state: String
    ): Response<List<TransactionScheduleResponse>> {
        return transactionScheduleApi.findAllByUserAndState1(userId, state)
    }

    suspend fun pauseSchedule(id: Long): Response<Unit> = api.pause(id)
    suspend fun resumeSchedule(id: Long): Response<Unit> = api.resume(id)
    suspend fun cancelSchedule(id: Long): Response<Unit> = api.cancel(id)
    suspend fun disableSchedule(id: Long): Response<Unit> = api.disable6(id)

    suspend fun updateSchedule(id: Long, request: Update2Request): Response<Update2200Response> {
        return api.update2(id, request)
    }

    suspend fun createTransactionSchedule(
        userId: Long,
        categoryId: Long,
        amount: BigDecimal,
        paymentMethod: Update2Request.PaymentMethod,
        transactionType: Update2Request.TransactionType,
        recurrenceRule: RecurrenceRuleRequest,
        reasonId: Long? = null
    ): Response<Update2200Response> {
        val request = Update2Request(
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
        return api.create1(request)
    }

    suspend fun createBudgetSchedule(
        userId: Long,
        categoryId: Long,
        alertable: Boolean,
        percentAlert: Int,
        limitAmount: BigDecimal,
        description: String,
        recurrenceRule: RecurrenceRuleRequest
    ): Response<Update2200Response> {
        val request = Update2Request(
            userId = userId,
            categoryId = categoryId,
            type = "BUDGET",
            alertable = alertable,
            percentAlert = percentAlert,
            limitAmount = limitAmount,
            description = description,
            amount = BigDecimal.ZERO,
            paymentMethod = Update2Request.PaymentMethod.OTHER,
            transactionType = Update2Request.TransactionType.SPENT,
            recurrenceRule = recurrenceRule
        )
        return api.create1(request)
    }
}
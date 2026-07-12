package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ScheduleApi
import org.openapitools.client.apis.ScheduleBudgetsApi
import org.openapitools.client.apis.ScheduleTransactionsApi
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.ScheduleRequest
import org.openapitools.client.models.ScheduleResponse
import org.openapitools.client.models.TransactionScheduleResponse
import retrofit2.Response
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val api: ScheduleApi,
    private val budgetScheduleApi: ScheduleBudgetsApi,
    private val transactionScheduleApi: ScheduleTransactionsApi
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

    suspend fun createSchedule(request: ScheduleRequest): Response<ScheduleResponse> = withContext(Dispatchers.IO) {
        api.createSchedule(request) }
}

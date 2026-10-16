package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ScheduleApi
import org.openapitools.client.apis.ScheduleBudgetsApi
import org.openapitools.client.apis.ScheduleTransactionsApi
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.PageResponseBudgetScheduleResponse
import org.openapitools.client.models.PageResponseTransactionScheduleResponse
import org.openapitools.client.models.ScheduleRequest
import org.openapitools.client.models.ScheduleResponse
import org.openapitools.client.models.ScheduleState
import org.openapitools.client.models.TransactionScheduleResponse
import retrofit2.Response
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val api: ScheduleApi,
    private val budgetScheduleApi: ScheduleBudgetsApi,
    private val transactionScheduleApi: ScheduleTransactionsApi
) {
    suspend fun listB(
        state: ScheduleState?,
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE
    ): Response<PageResponseBudgetScheduleResponse> =
        withContext(Dispatchers.IO) {
            budgetScheduleApi.listBudgetSchedules(state, page = page, size = size)
        }

    suspend fun listT(
        state: ScheduleState?,
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE
    ): Response<PageResponseTransactionScheduleResponse> =
        withContext(Dispatchers.IO) {
            transactionScheduleApi.listTransactionSchedules(state, page = page, size = size)
        }

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

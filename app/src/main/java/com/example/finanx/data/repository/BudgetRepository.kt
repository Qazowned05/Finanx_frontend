package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.BudgetControllerApi
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.TimeBoundListBudgetResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.AnalyticsControllerApi
import org.openapitools.client.models.BudgetExecutionResponse
import retrofit2.Response

class BudgetRepository(
    private val api: BudgetControllerApi = RetrofitClient.budgetApi,
    private val analyticsApi: AnalyticsControllerApi = RetrofitClient.analyticsApi
) {
    suspend fun getBudgets(userId: Long): Response<List<BudgetResponse>> =
        withContext(Dispatchers.IO) { api.findAllByUser4(userId) }

    suspend fun getDetailedBudgets(request: PeriodRequest): Response<List<BudgetExecutionResponse>> =
        withContext(Dispatchers.IO) { analyticsApi.getExecutionsByPeriod(request) }

    suspend fun getBudgetsByState(userId: Long, state: String): Response<List<BudgetResponse>> =
        withContext(Dispatchers.IO) { api.findAllByUserAndState4(userId, state) }

    suspend fun getBudgetById(id: Long): Response<BudgetResponse> =
        withContext(Dispatchers.IO) { api.findById5(id) }

    suspend fun createBudget(request: BudgetRequest): Response<BudgetResponse> =
        withContext(Dispatchers.IO) { api.create5(request) }

    suspend fun updateBudget(id: Long, request: BudgetRequest): Response<BudgetResponse> =
        withContext(Dispatchers.IO) { api.update6(id, request) }

    suspend fun cancelBudget(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.cancel2(id) }

    suspend fun disableBudget(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disable5(id) }

    suspend fun getBudgetsByHealth(health: String, period: PeriodRequest): Response<TimeBoundListBudgetResponse> =
        withContext(Dispatchers.IO) { api.getBudgetsByHealth(health, period) }
}

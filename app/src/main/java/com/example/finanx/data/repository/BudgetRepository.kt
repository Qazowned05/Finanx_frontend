package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.AnalyticsApi
import org.openapitools.client.apis.BudgetsApi
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.TimeBoundListBudgetResponse
import retrofit2.Response

class BudgetRepository(
    private val api: BudgetsApi = RetrofitClient.budgetApi,
    private val analyticsApi: AnalyticsApi = RetrofitClient.analyticsApi
) {
    suspend fun getBudgets(userId: Long): Response<List<BudgetResponse>> =
        withContext(Dispatchers.IO) { api.findAllBudgetsByUser(userId) }

    suspend fun getDetailedBudgets(request: PeriodRequest): Response<List<BudgetExecutionResponse>> =
        withContext(Dispatchers.IO) { analyticsApi.getBudgetsExecutionsByPeriod(request) }

    suspend fun getBudgetsByState(userId: Long, state: String): Response<List<BudgetResponse>> =
        withContext(Dispatchers.IO) { api.findAllBudgetsByUserAndState(userId, state) }

    suspend fun getBudgetById(id: Long): Response<BudgetResponse> =
        withContext(Dispatchers.IO) { api.findBudgetById(id) }

    suspend fun createBudget(request: BudgetRequest): Response<BudgetResponse> =
        withContext(Dispatchers.IO) { api.createBudget(request) }

    suspend fun updateBudget(id: Long, request: BudgetRequest): Response<BudgetResponse> =
        withContext(Dispatchers.IO) { api.updateBudget(id, request) }

    suspend fun cancelBudget(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.cancelBudget(id) }

    suspend fun disableBudget(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disableBudget(id) }

    suspend fun getBudgetsByHealth(health: String, period: PeriodRequest): Response<TimeBoundListBudgetResponse> =
        withContext(Dispatchers.IO) { api.findAllBudgetsByPeriodAndHealth(health, period) }
}

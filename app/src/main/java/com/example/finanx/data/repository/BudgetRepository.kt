package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.BudgetControllerApi
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.TimeBoundListBudgetResponse
import retrofit2.Response

class BudgetRepository(
    private val api: BudgetControllerApi = RetrofitClient.budgetApi
) {
    suspend fun getBudgets(userId: Long): Response<List<BudgetResponse>> {
        return api.findBudgetsByUser(userId)
    }

    suspend fun getBudgetsByState(userId: Long, state: String): Response<List<BudgetResponse>> {
        return api.findBudgetsByUserAndState(userId, state)
    }

    suspend fun getBudgetById(id: Long): Response<BudgetResponse> {
        return api.findBudgetById(id)
    }

    suspend fun createBudget(request: BudgetRequest): Response<BudgetResponse> {
        return api.createBudget(request)
    }

    suspend fun updateBudget(id: Long, request: BudgetRequest): Response<BudgetResponse> {
        return api.updateBudget(id, request)
    }

    suspend fun cancelBudget(id: Long): Response<Unit> {
        return api.cancelBudget(id)
    }

    suspend fun disableBudget(id: Long): Response<Unit> {
        return api.disableBudget(id)
    }

    suspend fun getBudgetsByHealth(health: String, period: PeriodRequest): Response<TimeBoundListBudgetResponse> {
        return api.getBudgetsByHealth(health, period)
    }
}

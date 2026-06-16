package com.example.finanx.data.repository

import org.openapitools.client.apis.BudgetControllerApi
import org.openapitools.client.models.BudgetResponse
import retrofit2.Response

class BudgetRepository(private val api: BudgetControllerApi) {
    suspend fun getBudgets(userId: Long): Response<List<BudgetResponse>> {
        return api.findAllByUser4(userId)
    }
}

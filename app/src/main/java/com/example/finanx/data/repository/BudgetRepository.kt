package com.example.finanx.data.repository

import com.example.finanx.data.api.FinanxApi
import com.example.finanx.data.model.BudgetResponse
import retrofit2.Response

class BudgetRepository(private val api: FinanxApi) {
    suspend fun getBudgets(userId: Long): Response<List<BudgetResponse>> {
        return api.getBudgets(userId)
    }
}

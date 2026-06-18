package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.SavingGoalControllerApi
import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse
import retrofit2.Response

class SavingGoalRepository(
    private val api: SavingGoalControllerApi = RetrofitClient.savingGoalApi
) {

    suspend fun getSavingGoals(userId: Long): Response<List<SavingGoalResponse>> {
        return api.findSavingGoalsByUser(userId)
    }

    suspend fun getSavingGoalById(id: Long): Response<SavingGoalResponse> {
        return api.findSavingGoalById(id)
    }

    suspend fun createSavingGoal(request: SavingGoalRequest): Response<SavingGoalResponse> {
        return api.createSavingGoal(request)
    }

    suspend fun updateSavingGoal(id: Long, request: SavingGoalRequest): Response<SavingGoalResponse> {
        return api.updateSavingGoal(id, request)
    }

    suspend fun disableSavingGoal(id: Long): Response<Unit> {
        return api.disableSavingGoal(id)
    }

    suspend fun cancelSavingGoal(id: Long): Response<Unit> {
        return api.cancelSavingGoal(id)
    }
}
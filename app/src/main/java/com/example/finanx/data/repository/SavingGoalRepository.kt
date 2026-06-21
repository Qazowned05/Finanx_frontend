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
        return api.findAllActiveByUser1(userId)
    }

    suspend fun getSavingGoalById(id: Long): Response<SavingGoalResponse> {
        return api.findById2(id)
    }

    suspend fun createSavingGoal(request: SavingGoalRequest): Response<SavingGoalResponse> {
        return api.create2(request)
    }

    suspend fun updateSavingGoal(id: Long, request: SavingGoalRequest): Response<SavingGoalResponse> {
        return api.update3(id, request)
    }

    suspend fun disableSavingGoal(id: Long): Response<Unit> {
        return api.disable3(id)
    }

    suspend fun cancelSavingGoal(id: Long): Response<Unit> {
        return api.cancel1(id)
    }
}
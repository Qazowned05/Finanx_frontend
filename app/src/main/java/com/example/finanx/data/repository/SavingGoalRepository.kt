package com.example.finanx.data.repository

import org.openapitools.client.apis.SavingGoalControllerApi
import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse
import retrofit2.Response

class SavingGoalRepository(private val api: SavingGoalControllerApi) {

    suspend fun getSavingGoals(userId: Long): Response<List<SavingGoalResponse>> {
        return api.findAllActiveByUser1(userId)
    }



    suspend fun createSavingGoal(request: SavingGoalRequest): Response<SavingGoalResponse> {
        return api.create2(request)
    }

    suspend fun updateSavingGoal(id: Long, request: SavingGoalRequest): Response<SavingGoalResponse> {
        return api.update3(id, request)
    }

    suspend fun deleteSavingGoal(id: Long): Response<Unit> {
        return Response.success(Unit)
    }
}
package com.example.finanx.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.SavingsApi
import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse
import retrofit2.Response
import javax.inject.Inject

class SavingGoalRepository @Inject constructor(
    private val api: SavingsApi
) {

    suspend fun getSavingGoals(userId: Long): Response<List<SavingGoalResponse>> =
        withContext(Dispatchers.IO) { api.findAllActiveSavingGoalByUser(userId) }

    suspend fun getSavingGoalById(id: Long): Response<SavingGoalResponse> =
        withContext(Dispatchers.IO) { api.findSavingGoalById(id) }

    suspend fun createSavingGoal(request: SavingGoalRequest): Response<SavingGoalResponse> =
        withContext(Dispatchers.IO) { api.createSavingGoal(request) }

    suspend fun updateSavingGoal(id: Long, request: SavingGoalRequest): Response<SavingGoalResponse> =
        withContext(Dispatchers.IO) { api.updateSavingGoal(id, request) }

    suspend fun disableSavingGoal(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disableSavingGoal(id) }

    suspend fun cancelSavingGoal(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.cancelSavingGoal(id) }
}

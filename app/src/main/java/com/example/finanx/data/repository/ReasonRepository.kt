package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.ReasonControllerApi
import org.openapitools.client.models.ReasonRequest
import org.openapitools.client.models.ReasonResponse
import retrofit2.Response

class ReasonRepository(
    private val api: ReasonControllerApi = RetrofitClient.reasonApi
) {
    suspend fun getReasonsByUser(userId: Long): Response<List<ReasonResponse>> {
        return api.findReasonsByUser(userId)
    }

    suspend fun getReasonById(id: Long): Response<ReasonResponse> {
        return api.findReasonById(id)
    }

    suspend fun createReason(request: ReasonRequest): Response<ReasonResponse> {
        return api.createReason(request)
    }

    suspend fun updateReason(id: Long, request: ReasonRequest): Response<ReasonResponse> {
        return api.updateReason(id, request)
    }

    suspend fun disableReason(id: Long): Response<Unit> {
        return api.disableReason(id)
    }
}
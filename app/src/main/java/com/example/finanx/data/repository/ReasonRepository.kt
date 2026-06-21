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
        return api.findAllByUser1(userId)
    }

    suspend fun getReasonById(id: Long): Response<ReasonResponse> {
        return api.findById3(id)
    }

    suspend fun createReason(request: ReasonRequest): Response<ReasonResponse> {
        return api.create3(request)
    }

    suspend fun updateReason(id: Long, request: ReasonRequest): Response<ReasonResponse> {
        return api.update4(id, request)
    }

    suspend fun disableReason(id: Long): Response<Unit> {
        return api.disable1(id)
    }
}
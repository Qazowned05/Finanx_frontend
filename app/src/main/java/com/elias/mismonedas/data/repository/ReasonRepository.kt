package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ReasonsApi
import org.openapitools.client.models.ReasonRequest
import org.openapitools.client.models.ReasonResponse
import retrofit2.Response
import javax.inject.Inject

class ReasonRepository @Inject constructor(
    private val api: ReasonsApi
) {
    suspend fun getReasonsByUser(userId: Long): Response<List<ReasonResponse>> =
        withContext(Dispatchers.IO) { api.findAllReasonByUser(userId) }

    suspend fun getReasonById(id: Long): Response<ReasonResponse> =
        withContext(Dispatchers.IO) { api.findReasonById(id) }

    suspend fun createReason(request: ReasonRequest): Response<ReasonResponse> =
        withContext(Dispatchers.IO) { api.createReason(request) }

    suspend fun updateReason(id: Long, request: ReasonRequest): Response<ReasonResponse> =
        withContext(Dispatchers.IO) { api.updateReason(id, request) }

    suspend fun disableReason(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disableReason(id) }
}

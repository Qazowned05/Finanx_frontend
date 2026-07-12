package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.FCMTokensApi
import org.openapitools.client.models.FcmTokenResponse
import org.openapitools.client.models.RegisterFcmTokenRequest
import java.io.IOException
import javax.inject.Inject

class FCMTokenRepository @Inject constructor(
    private val api: FCMTokensApi
) {
    suspend fun register(userId: Long, token: String, deviceName: String?): Result<FcmTokenResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = RegisterFcmTokenRequest(token = token, deviceName = deviceName)
                val response = api.registerFcmToken(userId, request)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Result.success(body)
                } else {
                    val error = response.errorBody()?.string() ?: "Error al registrar token"
                    Result.failure(IOException(error))
                }
            } catch (e: Exception) {
                Result.failure(IOException("Error de red: ${e.message}"))
            }
        }

    suspend fun list(userId: Long): Result<List<FcmTokenResponse>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.listFcmTokens(userId)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Result.success(body)
                } else {
                    Result.failure(IOException("Error al listar tokens"))
                }
            } catch (e: Exception) {
                Result.failure(IOException("Error de red: ${e.message}"))
            }
        }

    suspend fun remove(userId: Long, tokenId: Long): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.removeFcmToken(userId, tokenId)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(IOException("Error al eliminar token"))
                }
            } catch (e: Exception) {
                Result.failure(IOException("Error de red: ${e.message}"))
            }
        }
}

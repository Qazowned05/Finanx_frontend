package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.FCMTokensApi
import org.openapitools.client.models.FcmTokenResponse
import org.openapitools.client.models.PageResponseFcmTokenResponse
import org.openapitools.client.models.RegisterFcmTokenRequest
import java.io.IOException
import javax.inject.Inject

class FCMTokenRepository @Inject constructor(
    private val api: FCMTokensApi
) {
    suspend fun register(token: String, deviceName: String?): Result<FcmTokenResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = RegisterFcmTokenRequest(token = token, deviceName = deviceName)
                val response = api.registerFcmToken(request)
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

    suspend fun list(): Result<PageResponseFcmTokenResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.listFcmTokens()
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

    suspend fun remove(tokenId: Long): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.removeFcmToken(tokenId)
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

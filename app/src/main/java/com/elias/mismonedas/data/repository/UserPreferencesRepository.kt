package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.UserPreferencesApi
import org.openapitools.client.apis.WalletsApi
import org.openapitools.client.models.TimeZone
import org.openapitools.client.models.UserPreferencesRequest
import org.openapitools.client.models.UserPreferencesResponse
import org.openapitools.client.models.WalletRequest
import org.openapitools.client.models.WalletResponse
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userPreferencesApi: UserPreferencesApi,
    private val walletsApi: WalletsApi
) {
    suspend fun createPreferences(
        userId: Long,
        currencyCode: String,
        timeZone: TimeZone,
        emailNotifications: Boolean
    ): Result<UserPreferencesResponse> = withContext(Dispatchers.IO) {
        try {
            val request = UserPreferencesRequest(
                timeZone = timeZone,
                currencyCode = currencyCode,
                emailNotifications = emailNotifications
            )
            val response = userPreferencesApi.createUserPreferences(request)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(IOException("Error al crear preferencias: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Error de red al crear preferencias: ${e.message}"))
        }
    }

    suspend fun getPreferences(): Result<UserPreferencesResponse> = withContext(Dispatchers.IO) {
        try {
            val response = userPreferencesApi.getUserPreferences()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(IOException("Error al obtener preferencias: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Error de red al obtener preferencias: ${e.message}"))
        }
    }

    suspend fun updatePreferences(
        currencyCode: String,
        timeZone: TimeZone,
        emailNotifications: Boolean
    ): Result<UserPreferencesResponse> = withContext(Dispatchers.IO) {
        try {
            val request = UserPreferencesRequest(
                timeZone = timeZone,
                currencyCode = currencyCode,
                emailNotifications = emailNotifications
            )
            val response = userPreferencesApi.updateUserPreferences(request)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(IOException("Error al actualizar preferencias: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Error de red al actualizar preferencias: ${e.message}"))
        }
    }

    suspend fun createWallet(
        currencyCode: String
    ): Result<WalletResponse> = withContext(Dispatchers.IO) {
        try {
            val request = WalletRequest(
                currencyCode = currencyCode,
                primary = true
            )
            val response = walletsApi.create(request)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(IOException("Error al crear billetera: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Error de red al crear billetera: ${e.message}"))
        }
    }
}

package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.WalletsApi
import org.openapitools.client.models.WalletRequest
import org.openapitools.client.models.WalletResponse
import java.io.IOException
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val api: WalletsApi
) {
    suspend fun listByUser(userId: Long): List<WalletResponse> =
        withContext(Dispatchers.IO) {
            val response = api.findAllByUser(userId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al listar billeteras: ${response.code()}")
            }
        }

    suspend fun getById(userId: Long, walletId: Long): WalletResponse =
        withContext(Dispatchers.IO) {
            val response = api.findById(userId, walletId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al obtener billetera: ${response.code()}")
            }
        }

    suspend fun create(userId: Long, currencyCode: String, primary: Boolean = false): WalletResponse =
        withContext(Dispatchers.IO) {
            val request = WalletRequest(
                currencyCode = currencyCode,
                primary = primary
            )
            val response = api.create(userId, request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al crear billetera: ${response.code()}")
            }
        }

    suspend fun update(userId: Long, walletId: Long, currencyCode: String, primary: Boolean): WalletResponse =
        withContext(Dispatchers.IO) {
            val request = WalletRequest(
                currencyCode = currencyCode,
                primary = primary
            )
            val response = api.update(userId, walletId, request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al actualizar billetera: ${response.code()}")
            }
        }

    suspend fun disable(userId: Long, walletId: Long): Unit =
        withContext(Dispatchers.IO) {
            val response = api.disable(userId, walletId)
            if (!response.isSuccessful) {
                throw IOException("Error al deshabilitar billetera: ${response.code()}")
            }
        }
}

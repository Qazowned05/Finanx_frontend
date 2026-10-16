package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.WalletsApi
import org.openapitools.client.models.PageResponseWalletResponse
import org.openapitools.client.models.WalletRequest
import org.openapitools.client.models.WalletResponse
import java.io.IOException
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val api: WalletsApi
) {
    suspend fun list(): List<WalletResponse> =
        withContext(Dispatchers.IO) {
            val response = api.listWallets()
            if (response.isSuccessful && response.body() != null) {
                response.body()?.items!!
            } else {
                throw IOException("Error al listar billeteras: ${response.code()}")
            }
        }

    suspend fun listPage(
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE
    ): PageResponseWalletResponse =
        withContext(Dispatchers.IO) {
            val response = api.listWallets(
                page = page,
                size = size,
                sort = listOf("id,ASC")
            )
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al listar billeteras: ${response.code()}")
            }
        }

    suspend fun getById(walletId: Long): WalletResponse =
        withContext(Dispatchers.IO) {
            val response = api.findById(walletId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al obtener billetera: ${response.code()}")
            }
        }

    suspend fun create(currencyCode: String, primary: Boolean = false): WalletResponse =
        withContext(Dispatchers.IO) {
            val request = WalletRequest(
                currencyCode = currencyCode,
                primary = primary
            )
            val response = api.create(request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al crear billetera: ${response.code()}")
            }
        }

    suspend fun update(walletId: Long, currencyCode: String, primary: Boolean): WalletResponse =
        withContext(Dispatchers.IO) {
            val request = WalletRequest(
                currencyCode = currencyCode,
                primary = primary
            )
            val response = api.update(walletId, request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al actualizar billetera: ${response.code()}")
            }
        }

    suspend fun disable(walletId: Long): Unit =
        withContext(Dispatchers.IO) {
            val response = api.disable(walletId)
            if (!response.isSuccessful) {
                throw IOException("Error al deshabilitar billetera: ${response.code()}")
            }
        }
}

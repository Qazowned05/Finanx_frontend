package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.WalletRequest
import org.openapitools.client.models.WalletResponse

interface WalletsApi {
    /**
     * POST users/{userId}/wallets
     * Crear billetera
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param walletRequest 
     * @return [WalletResponse]
     */
    @POST("users/{userId}/wallets")
    suspend fun create(@Path("userId") userId: kotlin.Long, @Body walletRequest: WalletRequest): Response<WalletResponse>

    /**
     * DELETE users/{userId}/wallets/{id}
     * Deshabilitar billetera
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param id 
     * @return [Unit]
     */
    @DELETE("users/{userId}/wallets/{id}")
    suspend fun disable(@Path("userId") userId: kotlin.Long, @Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET users/{userId}/wallets
     * Listar billeteras de un usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @return [kotlin.collections.List<WalletResponse>]
     */
    @GET("users/{userId}/wallets")
    suspend fun findAllByUser(@Path("userId") userId: kotlin.Long): Response<kotlin.collections.List<WalletResponse>>

    /**
     * GET users/{userId}/wallets/{id}
     * Obtener billetera por ID
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param id 
     * @return [WalletResponse]
     */
    @GET("users/{userId}/wallets/{id}")
    suspend fun findById(@Path("userId") userId: kotlin.Long, @Path("id") id: kotlin.Long): Response<WalletResponse>

    /**
     * PUT users/{userId}/wallets/{id}
     * Actualizar billetera
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param id 
     * @param walletRequest 
     * @return [WalletResponse]
     */
    @PUT("users/{userId}/wallets/{id}")
    suspend fun update(@Path("userId") userId: kotlin.Long, @Path("id") id: kotlin.Long, @Body walletRequest: WalletRequest): Response<WalletResponse>

}

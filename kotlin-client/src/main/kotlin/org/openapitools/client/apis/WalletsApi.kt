package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PageResponseWalletResponse
import org.openapitools.client.models.WalletRequest
import org.openapitools.client.models.WalletResponse

interface WalletsApi {
    /**
     * POST api/v1/wallets
     * Crear billetera
     * 
     * Responses:
     *  - 200: OK
     *
     * @param walletRequest 
     * @return [WalletResponse]
     */
    @POST("api/v1/wallets")
    suspend fun create(@Body walletRequest: WalletRequest): Response<WalletResponse>

    /**
     * DELETE api/v1/wallets/{id}
     * Deshabilitar billetera
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("api/v1/wallets/{id}")
    suspend fun disable(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET api/v1/wallets/{id}
     * Obtener billetera por ID
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [WalletResponse]
     */
    @GET("api/v1/wallets/{id}")
    suspend fun findById(@Path("id") id: kotlin.Long): Response<WalletResponse>

    /**
     * GET api/v1/wallets
     * Listar billeteras del usuario autenticado
     * Lista paginada con page, size y sort estándar.
     * Responses:
     *  - 200: OK
     *
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("id,ASC"))
     * @return [PageResponseWalletResponse]
     */
    @GET("api/v1/wallets")
    suspend fun listWallets(@Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("id,ASC")): Response<PageResponseWalletResponse>

    /**
     * PUT api/v1/wallets/{id}
     * Actualizar billetera
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param walletRequest 
     * @return [WalletResponse]
     */
    @PUT("api/v1/wallets/{id}")
    suspend fun update(@Path("id") id: kotlin.Long, @Body walletRequest: WalletRequest): Response<WalletResponse>

}

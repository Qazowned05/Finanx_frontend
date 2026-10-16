package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PageResponseTransactionResponse
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse
import org.openapitools.client.models.TransactionType

interface TransactionsApi {
    /**
     * POST api/v1/transactions
     * Crear transaccion
     * Crea una nueva transaccion.
     * Responses:
     *  - 200: OK
     *
     * @param transactionRequest 
     * @return [TransactionResponse]
     */
    @POST("api/v1/transactions")
    suspend fun createTransaction(@Body transactionRequest: TransactionRequest): Response<TransactionResponse>

    /**
     * GET api/v1/transactions/{id}
     * Buscar transaccion
     * Obtiene una transaccion por ID.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [TransactionResponse]
     */
    @GET("api/v1/transactions/{id}")
    suspend fun findTransactionById(@Path("id") id: kotlin.Long): Response<TransactionResponse>

    /**
     * GET api/v1/transactions
     * Listar transacciones del usuario
     * Lista paginada del usuario autenticado. Admite filtros por active, type, walletId, categoryId, rango from/to y busqueda de texto libre sobre el motivo y la categoria; el orden usa sort estándar.
     * Responses:
     *  - 200: OK
     *
     * @param active  (optional)
     * @param type  (optional)
     * @param walletId  (optional)
     * @param categoryId  (optional)
     * @param from  (optional)
     * @param to  (optional)
     * @param search  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("createdAt,DESC"))
     * @return [PageResponseTransactionResponse]
     */
    @GET("api/v1/transactions")
    suspend fun listTransactions(@Query("active") active: kotlin.Boolean? = null, @Query("type") type: TransactionType? = null, @Query("walletId") walletId: kotlin.Long? = null, @Query("categoryId") categoryId: kotlin.Long? = null, @Query("from") from: java.time.LocalDate? = null, @Query("to") to: java.time.LocalDate? = null, @Query("search") search: kotlin.String? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("createdAt,DESC")): Response<PageResponseTransactionResponse>

    /**
     * DELETE api/v1/transactions/{id}
     * Eliminar transaccion
     * Deshabilita una transaccion.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("api/v1/transactions/{id}")
    suspend fun transactionDelete(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * PUT api/v1/transactions/{id}
     * Actualizar transaccion
     * Actualiza una transaccion.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param transactionRequest 
     * @return [TransactionResponse]
     */
    @PUT("api/v1/transactions/{id}")
    suspend fun updateTransaction(@Path("id") id: kotlin.Long, @Body transactionRequest: TransactionRequest): Response<TransactionResponse>

}

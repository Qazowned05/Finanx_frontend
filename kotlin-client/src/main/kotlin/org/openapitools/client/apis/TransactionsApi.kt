package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse

interface TransactionsApi {
    /**
     * POST transactions
     * Crear transaccion
     * Crea una nueva transaccion.
     * Responses:
     *  - 200: OK
     *
     * @param transactionRequest 
     * @return [TransactionResponse]
     */
    @POST("transactions")
    suspend fun createTransaction(@Body transactionRequest: TransactionRequest): Response<TransactionResponse>

    /**
     * GET transactions/user/{userId}/active
     * Listar transacciones activas
     * Obtiene todas las transacciones activas de un usuario.
     * Responses:
     *  - 200: OK
     *
     * @param userId ID usuario
     * @return [kotlin.collections.List<TransactionResponse>]
     */
    @GET("transactions/user/{userId}/active")
    suspend fun findAllActiveTransactionByUser(@Path("userId") userId: kotlin.Long): Response<kotlin.collections.List<TransactionResponse>>

    /**
     * GET transactions/user/{userId}/category/{categoryId}/between
     * Filtrar por categoria y fecha
     * Obtiene transacciones por categoria y rango de fechas.
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param categoryId 
     * @param from 
     * @param to 
     * @return [kotlin.collections.List<TransactionResponse>]
     */
    @GET("transactions/user/{userId}/category/{categoryId}/between")
    suspend fun findAllTransactionByCategoryAndDateBetween(@Path("userId") userId: kotlin.Long, @Path("categoryId") categoryId: kotlin.Long, @Query("from") from: java.time.LocalDate, @Query("to") to: java.time.LocalDate): Response<kotlin.collections.List<TransactionResponse>>

    /**
     * GET transactions/user/{userId}
     * Listar transacciones por usuario
     * Obtiene todas las transacciones de un usuario.
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @return [kotlin.collections.List<TransactionResponse>]
     */
    @GET("transactions/user/{userId}")
    suspend fun findAllTransactionByUser(@Path("userId") userId: kotlin.Long): Response<kotlin.collections.List<TransactionResponse>>

    /**
     * GET transactions/user/{userId}/between
     * Listar transacciones por rango
     * Obtiene transacciones en un rango de fechas.
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param from 
     * @param to 
     * @return [kotlin.collections.List<TransactionResponse>]
     */
    @GET("transactions/user/{userId}/between")
    suspend fun findAllTransactionsByUserBetween(@Path("userId") userId: kotlin.Long, @Query("from") from: java.time.LocalDate, @Query("to") to: java.time.LocalDate): Response<kotlin.collections.List<TransactionResponse>>

    /**
     * GET transactions/{id}
     * Buscar transaccion
     * Obtiene una transaccion por ID.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [TransactionResponse]
     */
    @GET("transactions/{id}")
    suspend fun findTransactionById(@Path("id") id: kotlin.Long): Response<TransactionResponse>

    /**
     * DELETE transactions/{id}
     * Eliminar transaccion
     * Deshabilita una transaccion.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("transactions/{id}")
    suspend fun transactionDelete(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * PUT transactions/{id}
     * Actualizar transaccion
     * Actualiza una transaccion.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param transactionRequest 
     * @return [TransactionResponse]
     */
    @PUT("transactions/{id}")
    suspend fun updateTransaction(@Path("id") id: kotlin.Long, @Body transactionRequest: TransactionRequest): Response<TransactionResponse>

}

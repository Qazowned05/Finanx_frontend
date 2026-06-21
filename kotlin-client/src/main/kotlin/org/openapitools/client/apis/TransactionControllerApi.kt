package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse

interface TransactionControllerApi {
    /**
     * POST transactions
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param transactionRequest 
     * @return [TransactionResponse]
     */
    @POST("transactions")
    suspend fun create(@Body transactionRequest: TransactionRequest): Response<TransactionResponse>

    /**
     * DELETE transactions/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("transactions/{id}")
    suspend fun disable(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET transactions/user/{userId}/active
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @return [kotlin.collections.List<TransactionResponse>]
     */
    @GET("transactions/user/{userId}/active")
    suspend fun findAllActiveByUser(@Path("userId") userId: kotlin.Long): Response<kotlin.collections.List<TransactionResponse>>

    /**
     * GET transactions/user/{userId}/category/{categoryId}/between
     * 
     * 
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
    suspend fun findAllByCategoryAndDateBetween(@Path("userId") userId: kotlin.Long, @Path("categoryId") categoryId: kotlin.Long, @Query("from") from: java.time.LocalDate, @Query("to") to: java.time.LocalDate): Response<kotlin.collections.List<TransactionResponse>>

    /**
     * GET transactions/{userId}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @return [kotlin.collections.List<TransactionResponse>]
     */
    @GET("transactions/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: kotlin.Long): Response<kotlin.collections.List<TransactionResponse>>

    /**
     * GET transactions/user/{userId}/between
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param from 
     * @param to 
     * @return [kotlin.collections.List<TransactionResponse>]
     */
    @GET("transactions/user/{userId}/between")
    suspend fun findAllByUserBetween(@Path("userId") userId: kotlin.Long, @Query("from") from: java.time.LocalDate, @Query("to") to: java.time.LocalDate): Response<kotlin.collections.List<TransactionResponse>>

    /**
     * GET transactions/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [TransactionResponse]
     */
    @GET("transactions/{id}")
    suspend fun findById1(@Path("id") id: kotlin.Long): Response<TransactionResponse>

    /**
     * PUT transactions/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param transactionRequest 
     * @return [TransactionResponse]
     */
    @PUT("transactions/{id}")
    suspend fun update1(@Path("id") id: kotlin.Long, @Body transactionRequest: TransactionRequest): Response<TransactionResponse>

}

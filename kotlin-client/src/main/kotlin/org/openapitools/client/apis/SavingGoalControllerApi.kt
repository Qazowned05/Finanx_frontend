package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse

interface SavingGoalControllerApi {
    /**
     * POST savings/{id}/cancel
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("savings/{id}/cancel")
    suspend fun cancel1(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST savings
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param savingGoalRequest 
     * @return [SavingGoalResponse]
     */
    @POST("savings")
    suspend fun create2(@Body savingGoalRequest: SavingGoalRequest): Response<SavingGoalResponse>

    /**
     * POST savings/{id}/disable
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("savings/{id}/disable")
    suspend fun disable3(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET savings/user/{idUser}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<SavingGoalResponse>]
     */
    @GET("savings/user/{idUser}")
    suspend fun findAllActiveByUser1(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<SavingGoalResponse>>

    /**
     * GET savings/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [SavingGoalResponse]
     */
    @GET("savings/{id}")
    suspend fun findById2(@Path("id") id: kotlin.Long): Response<SavingGoalResponse>

    /**
     * PUT savings/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param savingGoalRequest 
     * @return [SavingGoalResponse]
     */
    @PUT("savings/{id}")
    suspend fun update3(@Path("id") id: kotlin.Long, @Body savingGoalRequest: SavingGoalRequest): Response<SavingGoalResponse>

}

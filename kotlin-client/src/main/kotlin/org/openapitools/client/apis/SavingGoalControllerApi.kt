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
    suspend fun cancelSavingGoal(@Path("id") id: kotlin.Long): Response<Unit>

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
    suspend fun createSavingGoal(@Body savingGoalRequest: SavingGoalRequest): Response<SavingGoalResponse>

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
    suspend fun disableSavingGoal(@Path("id") id: kotlin.Long): Response<Unit>

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
    suspend fun findSavingGoalById(@Path("id") id: kotlin.Long): Response<SavingGoalResponse>

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
    suspend fun findSavingGoalsByUser(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<SavingGoalResponse>>

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
    suspend fun updateSavingGoal(@Path("id") id: kotlin.Long, @Body savingGoalRequest: SavingGoalRequest): Response<SavingGoalResponse>

}

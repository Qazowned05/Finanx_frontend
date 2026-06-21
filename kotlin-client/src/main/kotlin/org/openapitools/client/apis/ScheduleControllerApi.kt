package org.openapitools.client.apis

import retrofit2.http.*
import retrofit2.Response
import com.squareup.moshi.Json
import org.openapitools.client.models.TransactionScheduleRequest

import org.openapitools.client.models.Update2200Response
import org.openapitools.client.models.Update2Request

interface ScheduleControllerApi {
    /**
     * POST schedule/{id}/cancel
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("schedule/{id}/cancel")
    suspend fun cancel(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST schedule
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param update2Request 
     * @return [Update2200Response]
     */
    @POST("schedule")
    suspend fun create1(@Body update2Request: TransactionScheduleRequest): Response<Update2200Response>

    /**
     * DELETE schedule/{id}/disable
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("schedule/{id}/disable")
    suspend fun disable6(@Path("id") id: kotlin.Long): Response<Unit>


    /**
    * enum for parameter state
    */
    enum class StateFindAllByUserAndState(val value: kotlin.String) {
        @Json(name = "RUNNING") RUNNING("RUNNING"),
        @Json(name = "PAUSED") PAUSED("PAUSED"),
        @Json(name = "FINALIZED") FINALIZED("FINALIZED"),
        @Json(name = "CANCELLED") CANCELLED("CANCELLED"),
        @Json(name = "CREATED") CREATED("CREATED"),
        @Json(name = "ERROR") ERROR("ERROR")
    }

    /**
     * GET schedule/user/{idUser}/{state}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @param state 
     * @return [kotlin.collections.List<Update2200Response>]
     */
    @GET("schedule/user/{idUser}/{state}")
    suspend fun findAllByUserAndState(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<Update2200Response>>

    /**
     * POST schedule/{id}/pause
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("schedule/{id}/pause")
    suspend fun pause(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST schedule/{id}/resume
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("schedule/{id}/resume")
    suspend fun resume(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * PUT schedule/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param update2Request 
     * @return [Update2200Response]
     */
    @PUT("schedule/{id}")
    suspend fun update2(@Path("id") id: kotlin.Long, @Body update2Request: Update2Request): Response<Update2200Response>

}

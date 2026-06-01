package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse

interface BudgetControllerApi {
    /**
     * POST bugdets/{id}/cancel
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("bugdets/{id}/cancel")
    suspend fun cancel(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST bugdets
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param budgetRequest 
     * @return [BudgetResponse]
     */
    @POST("bugdets")
    suspend fun create5(@Body budgetRequest: BudgetRequest): Response<BudgetResponse>

    /**
     * POST bugdets/{id}/disable
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("bugdets/{id}/disable")
    suspend fun disable5(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET bugdets/{idUser}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<BudgetResponse>]
     */
    @GET("bugdets/{idUser}")
    suspend fun findAllByUser3(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<BudgetResponse>>


    /**
    * enum for parameter state
    */
    enum class StateFindAllByUserAndState1(val value: kotlin.String) {
        @Json(name = "ACTIVE") ACTIVE("ACTIVE"),
        @Json(name = "CANCELLED") CANCELLED("CANCELLED"),
        @Json(name = "FINALIZED") FINALIZED("FINALIZED")
    }

    /**
     * GET bugdets/{idUser}/{state}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @param state 
     * @return [kotlin.collections.List<BudgetResponse>]
     */
    @GET("bugdets/{idUser}/{state}")
    suspend fun findAllByUserAndState1(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<BudgetResponse>>

    /**
     * GET bugdets/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [BudgetResponse]
     */
    @GET("bugdets/{id}")
    suspend fun findById6(@Path("id") id: kotlin.Long): Response<BudgetResponse>

    /**
     * PUT bugdets/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param budgetRequest 
     * @return [BudgetResponse]
     */
    @PUT("bugdets/{id}")
    suspend fun update6(@Path("id") id: kotlin.Long, @Body budgetRequest: BudgetRequest): Response<BudgetResponse>

}

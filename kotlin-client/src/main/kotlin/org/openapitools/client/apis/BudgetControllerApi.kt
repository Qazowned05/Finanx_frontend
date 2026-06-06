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
    suspend fun cancel2(@Path("id") id: kotlin.Long): Response<Unit>

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
     * GET bugdets/user/{idUser}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<BudgetResponse>]
     */
    @GET("bugdets/user/{idUser}")
    suspend fun findAllByUser4(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<BudgetResponse>>


    /**
    * enum for parameter state
    */
    enum class StateFindAllByUserAndState4(val value: kotlin.String) {
        @Json(name = "ACTIVE") ACTIVE("ACTIVE"),
        @Json(name = "CANCELLED") CANCELLED("CANCELLED"),
        @Json(name = "DISABLED") DISABLED("DISABLED"),
        @Json(name = "FINALIZED") FINALIZED("FINALIZED")
    }

    /**
     * GET bugdets/user/{idUser}/{state}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @param state 
     * @return [kotlin.collections.List<BudgetResponse>]
     */
    @GET("bugdets/user/{idUser}/{state}")
    suspend fun findAllByUserAndState4(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<BudgetResponse>>

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
    suspend fun findById5(@Path("id") id: kotlin.Long): Response<BudgetResponse>

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

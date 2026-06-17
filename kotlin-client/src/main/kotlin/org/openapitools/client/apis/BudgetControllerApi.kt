package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.TimeBoundListBudgetResponse

import org.openapitools.client.models.*

interface BudgetControllerApi {
    /**
     * POST budgets/{id}/cancel
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("budgets/{id}/cancel")
    suspend fun cancel2(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST budgets
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param budgetRequest 
     * @return [BudgetResponse]
     */
    @POST("budgets")
    suspend fun create5(@Body budgetRequest: BudgetRequest): Response<BudgetResponse>

    /**
     * POST budgets/{id}/disable
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("budgets/{id}/disable")
    suspend fun disable5(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET budgets/user/{idUser}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<BudgetResponse>]
     */
    @GET("budgets/user/{idUser}")
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
     * GET budgets/user/{idUser}/{state}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @param state 
     * @return [kotlin.collections.List<BudgetResponse>]
     */
    @GET("budgets/user/{idUser}/{state}")
    suspend fun findAllByUserAndState4(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<BudgetResponse>>

    /**
     * GET budgets/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [BudgetResponse]
     */
    @GET("budgets/{id}")
    suspend fun findById5(@Path("id") id: kotlin.Long): Response<BudgetResponse>


    /**
    * enum for parameter health
    */
    enum class HealthGetBudgetsByHealth(val value: kotlin.String) {
        @Json(name = "OK") OK("OK"),
        @Json(name = "NEAR_LIMIT") NEAR_LIMIT("NEAR_LIMIT"),
        @Json(name = "EXCEEDED") EXCEEDED("EXCEEDED"),
        @Json(name = "INACTIVE") INACTIVE("INACTIVE")
    }

    /**
     * GET budgets/{health}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param health 
     * @param start 
     * @param end 
     * @param userId 
     * @return [TimeBoundListBudgetResponse]
     */
    @GET("budgets/{health}")
    suspend fun getBudgetsByHealth(@Path("health") health: kotlin.String, @Query("start") start: java.time.LocalDate, @Query("end") end: java.time.LocalDate, @Query("userId") userId: kotlin.Long): Response<TimeBoundListBudgetResponse>

    /**
     * PUT budgets/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param budgetRequest 
     * @return [BudgetResponse]
     */
    @PUT("budgets/{id}")
    suspend fun update6(@Path("id") id: kotlin.Long, @Body budgetRequest: BudgetRequest): Response<BudgetResponse>

}

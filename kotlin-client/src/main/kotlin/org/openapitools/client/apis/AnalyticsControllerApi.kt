package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.Budgets200Response
import org.openapitools.client.models.PeriodRequest

import org.openapitools.client.models.*

interface AnalyticsControllerApi {
    /**
     * GET analytics/dashboard/balance
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param periodRequest 
     * @param prompt  (optional)
     * @return [Budgets200Response]
     */
    @GET("analytics/dashboard/balance")
    suspend fun balance(@Body periodRequest: PeriodRequest, @Query("prompt") prompt: kotlin.String? = null): Response<Budgets200Response>

    /**
     * GET analytics/dashboard/budgets
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param periodRequest 
     * @param prompt  (optional)
     * @return [Budgets200Response]
     */
    @GET("analytics/dashboard/budgets")
    suspend fun budgets(@Body periodRequest: PeriodRequest, @Query("prompt") prompt: kotlin.String? = null): Response<Budgets200Response>

    /**
     * GET analytics/budgets/{id}/execution
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [BudgetExecutionResponse]
     */
    @GET("analytics/budgets/{id}/execution")
    suspend fun getExecution(@Path("id") id: kotlin.Long): Response<BudgetExecutionResponse>

    /**
     * GET analytics/budgets/executions
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param start 
     * @param end 
     * @param userId 
     * @return [kotlin.collections.List<BudgetExecutionResponse>]
     */
    @GET("analytics/budgets/executions")
    suspend fun getExecutionsByPeriod(@Query("start") start: java.time.LocalDate, @Query("end") end: java.time.LocalDate, @Query("userId") userId: kotlin.Long): Response<kotlin.collections.List<BudgetExecutionResponse>>

}

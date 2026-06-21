package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.Budgets200Response
import org.openapitools.client.models.PeriodRequest

interface AnalyticsControllerApi {
    /**
     * POST analytics/dashboard/balance
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param periodRequest 
     * @param prompt  (optional)
     * @return [Budgets200Response]
     */
    @POST("analytics/dashboard/balance")
    suspend fun balance(@Body periodRequest: PeriodRequest, @Query("prompt") prompt: kotlin.String? = null): Response<Budgets200Response>

    /**
     * POST analytics/dashboard/budgets
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param periodRequest 
     * @param prompt  (optional)
     * @return [Budgets200Response]
     */
    @POST("analytics/dashboard/budgets")
    suspend fun budgets(@Body periodRequest: PeriodRequest, @Query("prompt") prompt: kotlin.String? = null): Response<Budgets200Response>

    /**
     * POST analytics/budgets/{id}/execution
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [BudgetExecutionResponse]
     */
    @POST("analytics/budgets/{id}/execution")
    suspend fun getExecution(@Path("id") id: kotlin.Long): Response<BudgetExecutionResponse>

    /**
     * POST analytics/budgets/executions
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param periodRequest 
     * @return [kotlin.collections.List<BudgetExecutionResponse>]
     */
    @POST("analytics/budgets/executions")
    suspend fun getExecutionsByPeriod(@Body periodRequest: PeriodRequest): Response<kotlin.collections.List<BudgetExecutionResponse>>

}

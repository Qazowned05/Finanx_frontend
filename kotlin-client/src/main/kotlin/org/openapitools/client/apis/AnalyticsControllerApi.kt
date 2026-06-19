package org.openapitools.client.apis

import retrofit2.http.*
import retrofit2.Response

import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.Budgets200Response
import org.openapitools.client.models.PeriodRequest

interface AnalyticsControllerApi {
    /**
     * GET analytics/dashboard/balance
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param periodRequest 
     * @return [Budgets200Response]
     */
    @GET("analytics/dashboard/balance")
    suspend fun balance(@Body periodRequest: PeriodRequest): Response<Budgets200Response>

    /**
     * GET analytics/dashboard/budgets
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param periodRequest 
     * @return [Budgets200Response]
     */
    @GET("analytics/dashboard/budgets")
    suspend fun budgets(@Body periodRequest: PeriodRequest): Response<Budgets200Response>

    /**
     * GET analytics/{id}/execution
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [BudgetExecutionResponse]
     */
    @GET("analytics/{id}/execution")
    suspend fun getExecution(@Path("id") id: kotlin.Long): Response<BudgetExecutionResponse>

}

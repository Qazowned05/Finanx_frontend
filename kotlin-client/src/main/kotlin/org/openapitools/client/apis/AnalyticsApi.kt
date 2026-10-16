package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetsDashboard
import org.openapitools.client.models.DashboardResponse
import org.openapitools.client.models.PageResponseBudgetExecutionResponse
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.TransactionsDashboard

interface AnalyticsApi {
    /**
     * POST api/v1/analytics/dashboard/balance
     * Dashboard de balance
     * Genera el dashboard de balance basado en un periodo y un prompt opcional.
     * Responses:
     *  - 200: Dashboard generado
     *  - 400: Request invalido
     *
     * @param periodRequest 
     * @return [TransactionsDashboard]
     */
    @POST("api/v1/analytics/dashboard/balance")
    suspend fun getBalanceDashboard(@Body periodRequest: PeriodRequest): Response<TransactionsDashboard>

    /**
     * POST api/v1/analytics/budgets/{id}/execution
     * Ejecucion de presupuesto
     * Obtiene la ejecucion de un presupuesto por ID.
     * Responses:
     *  - 200: Ejecucion encontrada
     *  - 404: No encontrado
     *
     * @param id ID presupuesto
     * @return [BudgetExecutionResponse]
     */
    @POST("api/v1/analytics/budgets/{id}/execution")
    suspend fun getBudgetExecution(@Path("id") id: kotlin.Long): Response<BudgetExecutionResponse>

    /**
     * POST api/v1/analytics/dashboard/budgets
     * Dashboard de presupuestos
     * Genera el dashboard de presupuestos.
     * Responses:
     *  - 200: Dashboard generado
     *  - 400: Request invalido
     *
     * @param periodRequest 
     * @return [BudgetsDashboard]
     */
    @POST("api/v1/analytics/dashboard/budgets")
    suspend fun getBudgetsDashboard(@Body periodRequest: PeriodRequest): Response<BudgetsDashboard>

    /**
     * GET api/v1/analytics/budgets/executions
     * Ejecuciones por periodo
     * Obtiene ejecuciones de presupuestos paginadas por un rango de fechas del usuario.
     * Responses:
     *  - 200: Listado obtenido
     *  - 400: Request invalido
     *
     * @param from 
     * @param to 
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("createdAt,DESC"))
     * @return [PageResponseBudgetExecutionResponse]
     */
    @GET("api/v1/analytics/budgets/executions")
    suspend fun getBudgetsExecutionsByPeriod(@Query("from") from: java.time.LocalDate, @Query("to") to: java.time.LocalDate, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("createdAt,DESC")): Response<PageResponseBudgetExecutionResponse>

}

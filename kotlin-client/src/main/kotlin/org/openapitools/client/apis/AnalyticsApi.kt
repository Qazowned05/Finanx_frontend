package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response

import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetsDashboard
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.TransactionsDashboard

interface AnalyticsApi {
    /**
     * POST analytics/dashboard/balance
     * Dashboard de balance
     * Genera el dashboard de balance basado en un periodo.
     * Responses:
     *  - 200: Dashboard generado
     *  - 400: Request invalido
     *
     * @param periodRequest
     * @return [TransactionsDashboard]
     */
    @POST("analytics/dashboard/balance")
    suspend fun getBalanceDashboard(@Body periodRequest: PeriodRequest): Response<TransactionsDashboard>

    /**
     * POST analytics/budgets/{id}/execution
     * Ejecucion de presupuesto
     * Obtiene la ejecucion de un presupuesto por ID.
     * Responses:
     *  - 200: Ejecucion encontrada
     *  - 404: No encontrado
     *
     * @param id ID presupuesto
     * @return [BudgetExecutionResponse]
     */
    @POST("analytics/budgets/{id}/execution")
    suspend fun getBudgetExecution(@Path("id") id: kotlin.Long): Response<BudgetExecutionResponse>

    /**
     * POST analytics/dashboard/budgets
     * Dashboard de presupuestos
     * Genera el dashboard de presupuestos.
     * Responses:
     *  - 200: Dashboard generado
     *  - 400: Request invalido
     *
     * @param periodRequest
     * @return [BudgetsDashboard]
     */
    @POST("analytics/dashboard/budgets")
    suspend fun getBudgetsDashboard(@Body periodRequest: PeriodRequest): Response<BudgetsDashboard>

    /**
     * POST analytics/budgets/executions
     * Ejecuciones por periodo
     * Obtiene ejecuciones de presupuestos por periodo.
     * Responses:
     *  - 200: Listado obtenido
     *  - 400: Request invalido
     *
     * @param periodRequest 
     * @return [kotlin.collections.List<BudgetExecutionResponse>]
     */
    @POST("analytics/budgets/executions")
    suspend fun getBudgetsExecutionsByPeriod(@Body periodRequest: PeriodRequest): Response<kotlin.collections.List<BudgetExecutionResponse>>

}

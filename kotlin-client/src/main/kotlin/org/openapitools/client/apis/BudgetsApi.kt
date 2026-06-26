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

interface BudgetsApi {
    /**
     * POST budgets/{id}/cancel
     * Cancelar presupuesto
     * Cancela un presupuesto.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("budgets/{id}/cancel")
    suspend fun cancelBudget(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST budgets
     * Crear presupuesto
     * Crea un nuevo presupuesto.
     * Responses:
     *  - 201: Creado
     *  - 400: Datos invalidos
     *
     * @param budgetRequest 
     * @return [BudgetResponse]
     */
    @POST("budgets")
    suspend fun createBudget(@Body budgetRequest: BudgetRequest): Response<BudgetResponse>

    /**
     * POST budgets/{id}/disable
     * Deshabilitar presupuesto
     * Deshabilita un presupuesto.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("budgets/{id}/disable")
    suspend fun disableBudget(@Path("id") id: kotlin.Long): Response<Unit>


    /**
    * enum for parameter health
    */
    enum class HealthFindAllBudgetsByPeriodAndHealth(val value: kotlin.String) {
        @Json(name = "OK") OK("OK"),
        @Json(name = "NEAR_LIMIT") NEAR_LIMIT("NEAR_LIMIT"),
        @Json(name = "EXCEEDED") EXCEEDED("EXCEEDED"),
        @Json(name = "INACTIVE") INACTIVE("INACTIVE")
    }

    /**
     * POST budgets/{health}
     * Filtrar por salud
     * Filtra presupuestos por health.
     * Responses:
     *  - 200: OK
     *
     * @param health 
     * @param periodRequest 
     * @return [TimeBoundListBudgetResponse]
     */
    @POST("budgets/{health}")
    suspend fun findAllBudgetsByPeriodAndHealth(@Path("health") health: kotlin.String, @Body periodRequest: PeriodRequest): Response<TimeBoundListBudgetResponse>

    /**
     * GET budgets/user/{idUser}
     * Listar presupuestos por usuario
     * Obtiene todos los presupuestos de un usuario.
     * Responses:
     *  - 200: Listado obtenido
     *  - 404: Usuario no encontrado
     *
     * @param idUser ID usuario
     * @return [kotlin.collections.List<BudgetResponse>]
     */
    @GET("budgets/user/{idUser}")
    suspend fun findAllBudgetsByUser(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<BudgetResponse>>


    /**
    * enum for parameter state
    */
    enum class StateFindAllBudgetsByUserAndState(val value: kotlin.String) {
        @Json(name = "ACTIVE") ACTIVE("ACTIVE"),
        @Json(name = "CANCELLED") CANCELLED("CANCELLED"),
        @Json(name = "DISABLED") DISABLED("DISABLED"),
        @Json(name = "FINALIZED") FINALIZED("FINALIZED")
    }

    /**
     * GET budgets/user/{idUser}/{state}
     * Listar por usuario y estado
     * Obtiene presupuestos filtrados por estado.
     * Responses:
     *  - 200: Listado obtenido
     *
     * @param idUser 
     * @param state 
     * @return [kotlin.collections.List<BudgetResponse>]
     */
    @GET("budgets/user/{idUser}/{state}")
    suspend fun findAllBudgetsByUserAndState(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<BudgetResponse>>

    /**
     * GET budgets/{id}
     * Buscar presupuesto
     * Obtiene un presupuesto por ID.
     * Responses:
     *  - 200: Encontrado
     *  - 404: No encontrado
     *
     * @param id 
     * @return [BudgetResponse]
     */
    @GET("budgets/{id}")
    suspend fun findBudgetById(@Path("id") id: kotlin.Long): Response<BudgetResponse>

    /**
     * PUT budgets/{id}
     * Actualizar presupuesto
     * Actualiza un presupuesto.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param budgetRequest 
     * @return [BudgetResponse]
     */
    @PUT("budgets/{id}")
    suspend fun updateBudget(@Path("id") id: kotlin.Long, @Body budgetRequest: BudgetRequest): Response<BudgetResponse>

}

package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.BudgetHealth
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.BudgetState
import org.openapitools.client.models.PageResponseBudgetResponse

interface BudgetsApi {
    /**
     * POST api/v1/budgets/{id}/cancel
     * Cancelar presupuesto
     * Cancela un presupuesto.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("api/v1/budgets/{id}/cancel")
    suspend fun cancelBudget(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST api/v1/budgets
     * Crear presupuesto
     * Crea un nuevo presupuesto.
     * Responses:
     *  - 201: Creado
     *  - 400: Datos invalidos
     *
     * @param budgetRequest 
     * @return [BudgetResponse]
     */
    @POST("api/v1/budgets")
    suspend fun createBudget(@Body budgetRequest: BudgetRequest): Response<BudgetResponse>

    /**
     * POST api/v1/budgets/{id}/disable
     * Deshabilitar presupuesto
     * Deshabilita un presupuesto.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("api/v1/budgets/{id}/disable")
    suspend fun disableBudget(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET api/v1/budgets/{id}
     * Buscar presupuesto
     * Obtiene un presupuesto por ID.
     * Responses:
     *  - 200: Encontrado
     *  - 404: No encontrado
     *
     * @param id 
     * @return [BudgetResponse]
     */
    @GET("api/v1/budgets/{id}")
    suspend fun findBudgetById(@Path("id") id: kotlin.Long): Response<BudgetResponse>

    /**
     * GET api/v1/budgets
     * Listar presupuestos por usuario
     * Lista paginada del usuario autenticado. Admite filtros active, state, health y rango from/to; active&#x3D;true por defecto.
     * Responses:
     *  - 200: Listado obtenido
     *  - 404: Usuario no encontrado
     *
     * @param active  (optional, default to true)
     * @param state  (optional)
     * @param health  (optional)
     * @param from  (optional)
     * @param to  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("end,ASC"))
     * @return [PageResponseBudgetResponse]
     */
    @GET("api/v1/budgets")
    suspend fun listBudgets(@Query("active") active: kotlin.Boolean? = true, @Query("state") state: BudgetState? = null, @Query("health") health: BudgetHealth? = null, @Query("from") from: java.time.LocalDate? = null, @Query("to") to: java.time.LocalDate? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("end,ASC")): Response<PageResponseBudgetResponse>

    /**
     * PUT api/v1/budgets/{id}
     * Actualizar presupuesto
     * Actualiza un presupuesto.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param budgetRequest 
     * @return [BudgetResponse]
     */
    @PUT("api/v1/budgets/{id}")
    suspend fun updateBudget(@Path("id") id: kotlin.Long, @Body budgetRequest: BudgetRequest): Response<BudgetResponse>

}

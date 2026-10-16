package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PageResponseSavingGoalResponse
import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse
import org.openapitools.client.models.SavingGoalState

interface SavingsApi {
    /**
     * POST api/v1/savings/{id}/cancel
     * Cancelar meta
     * Cancela una meta de ahorro.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("api/v1/savings/{id}/cancel")
    suspend fun cancelSavingGoal(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST api/v1/savings
     * Crear meta
     * Crea una nueva meta de ahorro.
     * Responses:
     *  - 201: Creada
     *  - 400: Datos invalidos
     *
     * @param savingGoalRequest 
     * @return [SavingGoalResponse]
     */
    @POST("api/v1/savings")
    suspend fun createSavingGoal(@Body savingGoalRequest: SavingGoalRequest): Response<SavingGoalResponse>

    /**
     * POST api/v1/savings/{id}/disable
     * Deshabilitar meta
     * Deshabilita una meta.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("api/v1/savings/{id}/disable")
    suspend fun disableSavingGoal(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET api/v1/savings/{id}
     * Buscar meta
     * Obtiene una meta de ahorro por ID.
     * Responses:
     *  - 200: Encontrada
     *  - 404: No encontrada
     *
     * @param id ID meta
     * @return [SavingGoalResponse]
     */
    @GET("api/v1/savings/{id}")
    suspend fun findSavingGoalById(@Path("id") id: kotlin.Long): Response<SavingGoalResponse>

    /**
     * GET api/v1/savings
     * Listar metas del usuario
     * Obtiene las metas de ahorro del usuario autenticado.
     * Responses:
     *  - 200: Listado obtenido
     *  - 404: Usuario no encontrado
     *
     * @param active  (optional, default to true)
     * @param state  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("deadline,ASC"))
     * @return [PageResponseSavingGoalResponse]
     */
    @GET("api/v1/savings")
    suspend fun listSavingGoals(@Query("active") active: kotlin.Boolean? = true, @Query("state") state: SavingGoalState? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("deadline,ASC")): Response<PageResponseSavingGoalResponse>

    /**
     * PUT api/v1/savings/{id}
     * Actualizar meta
     * Actualiza una meta de ahorro.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param savingGoalRequest 
     * @return [SavingGoalResponse]
     */
    @PUT("api/v1/savings/{id}")
    suspend fun updateSavingGoal(@Path("id") id: kotlin.Long, @Body savingGoalRequest: SavingGoalRequest): Response<SavingGoalResponse>

}

package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse

interface SavingsApi {
    /**
     * POST savings/{id}/cancel
     * Cancelar meta
     * Cancela una meta de ahorro.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("savings/{id}/cancel")
    suspend fun cancelSavingGoal(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST savings
     * Crear meta
     * Crea una nueva meta de ahorro.
     * Responses:
     *  - 201: Creada
     *  - 400: Datos invalidos
     *
     * @param savingGoalRequest 
     * @return [SavingGoalResponse]
     */
    @POST("savings")
    suspend fun createSavingGoal(@Body savingGoalRequest: SavingGoalRequest): Response<SavingGoalResponse>

    /**
     * POST savings/{id}/disable
     * Deshabilitar meta
     * Deshabilita una meta.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("savings/{id}/disable")
    suspend fun disableSavingGoal(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET savings/user/{idUser}
     * Listar metas activas
     * Obtiene metas de ahorro activas por usuario.
     * Responses:
     *  - 200: Listado obtenido
     *
     * @param idUser ID usuario
     * @return [kotlin.collections.List<SavingGoalResponse>]
     */
    @GET("savings/user/{idUser}")
    suspend fun findAllActiveSavingGoalByUser(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<SavingGoalResponse>>

    /**
     * GET savings/{id}
     * Obtener una meta de ahorro
     * Obtiene una meta de ahorro por ID.
     * Responses:
     *  - 200: Encontrada
     *  - 404: No encontrada
     *
     * @param id ID meta
     * @return [SavingGoalResponse]
     */
    @GET("savings/{id}")
    suspend fun findSavingGoalById(@Path("id") id: kotlin.Long): Response<SavingGoalResponse>

    /**
     * PUT savings/{id}
     * Actualizar meta
     * Actualiza una meta de ahorro.
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param savingGoalRequest 
     * @return [SavingGoalResponse]
     */
    @PUT("savings/{id}")
    suspend fun updateSavingGoal(@Path("id") id: kotlin.Long, @Body savingGoalRequest: SavingGoalRequest): Response<SavingGoalResponse>

}

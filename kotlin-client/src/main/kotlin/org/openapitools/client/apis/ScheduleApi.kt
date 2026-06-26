package org.openapitools.client.apis

import retrofit2.http.*
import retrofit2.Response
import com.squareup.moshi.Json

import org.openapitools.client.models.ScheduleRequest
import org.openapitools.client.models.ScheduleResponse

interface ScheduleApi {
    /**
     * POST schedule/{id}/cancel
     * Cancelar programacion
     * Cancela una programacion.
     * Responses:
     *  - 204: Cancelada
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @return [Unit]
     */
    @POST("schedule/{id}/cancel")
    suspend fun cancelSchedule(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST schedule
     * Crear programacion
     * Crea una nueva programacion.
     * Responses:
     *  - 201: Programacion creada
     *  - 400: Datos invalidos
     *
     * @param scheduleRequest
     * @return [ScheduleResponse]
     */
    @POST("schedule")
    suspend fun createSchedule(@Body scheduleRequest: ScheduleRequest): Response<ScheduleResponse>

    /**
     * DELETE schedule/{id}/disable
     * Deshabilitar programacion
     * Deshabilita una programacion.
     * Responses:
     *  - 204: Deshabilitada
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @return [Unit]
     */
    @DELETE("schedule/{id}/disable")
    suspend fun disableSchedule(@Path("id") id: kotlin.Long): Response<Unit>


    /**
    * enum for parameter state
    */
    enum class StateFindAllScheduleByUserAndState(val value: kotlin.String) {
        @Json(name = "RUNNING") RUNNING("RUNNING"),
        @Json(name = "PAUSED") PAUSED("PAUSED"),
        @Json(name = "FINALIZED") FINALIZED("FINALIZED"),
        @Json(name = "CANCELLED") CANCELLED("CANCELLED"),
        @Json(name = "CREATED") CREATED("CREATED"),
        @Json(name = "ERROR") ERROR("ERROR")
    }

    /**
     * GET schedule/user/{idUser}/{state}
     * Listar programaciones por usuario y estado
     * Obtiene programaciones activas filtradas por usuario y estado.
     * Responses:
     *  - 200: Listado obtenido
     *
     * @param idUser ID usuario
     * @param state Estado de la programacion
     * @return [kotlin.collections.List<UpdateSchedule200Response>]
     */
    @GET("schedule/user/{idUser}/{state}")
    suspend fun findAllScheduleByUserAndState(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<ScheduleResponse>>

    /**
     * POST schedule/{id}/pause
     * Pausar programacion
     * Pausa una programacion.
     * Responses:
     *  - 204: Pausada
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @return [Unit]
     */
    @POST("schedule/{id}/pause")
    suspend fun pauseSchedule(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST schedule/{id}/resume
     * Reanudar programacion
     * Reanuda una programacion pausada.
     * Responses:
     *  - 204: Reanudada
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @return [Unit]
     */
    @POST("schedule/{id}/resume")
    suspend fun resumeSchedule(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * PUT schedule/{id}
     * Actualizar programacion
     * Actualiza una programacion existente.
     * Responses:
     *  - 200: Actualizada
     *  - 400: Datos invalidos
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @param scheduleRequest
     * @return [ScheduleRequest]
     */
    @PUT("schedule/{id}")
    suspend fun updateSchedule(@Path("id") id: kotlin.Long, @Body scheduleRequest: ScheduleRequest): Response<ScheduleResponse>

}

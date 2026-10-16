package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PageResponseScheduleResponse
import org.openapitools.client.models.ScheduleRequest
import org.openapitools.client.models.ScheduleResponse
import org.openapitools.client.models.ScheduleState

interface ScheduleApi {
    /**
     * POST api/v1/schedule/{id}/cancel
     * Cancelar programacion
     * Cancela una programacion.
     * Responses:
     *  - 204: Cancelada
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @return [Unit]
     */
    @POST("api/v1/schedule/{id}/cancel")
    suspend fun cancelSchedule(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST api/v1/schedule
     * Crear programacion
     * Crea una nueva programacion.
     * Responses:
     *  - 201: Programacion creada
     *  - 400: Datos invalidos
     *
     * @param scheduleRequest 
     * @return [ScheduleResponse]
     */
    @POST("api/v1/schedule")
    suspend fun createSchedule(@Body scheduleRequest: ScheduleRequest): Response<ScheduleResponse>

    /**
     * DELETE api/v1/schedule/{id}/disable
     * Deshabilitar programacion
     * Deshabilita una programacion.
     * Responses:
     *  - 204: Deshabilitada
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @return [Unit]
     */
    @DELETE("api/v1/schedule/{id}/disable")
    suspend fun disableSchedule(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET api/v1/schedule
     * Listar programaciones por usuario y estado
     * Lista programaciones activas paginadas con filtro opcional state.
     * Responses:
     *  - 200: Listado obtenido
     *
     * @param state  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("nextRunAt,ASC"))
     * @return [PageResponseScheduleResponse]
     */
    @GET("api/v1/schedule")
    suspend fun listSchedules(@Query("state") state: ScheduleState? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("nextRunAt,ASC")): Response<PageResponseScheduleResponse>

    /**
     * POST api/v1/schedule/{id}/pause
     * Pausar programacion
     * Pausa una programacion.
     * Responses:
     *  - 204: Pausada
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @return [Unit]
     */
    @POST("api/v1/schedule/{id}/pause")
    suspend fun pauseSchedule(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST api/v1/schedule/{id}/resume
     * Reanudar programacion
     * Reanuda una programacion pausada.
     * Responses:
     *  - 204: Reanudada
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @return [Unit]
     */
    @POST("api/v1/schedule/{id}/resume")
    suspend fun resumeSchedule(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * PUT api/v1/schedule/{id}
     * Actualizar programacion
     * Actualiza una programacion existente.
     * Responses:
     *  - 200: Actualizada
     *  - 400: Datos invalidos
     *  - 404: No encontrada
     *
     * @param id ID de la programacion
     * @param scheduleRequest 
     * @return [ScheduleResponse]
     */
    @PUT("api/v1/schedule/{id}")
    suspend fun updateSchedule(@Path("id") id: kotlin.Long, @Body scheduleRequest: ScheduleRequest): Response<ScheduleResponse>

}

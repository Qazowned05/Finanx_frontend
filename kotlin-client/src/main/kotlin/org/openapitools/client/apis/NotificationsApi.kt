package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.NotificationDTO
import org.openapitools.client.models.NotificationState
import org.openapitools.client.models.PageResponseNotificationDTO

interface NotificationsApi {
    /**
     * POST api/v1/notifications/discard
     * Descartar notificacion
     * Marca una notificacion como descartada.
     * Responses:
     *  - 204: Descartada
     *  - 404: No encontrada
     *
     * @param body 
     * @return [Unit]
     */
    @POST("api/v1/notifications/discard")
    suspend fun discardNotification(@Body body: kotlin.Long): Response<Unit>

    /**
     * GET api/v1/notifications/{id}
     * Buscar notificacion
     * Obtiene una notificacion por ID.
     * Responses:
     *  - 200: Encontrada
     *  - 404: No encontrada
     *
     * @param id ID notificacion
     * @return [NotificationDTO]
     */
    @GET("api/v1/notifications/{id}")
    suspend fun findNotificationById(@Path("id") id: kotlin.Long): Response<NotificationDTO>

    /**
     * GET api/v1/notifications
     * Listar notificaciones del usuario
     * Lista paginada de notificaciones del usuario autenticado con filtros opcionales state y search.
     * Responses:
     *  - 200: Listado obtenido
     *  - 404: Usuario no encontrado
     *
     * @param state  (optional)
     * @param search  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("createdAt,DESC"))
     * @return [PageResponseNotificationDTO]
     */
    @GET("api/v1/notifications")
    suspend fun listNotifications(@Query("state") state: NotificationState? = null, @Query("search") search: kotlin.String? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("createdAt,DESC")): Response<PageResponseNotificationDTO>

    /**
     * POST api/v1/notifications/mark-as-read
     * Marcar como leida
     * Marca una notificacion como leida.
     * Responses:
     *  - 204: Actualizada
     *  - 404: No encontrada
     *
     * @param body 
     * @return [Unit]
     */
    @POST("api/v1/notifications/mark-as-read")
    suspend fun markAsRead(@Body body: kotlin.Long): Response<Unit>

    /**
     * POST api/v1/notifications/purge-older-than
     * Eliminar notificaciones antiguas
     * Elimina notificaciones anteriores a una fecha.
     * Responses:
     *  - 204: Eliminadas
     *
     * @param dateTime Fecha limite
     * @return [Unit]
     */
    @POST("api/v1/notifications/purge-older-than")
    suspend fun purgeNotifications(@Query("dateTime") dateTime: java.time.LocalDateTime): Response<Unit>

}

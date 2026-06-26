package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.NotificationDTO

interface NotificationsApi {
    /**
     * POST notifications/discard
     * Descartar notificacion
     * Marca una notificacion como descartada.
     * Responses:
     *  - 204: Descartada
     *  - 404: No encontrada
     *
     * @param body 
     * @return [Unit]
     */
    @POST("notifications/discard")
    suspend fun discardNotification(@Body body: kotlin.Long): Response<Unit>

    /**
     * GET notifications/user/{idUser}
     * Listar notificaciones por usuario
     * Obtiene todas las notificaciones de un usuario.
     * Responses:
     *  - 200: Listado obtenido
     *  - 404: Usuario no encontrado
     *
     * @param idUser ID usuario
     * @return [kotlin.collections.List<NotificationDTO>]
     */
    @GET("notifications/user/{idUser}")
    suspend fun findAllNotificationByUser(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<NotificationDTO>>


    /**
    * enum for parameter state
    */
    enum class StateFindAllNotificationsByUserAndState(val value: kotlin.String) {
        @Json(name = "SCHEDULED") SCHEDULED("SCHEDULED"),
        @Json(name = "SENT") SENT("SENT"),
        @Json(name = "READ") READ("READ"),
        @Json(name = "DISCARD") DISCARD("DISCARD")
    }

    /**
     * GET notifications/user/{idUser}/{state}
     * Listar por estado
     * Obtiene notificaciones filtradas por estado.
     * Responses:
     *  - 200: Listado obtenido
     *
     * @param idUser ID usuario
     * @param state Estado
     * @return [kotlin.collections.List<NotificationDTO>]
     */
    @GET("notifications/user/{idUser}/{state}")
    suspend fun findAllNotificationsByUserAndState(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<NotificationDTO>>

    /**
     * GET notifications/{id}
     * Buscar notificacion
     * Obtiene una notificacion por ID.
     * Responses:
     *  - 200: Encontrada
     *  - 404: No encontrada
     *
     * @param id ID notificacion
     * @return [NotificationDTO]
     */
    @GET("notifications/{id}")
    suspend fun findNotificationById(@Path("id") id: kotlin.Long): Response<NotificationDTO>

    /**
     * POST notifications/mark-as-read
     * Marcar como leida
     * Marca una notificacion como leida.
     * Responses:
     *  - 204: Actualizada
     *  - 404: No encontrada
     *
     * @param body 
     * @return [Unit]
     */
    @POST("notifications/mark-as-read")
    suspend fun markAsRead(@Body body: kotlin.Long): Response<Unit>

    /**
     * POST notifications/user/{userId}/purge-older-than
     * Eliminar notificaciones antiguas
     * Elimina notificaciones anteriores a una fecha.
     * Responses:
     *  - 204: Eliminadas
     *
     * @param userId ID usuario
     * @param dateTime Fecha limite
     * @return [Unit]
     */
    @POST("notifications/user/{userId}/purge-older-than")
    suspend fun purgeNotifications(@Path("userId") userId: kotlin.Long, @Query("dateTime") dateTime: java.time.LocalDateTime): Response<Unit>

}

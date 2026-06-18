package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.NotificationDTO

interface NotificationControllerApi {
    /**
     * POST notifications/{id}/disable
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("notifications/{id}/disable")
    suspend fun disableNotification(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST notifications/discard
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param body 
     * @return [Unit]
     */
    @POST("notifications/discard")
    suspend fun discardNotification(@Body body: kotlin.Long): Response<Unit>

    /**
     * GET notifications/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [NotificationDTO]
     */
    @GET("notifications/{id}")
    suspend fun findNotificationById(@Path("id") id: kotlin.Long): Response<NotificationDTO>

    /**
     * GET notifications/user/{idUser}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<NotificationDTO>]
     */
    @GET("notifications/user/{idUser}")
    suspend fun findNotificationsByUser(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<NotificationDTO>>


    /**
    * enum for parameter state
    */
    enum class StateFindNotificationsByUserAndState(val value: kotlin.String) {
        @Json(name = "SCHEDULED") SCHEDULED("SCHEDULED"),
        @Json(name = "SENT") SENT("SENT"),
        @Json(name = "READ") READ("READ"),
        @Json(name = "DISCARD") DISCARD("DISCARD")
    }

    /**
     * GET notifications/user/{idUser}/{state}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @param state 
     * @return [kotlin.collections.List<NotificationDTO>]
     */
    @GET("notifications/user/{idUser}/{state}")
    suspend fun findNotificationsByUserAndState(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<NotificationDTO>>

    /**
     * GET notifications/user/{idUser}/scheduled
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<NotificationDTO>]
     */
    @GET("notifications/user/{idUser}/scheduled")
    suspend fun findScheduledNotificationsByUser(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<NotificationDTO>>

    /**
     * POST notifications/{id}/read
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST notifications/mark-as-read
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param body 
     * @return [Unit]
     */
    @POST("notifications/mark-as-read")
    suspend fun markAsRead(@Body body: kotlin.Long): Response<Unit>

    /**
     * POST notifications/user/{userId}/purge-older-than
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param dateTime 
     * @return [Unit]
     */
    @POST("notifications/user/{userId}/purge-older-than")
    suspend fun purge(@Path("userId") userId: kotlin.Long, @Query("dateTime") dateTime: java.time.LocalDateTime): Response<Unit>

}

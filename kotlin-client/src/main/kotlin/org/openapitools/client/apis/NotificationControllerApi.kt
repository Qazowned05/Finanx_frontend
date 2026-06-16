package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.NotificationDTO

interface NotificationControllerApi {
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
    suspend fun discard(@Body body: kotlin.Long): Response<Unit>

    /**
     * GET notifications/{idUser}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<NotificationDTO>]
     */
    @GET("notifications/user/{idUser}")
    suspend fun findAllByUser1(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<NotificationDTO>>


    /**
    * enum for parameter state
    */
    enum class StateFindAllByUserAndState3(val value: kotlin.String) {
        @Json(name = "SCHEDULED") SCHEDULED("SCHEDULED"),
        @Json(name = "SENT") SENT("SENT"),
        @Json(name = "READ") READ("READ"),
        @Json(name = "DISCARD") DISCARD("DISCARD")
    }

    /**
     * GET notifications/{idUser}/{state}
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
    suspend fun findAllByUserAndState3(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<NotificationDTO>>

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
    suspend fun findById6(@Path("id") id: kotlin.Long): Response<NotificationDTO>

}

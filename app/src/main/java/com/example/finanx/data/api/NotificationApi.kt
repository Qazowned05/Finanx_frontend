package com.example.finanx.data.api

import com.example.finanx.data.model.NotificationDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApi {

    // Obtiene todas las notificaciones de un usuario.
    // Backend: GET /notifications/user/{idUser}
    @GET("notifications/user/{idUser}")
    suspend fun findAllByUser(
        @Path("idUser") idUser: Long
    ): List<NotificationDTO>

    // Obtiene una notificación específica por ID.
    // Backend: GET /notifications/{id}
    @GET("notifications/{id}")
    suspend fun findById(
        @Path("id") id: Long
    ): NotificationDTO

    // Obtiene notificaciones de un usuario filtradas por estado.
    // Backend: GET /notifications/user/{idUser}/{state}
    @GET("notifications/user/{idUser}/{state}")
    suspend fun findAllByUserAndState(
        @Path("idUser") idUser: Long,
        @Path("state") state: String
    ): List<NotificationDTO>

    // Descarta una notificación.
    // Backend: POST /notifications/discard
    @POST("notifications/discard")
    suspend fun discard(
        @Body idNotification: Long
    ): Response<Unit>
}

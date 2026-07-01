package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.ChatHistoryResponse
import org.openapitools.client.models.ChatSessionResponse
import org.openapitools.client.models.SendMessageRequest

interface ChatBotApi {
    /**
     * PATCH users/{userId}/chat/sessions/{sessionId}/close
     * Cerrar sesion de chat
     * Marca la sesion como cerrada. No se podran enviar mas mensajes en ella.
     * Responses:
     *  - 200: Sesion cerrada exitosamente
     *  - 404: Sesion no encontrada
     *
     * @param userId ID del usuario
     * @param sessionId ID de la sesion
     * @return [ChatSessionResponse]
     */
    @PATCH("users/{userId}/chat/sessions/{sessionId}/close")
    suspend fun closeSession(@Path("userId") userId: kotlin.Long, @Path("sessionId") sessionId: kotlin.Long): Response<ChatSessionResponse>

    /**
     * GET users/{userId}/chat/sessions
     * Listar sesiones del usuario
     * Retorna todas las sesiones de chat del usuario.
     * Responses:
     *  - 200: Lista obtenida exitosamente
     *  - 404: Usuario no encontrado
     *
     * @param userId ID del usuario
     * @return [kotlin.collections.List<ChatSessionResponse>]
     */
    @GET("users/{userId}/chat/sessions")
    suspend fun findAllSessionsByUser(@Path("userId") userId: kotlin.Long): Response<kotlin.collections.List<ChatSessionResponse>>

    /**
     * GET users/{userId}/chat/sessions/{sessionId}
     * Obtener sesion por ID
     * Retorna el detalle de una sesion incluyendo su historial de mensajes.
     * Responses:
     *  - 200: Sesion encontrada
     *  - 404: Sesion no encontrada
     *
     * @param userId ID del usuario
     * @param sessionId ID de la sesion
     * @return [ChatSessionResponse]
     */
    @GET("users/{userId}/chat/sessions/{sessionId}")
    suspend fun findSessionById(@Path("userId") userId: kotlin.Long, @Path("sessionId") sessionId: kotlin.Long): Response<ChatSessionResponse>

    /**
     * GET users/{userId}/chat/sessions/uuid/{uuid}
     * Obtener sesion por UUID
     * Retorna el detalle de una sesion usando su UUID publico.
     * Responses:
     *  - 200: Sesion encontrada
     *  - 404: Sesion no encontrada
     *
     * @param userId ID del usuario
     * @param uuid UUID de la sesion
     * @return [ChatSessionResponse]
     */
    @GET("users/{userId}/chat/sessions/uuid/{uuid}")
    suspend fun findSessionByUUID(@Path("userId") userId: kotlin.Long, @Path("uuid") uuid: kotlin.String): Response<ChatSessionResponse>

    /**
     * POST users/{userId}/chat/sessions/{sessionId}/messages
     * Enviar mensaje al chatbot
     * Envia un mensaje dentro de una sesion activa. El chatbot detecta automaticamente la skill apropiada (ADVISOR para consultas financieras, CHITCHAT para conversacion general) e inyecta el contexto financiero cuando corresponde. Retorna la respuesta del asistente lista para mostrar al usuario. 
     * Responses:
     *  - 200: Mensaje procesado y respuesta generada
     *  - 400: Mensaje vacio o invalido
     *  - 404: Sesion no encontrada
     *  - 409: La sesion no esta activa
     *
     * @param userId ID del usuario
     * @param sessionId ID de la sesion activa
     * @param sendMessageRequest 
     * @return [ChatHistoryResponse]
     */
    @POST("users/{userId}/chat/sessions/{sessionId}/messages")
    suspend fun sendMessage(@Path("userId") userId: kotlin.Long, @Path("sessionId") sessionId: kotlin.Long, @Body sendMessageRequest: SendMessageRequest): Response<ChatHistoryResponse>

    /**
     * POST users/{userId}/chat/sessions
     * Iniciar sesion de chat
     * Crea una nueva sesion de chat e inyecta el contexto financiero del periodo actual y anterior.
     * Responses:
     *  - 200: Sesion creada exitosamente
     *  - 404: Usuario no encontrado
     *
     * @param userId ID del usuario
     * @return [ChatSessionResponse]
     */
    @POST("users/{userId}/chat/sessions")
    suspend fun startSession(@Path("userId") userId: kotlin.Long): Response<ChatSessionResponse>

}

package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.ChatHistoryResponse
import org.openapitools.client.models.ChatSessionResponse
import org.openapitools.client.models.PageResponseChatSessionResponse
import org.openapitools.client.models.SendMessageRequest

interface ChatBotApi {
    /**
     * PATCH api/v1/chat/sessions/{sessionId}/close
     * Cerrar sesion de chat
     * Marca la sesion como cerrada. No se podran enviar mas mensajes en ella.
     * Responses:
     *  - 200: Sesion cerrada exitosamente
     *  - 404: Sesion no encontrada
     *
     * @param sessionId ID de la sesion
     * @return [ChatSessionResponse]
     */
    @PATCH("api/v1/chat/sessions/{sessionId}/close")
    suspend fun closeSession(@Path("sessionId") sessionId: kotlin.Long): Response<ChatSessionResponse>

    /**
     * GET api/v1/chat/sessions/{sessionId}
     * Obtener sesion por ID
     * Retorna el detalle de una sesion incluyendo su historial de mensajes.
     * Responses:
     *  - 200: Sesion encontrada
     *  - 404: Sesion no encontrada
     *
     * @param sessionId ID de la sesion
     * @return [ChatSessionResponse]
     */
    @GET("api/v1/chat/sessions/{sessionId}")
    suspend fun findSessionById(@Path("sessionId") sessionId: kotlin.Long): Response<ChatSessionResponse>

    /**
     * GET api/v1/chat/sessions/uuid/{uuid}
     * Obtener sesion por UUID
     * Retorna el detalle de una sesion usando su UUID publico.
     * Responses:
     *  - 200: Sesion encontrada
     *  - 404: Sesion no encontrada
     *
     * @param uuid UUID de la sesion
     * @return [ChatSessionResponse]
     */
    @GET("api/v1/chat/sessions/uuid/{uuid}")
    suspend fun findSessionByUUID(@Path("uuid") uuid: kotlin.String): Response<ChatSessionResponse>

    /**
     * GET api/v1/chat/sessions
     * Listar sesiones del usuario
     * Retorna sesiones de chat paginadas; admite page, size y sort estándar.
     * Responses:
     *  - 200: Lista obtenida exitosamente
     *  - 404: Usuario no encontrado
     *
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("createdAt,DESC"))
     * @return [PageResponseChatSessionResponse]
     */
    @GET("api/v1/chat/sessions")
    suspend fun listChatSessions(@Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("createdAt,DESC")): Response<PageResponseChatSessionResponse>

    /**
     * POST api/v1/chat/sessions/{sessionId}/messages
     * Enviar mensaje al chatbot
     * Envia un mensaje dentro de una sesion activa. El chatbot detecta automaticamente la skill apropiada (ADVISOR para consultas financieras, CHITCHAT para conversacion general) e inyecta el contexto financiero cuando corresponde. Retorna la respuesta del asistente lista para mostrar al usuario. 
     * Responses:
     *  - 200: Mensaje procesado y respuesta generada
     *  - 400: Mensaje vacio o invalido
     *  - 404: Sesion no encontrada
     *  - 409: La sesion no esta activa
     *
     * @param sessionId ID de la sesion activa
     * @param sendMessageRequest 
     * @return [ChatHistoryResponse]
     */
    @POST("api/v1/chat/sessions/{sessionId}/messages")
    suspend fun sendMessage(@Path("sessionId") sessionId: kotlin.Long, @Body sendMessageRequest: SendMessageRequest): Response<ChatHistoryResponse>

    /**
     * POST api/v1/chat/sessions
     * Iniciar sesion de chat
     * Crea una nueva sesion de chat e inyecta el contexto financiero del periodo actual y anterior.
     * Responses:
     *  - 200: Sesion creada exitosamente
     *  - 404: Usuario no encontrado
     *
     * @return [ChatSessionResponse]
     */
    @POST("api/v1/chat/sessions")
    suspend fun startSession(): Response<ChatSessionResponse>

}

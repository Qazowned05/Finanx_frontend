package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ChatBotApi
import org.openapitools.client.models.ChatHistoryResponse
import org.openapitools.client.models.ChatSessionResponse
import org.openapitools.client.models.PageResponseChatSessionResponse
import org.openapitools.client.models.SendMessageRequest
import retrofit2.Response
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val api: ChatBotApi
) {
    suspend fun startSession(): Response<ChatSessionResponse> =
        withContext(Dispatchers.IO) { api.startSession() }

    suspend fun closeSession(sessionId: Long): Response<ChatSessionResponse> =
        withContext(Dispatchers.IO) { api.closeSession(sessionId) }

    suspend fun sendMessage(
        userId: Long,
        sessionId: Long,
        message: String
    ): Response<ChatHistoryResponse> =
        withContext(Dispatchers.IO) {
            api.sendMessage(sessionId, SendMessageRequest(message))
        }

    suspend fun list(): Response<PageResponseChatSessionResponse> =
        withContext(Dispatchers.IO) { api.listChatSessions() }

    suspend fun listChatSessionPage(
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE
    ): Response<PageResponseChatSessionResponse> =
        withContext(Dispatchers.IO) {
            api.listChatSessions(page = page, size = size)
        }

    suspend fun findSessionById(sessionId: Long): Response<ChatSessionResponse> =
        withContext(Dispatchers.IO) { api.findSessionById(sessionId) }
}
package com.example.finanx.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ChatBotApi
import org.openapitools.client.models.ChatHistoryResponse
import org.openapitools.client.models.ChatSessionResponse
import org.openapitools.client.models.SendMessageRequest
import retrofit2.Response
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val api: ChatBotApi
) {
    suspend fun startSession(userId: Long): Response<ChatSessionResponse> =
        withContext(Dispatchers.IO) { api.startSession(userId) }

    suspend fun closeSession(userId: Long, sessionId: Long): Response<ChatSessionResponse> =
        withContext(Dispatchers.IO) { api.closeSession(userId, sessionId) }

    suspend fun sendMessage(
        userId: Long,
        sessionId: Long,
        message: String
    ): Response<ChatHistoryResponse> =
        withContext(Dispatchers.IO) {
            api.sendMessage(userId, sessionId, SendMessageRequest(message))
        }

    suspend fun findAllSessions(userId: Long): Response<List<ChatSessionResponse>> =
        withContext(Dispatchers.IO) { api.findAllSessionsByUser(userId) }

    suspend fun findSessionById(userId: Long, sessionId: Long): Response<ChatSessionResponse> =
        withContext(Dispatchers.IO) { api.findSessionById(userId, sessionId) }
}
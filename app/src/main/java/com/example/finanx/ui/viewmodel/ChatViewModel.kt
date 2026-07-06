package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.ChatHistoryResponse
import org.openapitools.client.models.ChatSessionResponse
import javax.inject.Inject

data class ChatUiState(
    // Sesión activa
    val isStartingSession: Boolean = false,
    val activeSession: ChatSessionResponse? = null,
    val messages: List<ChatHistoryResponse> = emptyList(),
    val inputText: String = "",
    val isSending: Boolean = false,

    // Lista de sesiones pasadas
    val sessions: List<ChatSessionResponse> = emptyList(),
    val isLoadingSessions: Boolean = false,

    // Sesión en modo lectura (historial de sesión cerrada)
    val selectedSession: ChatSessionResponse? = null,

    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {


    var state by mutableStateOf(ChatUiState())
        private set

    // ─── Sesión activa ────────────────────────────────────────────────────────

    fun startSession(userId: Long) {
        viewModelScope.launch {
            state = state.copy(isStartingSession = true, error = null)
            try {
                val response = repository.startSession(userId)
                if (response.isSuccessful) {
                    val session = response.body()!!
                    state = state.copy(
                        isStartingSession = false,
                        activeSession = session,
                        // La sesión nueva llega con history vacío.
                        // Si en el futuro el backend devuelve historial previo
                        // (sesión reanudada), esto lo cargará automáticamente.
                        messages = session.history.orEmpty()
                    )
                } else {
                    state = state.copy(
                        isStartingSession = false,
                        error = "Error ${response.code()}: no se pudo iniciar la sesión."
                    )
                }
            } catch (e: Exception) {
                state = state.copy(
                    isStartingSession = false,
                    error = "Error de red: ${e.message}"
                )
            }
        }
    }

    fun closeSession(userId: Long) {
        val sessionId = state.activeSession?.id ?: return
        viewModelScope.launch {
            try {
                repository.closeSession(userId, sessionId)
            } catch (e: Exception) {
                // Silencioso — el estado local se limpia de todas formas
            } finally {
                state = state.copy(
                    activeSession = null,
                    messages = emptyList(),
                    inputText = ""
                )
                // Recarga la lista de sesiones para mostrar la que acaba de cerrarse
                loadSessions(userId)
            }
        }
    }

    fun onInputChange(text: String) {
        state = state.copy(inputText = text)
    }

    fun sendMessage(userId: Long) {
        val sessionId = state.activeSession?.id ?: return
        val text = state.inputText.trim().ifEmpty { return }

        viewModelScope.launch {
            // Optimistic UI: agrega el mensaje del usuario inmediatamente
            val userMsg = ChatHistoryResponse(
                role = ChatHistoryResponse.Role.USER,
                message = text,
                turnNumber = state.messages.size + 1
            )
            state = state.copy(
                messages = state.messages + userMsg,
                inputText = "",
                isSending = true,
                error = null
            )

            try {
                val response = repository.sendMessage(userId, sessionId, text)
                if (response.isSuccessful) {
                    val assistantMsg = response.body()!!
                    state = state.copy(
                        messages = state.messages + assistantMsg,
                        isSending = false
                    )
                } else {
                    state = state.copy(
                        isSending = false,
                        error = "Error ${response.code()}: no se pudo enviar el mensaje."
                    )
                }
            } catch (e: Exception) {
                state = state.copy(
                    isSending = false,
                    error = "Error de red: ${e.message}"
                )
            }
        }
    }

    // ─── Lista de sesiones pasadas ────────────────────────────────────────────

    fun loadSessions(userId: Long) {
        viewModelScope.launch {
            state = state.copy(isLoadingSessions = true, error = null)
            try {
                val response = repository.findAllSessions(userId)
                if (response.isSuccessful) {
                    state = state.copy(
                        sessions = response.body().orEmpty(),
                        isLoadingSessions = false
                    )
                } else {
                    state = state.copy(
                        isLoadingSessions = false,
                        error = "Error ${response.code()}: no se pudieron cargar las sesiones."
                    )
                }
            } catch (e: Exception) {
                state = state.copy(
                    isLoadingSessions = false,
                    error = "Error de red: ${e.message}"
                )
            }
        }
    }

    fun openSession(session: ChatSessionResponse) {
        state = state.copy(selectedSession = session)
    }

    fun closeSelectedSession() {
        state = state.copy(selectedSession = null)
    }
}
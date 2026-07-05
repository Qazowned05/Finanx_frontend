package com.example.finanx.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.AppBarState
import com.example.finanx.ui.components.SpotlightOverlay
import com.example.finanx.ui.components.TutorialPreferences
import com.example.finanx.ui.components.tutorial.TutorialController
import com.example.finanx.ui.components.tutorial.TutorialStep
import com.example.finanx.ui.components.tutorialTarget
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.viewmodel.ChatUiState
import kotlinx.coroutines.launch
import org.openapitools.client.models.ChatHistoryResponse
import org.openapitools.client.models.ChatSessionResponse
import java.time.format.DateTimeFormatter
import com.example.finanx.ui.viewmodel.ChatViewModel
import kotlinx.coroutines.flow.first

/**
 * Pantalla principal del chatbot Finn.
 * Estados:
 * - Sin sesión activa → muestra historial de sesiones pasadas + botón nueva sesión
 * - Con sesión activa → chat multi-turno en tiempo real
 * - Sesión seleccionada (cerrada) → historial read-only
 */
@Composable
fun ChatScreen(
    userId: Long,
    viewModel: ChatViewModel,
    onAppBarState: (AppBarState) -> Unit
) {
    val state = viewModel.state
    val context = androidx.compose.ui.platform.LocalContext.current
    val targetBounds = remember { mutableStateMapOf<String, Rect>() }
    val coroutineScope = rememberCoroutineScope()
    var tutorialController by remember { mutableStateOf<TutorialController?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadSessions(userId)
    }

    LaunchedEffect(Unit) {
        val completed = TutorialPreferences.isCompleted(context, "tutorial_finn").first()
        if (!completed) {
            tutorialController = TutorialController(
                listOf(
                    TutorialStep("finn_icon", "Finn es tu asistente financiero — pregúntale sobre tus gastos o pide consejos."),
                    TutorialStep("new_session_button", "Cada conversación es independiente; puedes revisarlas después en el historial."),
                    TutorialStep("chat_input_field", "Escríbele como lo harías con una persona.")
                )
            ) {
                coroutineScope.launch { TutorialPreferences.markCompleted(context, "tutorial_finn") }
                tutorialController = null
            }
        }
    }

    LaunchedEffect(state.selectedSession, state.activeSession) {
        onAppBarState(
            when {
                state.selectedSession != null -> AppBarState(
                    title = "Conversación anterior",
                    subtitle = sessionStateLabel(state.selectedSession.state)
                )
                state.activeSession != null -> AppBarState(
                    title = "Finn",
                    subtitle = "Sesión activa"
                )
                else -> AppBarState(title = "Finn", subtitle = "Asistente financiero")
            }
        )
    }

    if (state.selectedSession != null) {
        SessionHistoryView(session = state.selectedSession)
        return
    }

    if (state.activeSession != null) {
        ActiveChatView(
            state = state,
            onInputChange = viewModel::onInputChange,
            onSend = { viewModel.sendMessage(userId) },
            onClose = { viewModel.closeSession(userId) },
            inputModifier = Modifier.tutorialTarget("chat_input_field", targetBounds)
        )
    } else {
        SessionListView(
            state = state,
            onNewSession = { viewModel.startSession(userId) },
            onOpenSession = viewModel::openSession,
            newSessionModifier = Modifier.tutorialTarget("new_session_button", targetBounds),
            finnIconModifier = Modifier.tutorialTarget("finn_icon", targetBounds)
        )
    }

    tutorialController?.let { controller ->
        val current = controller.currentStep
        SpotlightOverlay(
            targetBounds = current?.let { targetBounds[it.targetKey] },
            message = current?.message.orEmpty(),
            isLastStep = controller.isLastStep,
            onNext = { controller.next() },
            onSkip = {
                coroutineScope.launch {
                    TutorialPreferences.markCompleted(context, "tutorial_finn")
                }
                tutorialController = null
            }
        )
    }
}

@Composable
private fun ActiveChatView(
    state: ChatUiState,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onClose: () -> Unit,
    inputModifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(state.messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onClose) {
                Text(
                    text = "Cerrar sesión",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(state.messages) { msg ->
                ChatBubble(message = msg)
            }

            if (state.isSending) {
                item {
                    TypingIndicator()
                }
            }
        }

        AnimatedVisibility(
            visible = state.error != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            state.error?.let {
                Text(
                    text = it,
                    color = ExpenseRed,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        ChatInputBar(
            text = state.inputText,
            isSending = state.isSending,
            onTextChange = onInputChange,
            onSend = onSend,
            modifier = inputModifier
        )
    }
}

@Composable
fun ChatBubble(message: ChatHistoryResponse) {
    val isUser = message.role == ChatHistoryResponse.Role.USER
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(BrownPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = if (isUser) 16.dp else 4.dp,
                topEnd = if (isUser) 4.dp else 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = if (isUser) BrownPrimary else SurfaceWhite,
            shadowElevation = 1.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.message ?: "",
                color = if (isUser) TextLight else TextDark,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(BrownPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(Modifier.width(6.dp))
        Surface(
            shape = RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp),
            color = SurfaceWhite
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(14.dp),
                strokeWidth = 2.dp
            )
        }
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    isSending: Boolean,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = 8.dp,
        color = SurfaceWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        "Escribe tu mensaje…",
                        color = TextGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                modifier = modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() }),
                enabled = !isSending,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = BorderSoft
                )
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank() && !isSending,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (text.isNotBlank() && !isSending) BrownPrimary
                        else BorderSoft,
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = if (text.isNotBlank() && !isSending) MaterialTheme.colorScheme.tertiary else TextGray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}


@Composable
fun SessionStateBadge(state: ChatSessionResponse.State?) {
    val (label, color) = when (state) {
        ChatSessionResponse.State.ACTIVE -> "Activa" to IncomeGreen
        ChatSessionResponse.State.CLOSED -> "Cerrada" to TextGray
        ChatSessionResponse.State.EXPIRED -> "Expirada" to ExpenseRed
        null -> "—" to TextGray
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

fun sessionStateLabel(state: ChatSessionResponse.State?): String =
    when (state) {
        ChatSessionResponse.State.ACTIVE -> "Sesión activa"
        ChatSessionResponse.State.CLOSED -> "Sesión cerrada"
        ChatSessionResponse.State.EXPIRED -> "Sesión expirada"
        null -> ""
    }

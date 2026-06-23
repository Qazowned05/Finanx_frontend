package com.example.finanx.ui.screens.analytics.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.BrownSecondary
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.theme.SurfaceWhite
import com.example.finanx.ui.theme.TextDark
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.theme.TextLight
import com.example.finanx.ui.theme.WarmBackground

private val PROMPT_SUGGESTIONS = listOf(
    "¿En qué categoría gasto más y cómo puedo reducirlo?",
    "Analiza mis tendencias de gasto en los últimos días.",
    "¿Hay alguna anomalía inusual en mis transacciones?",
    "Dame proyecciones de mis gastos para el próximo mes.",
    "¿Qué hábitos financieros debo cambiar urgentemente?",
    "Compara mis ingresos vs. gastos y dame un diagnóstico.",
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatBotPromptDialog(
    prompt: String,
    isLoading: Boolean,
    onPromptChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onSkip: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .imePadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = SurfaceWhite,
                shadowElevation = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(BrownPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Asistente de Análisis",
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "¿Qué quieres que analice?",
                                color = TextGray,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextGray)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Sugerencias",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextGray
                    )
                    Spacer(Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PROMPT_SUGGESTIONS.forEach { suggestion ->
                            SuggestionChip(
                                text = suggestion,
                                onClick = { onPromptChange(suggestion) }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = prompt,
                        onValueChange = onPromptChange,
                        placeholder = {
                            Text(
                                "Escribe tu pregunta o elige una sugerencia…",
                                color = TextGray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = Color(0xFFE3DCD0),
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                        enabled = !isLoading
                    )

                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WarmBackground,
                            border = BorderStroke(1.dp, Color(0xFFE3DCD0)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable(enabled = !isLoading, onClick = onSkip)
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Sin IA",
                                    color = TextGray,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BrownPrimary,
                            modifier = Modifier
                                .weight(2f)
                                .clickable(enabled = !isLoading, onClick = onSubmit)
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = GoldAccent,
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Send,
                                            contentDescription = null,
                                            tint = GoldAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = "Analizar con IA",
                                            color = TextLight,
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionChip(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF5F1EB),
        border = BorderStroke(1.dp, Color(0xFFE3DCD0)),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = BrownSecondary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
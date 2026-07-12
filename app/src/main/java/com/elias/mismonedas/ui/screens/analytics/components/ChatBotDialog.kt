package com.elias.mismonedas.ui.screens.analytics.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val PROMPT_SUGGESTIONS = listOf(
    "¿En qué categoría gasto más y cómo puedo reducirlo?",
    "Analiza mis tendencias de gasto en los últimos días.",
    "¿Hay alguna anomalía inusual en mis transacciones?",
    "Dame proyecciones de mis gastos para el próximo mes.",
    "¿Qué hábitos financieros debo cambiar urgentemente?",
    "Compara mis ingresos vs. gastos y dame un diagnóstico.",
)

@Composable
private fun SuggestionChip(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

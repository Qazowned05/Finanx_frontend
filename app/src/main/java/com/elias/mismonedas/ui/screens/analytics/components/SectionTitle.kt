package com.elias.mismonedas.ui.screens.analytics.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    isDanger: Boolean = false
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = if (isDanger) MaterialTheme.colorScheme.error else Color.Black,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}
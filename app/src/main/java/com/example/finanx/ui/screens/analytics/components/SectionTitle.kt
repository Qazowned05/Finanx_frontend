package com.example.finanx.ui.screens.analytics.components

import com.example.finanx.ui.theme.GoldAccent

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = GoldAccent,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}
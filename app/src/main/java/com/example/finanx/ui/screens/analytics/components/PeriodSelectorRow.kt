package com.example.finanx.ui.screens.analytics.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.theme.BorderSoft
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.theme.SurfaceWhite
import com.example.finanx.ui.theme.TextGray
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val fmt = DateTimeFormatter.ofPattern("dd MMM")

@Composable
fun PeriodSelectorRow(
    start: LocalDate,
    end: LocalDate,
    onQuickSelect: (days: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val chips = listOf(7 to "7d", 30 to "30d", 90 to "90d", 180 to "6m")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(end = 2.dp)
        )
        Text(
            text = "${start.format(fmt)} – ${end.format(fmt)}",
            style = MaterialTheme.typography.bodySmall,
            color = TextGray
        )
        chips.forEach { (days, label) ->
            PeriodChip(label = label, onClick = { onQuickSelect(days) })
        }
    }
}

@Composable
private fun PeriodChip(label: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceWhite,
        border = BorderStroke(1.dp, BorderSoft),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextGray,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
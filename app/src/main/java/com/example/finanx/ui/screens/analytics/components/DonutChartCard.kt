package com.example.finanx.ui.screens.analytics.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.finanx.ui.theme.BorderSoft
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.theme.SurfaceWhite
import com.example.finanx.ui.theme.TextDark
import com.example.finanx.ui.theme.TextGray

data class DonutSlice(val label: String, val share: Float, val color: Color)

@Composable
fun DonutChartCard(
    title: String,
    slices: List<DonutSlice>,
    modifier: Modifier = Modifier
) {
    if (slices.isEmpty() || slices.sumOf { it.share.toDouble() } == 0.0) return

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, BorderSoft)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Donut
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(120.dp)) {
                        var startAngle = -90f
                        val strokeWidth = 28.dp.toPx()
                        slices.forEach { slice ->
                            val sweep = slice.share * 360f
                            drawArc(
                                color = slice.color,
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                            )
                            startAngle += sweep
                        }
                    }
                }

                Spacer(Modifier.width(24.dp))

                // Legend
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    slices.forEach { slice ->
                        LegendRow(slice)
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendRow(slice: DonutSlice) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(10.dp),
            shape = CircleShape,
            color = slice.color
        ) {}
        Spacer(Modifier.width(6.dp))
        Text(
            text = "${slice.label}  ${(slice.share * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = TextDark
        )
    }
}
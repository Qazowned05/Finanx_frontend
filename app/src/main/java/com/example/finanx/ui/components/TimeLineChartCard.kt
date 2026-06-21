package com.example.finanx.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.theme.GrayBorder
import com.example.finanx.ui.theme.TextGray
import kotlin.math.roundToInt

@Composable
fun TimeLineChartCard(
    title: String,
    points: List<Float>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val textMeasurer = rememberTextMeasurer()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (points.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Sin datos suficientes", color = TextGray)
                }
            } else {
                val surfaceColor = MaterialTheme.colorScheme.surface
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .pointerInput(points) {
                            detectTapGestures { tapOffset ->
                                val width = size.width
                                val sectionWidth = width / maxOf(points.size - 1, 1)
                                val index = (tapOffset.x / sectionWidth).roundToInt()
                                if (index in points.indices) {
                                    selectedIndex = index
                                }
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val maxPoint = points.maxOrNull()?.takeIf { it > 0f } ?: 1f

                    repeat(4) { index ->
                        val y = height * (index + 1) / 5
                        drawLine(
                            color = GrayBorder,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    val path = Path()
                    val pointOffsets = mutableListOf<Offset>()

                    points.forEachIndexed { index, value ->
                        val x = width * index / maxOf(points.size - 1, 1)
                        val y = height - (height * (value / maxPoint))
                        pointOffsets.add(Offset(x, y))

                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    pointOffsets.forEachIndexed { index, offset ->
                        val isSelected = index == selectedIndex

                        drawCircle(
                            color = if (isSelected) surfaceColor else lineColor,
                            radius = if (isSelected) 6.dp.toPx() else 3.5.dp.toPx(),
                            center = offset
                        )

                        if (isSelected) {
                            drawCircle(
                                color = lineColor,
                                radius = 6.dp.toPx(),
                                center = offset,
                                style = Stroke(width = 2.dp.toPx())
                            )

                            val valueText = "S/ ${"%.2f".format(points[index])}"
                            val textLayoutResult = textMeasurer.measure(
                                text = valueText,
                                style = TextStyle(
                                    color = lineColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )

                            drawText(
                                textLayoutResult = textLayoutResult,
                                topLeft = Offset(
                                    x = offset.x - (textLayoutResult.size.width / 2),
                                    y = offset.y - 30.dp.toPx()
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
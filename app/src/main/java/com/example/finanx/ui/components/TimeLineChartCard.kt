package com.example.finanx.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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

import kotlin.math.roundToInt

@Composable
fun TimeLineChartCard(
    title: String,
    points: List<Float>,
    pointLabels: List<String> = emptyList(),
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
                    Text(text = "Sin datos suficientes", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                val surfaceColor = MaterialTheme.colorScheme.surface
                val tooltipContainerColor = MaterialTheme.colorScheme.inverseSurface
                val tooltipTextColor = MaterialTheme.colorScheme.inverseOnSurface
                val gridColor = MaterialTheme.colorScheme.outlineVariant
                val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .pointerInput(points) {
                            detectTapGestures { tapOffset ->
                                val width = size.width
                                val index = if (points.size == 1) {
                                    0
                                } else {
                                    val sectionWidth = width / points.lastIndex
                                    (tapOffset.x / sectionWidth).roundToInt()
                                }
                                if (index in points.indices) {
                                    selectedIndex = index
                                }
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val labelHeight = if (pointLabels.isNotEmpty()) {
                        22.dp.toPx()
                    } else {
                        0f
                    }
                    val plotHeight = height - labelHeight
                    val maxPoint = points.maxOrNull()?.takeIf { it > 0f } ?: 1f

                    repeat(4) { index ->
                        val y = plotHeight * (index + 1) / 5
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    val path = Path()
                    val pointOffsets = mutableListOf<Offset>()

                    points.forEachIndexed { index, value ->
                        val x = if (points.size == 1) {
                            width / 2f
                        } else {
                            width * index / points.lastIndex
                        }
                        val y = plotHeight - (plotHeight * (value / maxPoint))
                        pointOffsets.add(Offset(x, y))

                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    if (points.size > 1) {
                        drawPath(
                            path = path,
                            color = lineColor,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }

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

                            val label = pointLabels.getOrNull(index)
                            val valueText = if (label == null) {
                                "S/ ${"%.2f".format(points[index])}"
                            } else {
                                "$label  S/ ${"%.2f".format(points[index])}"
                            }
                            val textLayoutResult = textMeasurer.measure(
                                text = valueText,
                                style = TextStyle(
                                    color = tooltipTextColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )

                            val tooltipHorizontalPadding = 8.dp.toPx()
                            val tooltipVerticalPadding = 5.dp.toPx()
                            val tooltipWidth =
                                textLayoutResult.size.width +
                                        tooltipHorizontalPadding * 2
                            val tooltipHeight =
                                textLayoutResult.size.height +
                                        tooltipVerticalPadding * 2
                            val tooltipX =
                                (offset.x - tooltipWidth / 2)
                                    .coerceIn(
                                        0f,
                                        maxOf(0f, width - tooltipWidth)
                                    )
                            val tooltipY =
                                (offset.y - tooltipHeight - 12.dp.toPx())
                                    .coerceAtLeast(0f)

                            drawRoundRect(
                                color = tooltipContainerColor,
                                topLeft = Offset(tooltipX, tooltipY),
                                size = Size(tooltipWidth, tooltipHeight),
                                cornerRadius =
                                    CornerRadius(6.dp.toPx(), 6.dp.toPx())
                            )

                            drawText(
                                textLayoutResult = textLayoutResult,
                                topLeft = Offset(
                                    x = tooltipX + tooltipHorizontalPadding,
                                    y = tooltipY + tooltipVerticalPadding
                                )
                            )
                        }
                    }

                    if (pointLabels.isNotEmpty()) {
                        val labelIndexes = if (points.size == 1) {
                            listOf(0)
                        } else {
                            listOf(0, points.lastIndex).distinct()
                        }

                        labelIndexes.forEach { index ->
                            val label = pointLabels.getOrNull(index) ?: return@forEach
                            val offset = pointOffsets.getOrNull(index) ?: return@forEach
                            val textLayoutResult = textMeasurer.measure(
                                text = label,
                                style = TextStyle(
                                    color = labelColor,
                                    fontSize = 11.sp
                                )
                            )

                            drawText(
                                textLayoutResult = textLayoutResult,
                                topLeft = Offset(
                                    x = (offset.x - (textLayoutResult.size.width / 2))
                                        .coerceIn(
                                            0f,
                                            maxOf(0f, width - textLayoutResult.size.width)
                                        ),
                                    y = plotHeight + 6.dp.toPx()
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

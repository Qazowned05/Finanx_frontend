package com.example.finanx.ui.screens.analytics.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import org.openapitools.client.models.TimeLineTransactionResponse

@Composable
fun LineChartCard(
    timeline: TimeLineTransactionResponse,
    lineColor: Color,
    modifier: Modifier = Modifier,
) {
    val points = timeline.points.orEmpty()
    if (points.isEmpty()) return

    val yValues = points.map { it.amount?.toFloat() ?: 0f }

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(yValues) {
        modelProducer.runTransaction {
            lineSeries { series(y = yValues) }
        }
    }

    val bottomFormatter = remember(points) {
        CartesianValueFormatter { _, value, _ ->
            val index = value.toInt()
            points.getOrNull(index)?.date?.dayOfMonth?.toString() ?: ""
        }
    }


    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, BorderSoft),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            if (!timeline.title.isNullOrBlank()) {
                Text(
                    text = timeline.title.toString(),
                    fontWeight = FontWeight.SemiBold,
                    color = GoldAccent,
                    style = MaterialTheme.typography.labelMedium,
                )
                Spacer(Modifier.height(4.dp))
            }

            timeline.aggregate?.let { agg ->
                Text(
                    text = "${agg.occurrenceCount ?: 0} transacciones  ·  " +
                            "Prom. S/ ${"%.2f".format(agg.averageAmount ?: 0.toBigDecimal())}",
                    color = TextGray,
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(Modifier.height(8.dp))
            }

            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer(
                        lineProvider = LineCartesianLayer.LineProvider.series(
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(fill(lineColor)),
                                areaFill = LineCartesianLayer.AreaFill.single(
                                    fill(lineColor.copy(alpha = 0.12f))
                                ),
                            )
                        ),
                    ),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom(
                        valueFormatter = bottomFormatter,
                    ),
                ),
                modelProducer = modelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
            )

        }
    }
}

package com.example.finanx.ui.screens.analytics.components

import com.example.finanx.ui.theme.BorderSoft
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.SurfaceWhite
import com.example.finanx.ui.theme.TextDark
import com.example.finanx.ui.theme.TextGray

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.openapitools.client.models.Insight
import org.openapitools.client.models.InsightSummary

@Composable
fun InsightSummaryCard(
    insightSummary: InsightSummary,
    aiGenerated: Boolean = false,
    modifier: Modifier = Modifier
) {
    val insights = insightSummary.insights.orEmpty();
    if (insights.isEmpty()) return

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, if (aiGenerated) GoldAccent else BorderSoft)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (aiGenerated) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                }
                Text(
                    text = if (aiGenerated) "Insights de IA" else "Recomendaciones",
                    fontWeight = FontWeight.SemiBold,
                    color = if (aiGenerated) GoldAccent else TextDark,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${insights.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGray
                )
            }

            Spacer(Modifier.height(12.dp))

            insights.forEach { insight ->
                InsightRow(insight = insight)
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun InsightRow(insight: Insight) {
    val (dotColor, bgColor) = when (insight.severity) {
        Insight.Severity.CRITICAL -> ExpenseRed to Color(0xFFFFF0EE)
        Insight.Severity.WARNING  -> Color(0xFFB45309) to Color(0xFFFFFBEB)
        Insight.Severity.INFO     -> IncomeGreen to Color(0xFFF0F7F1)
        null                      -> GoldAccent to Color(0xFFFBF8F2)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(8.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(Modifier.width(10.dp))
        Column {
            if (!insight.title.isNullOrBlank()) {
                Text(
                    text = insight.title.toString(),
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            val body = insight.description ?: insight.action
            if (!body.isNullOrBlank()) {
                Text(
                    text = body,
                    color = TextGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (!insight.action.isNullOrBlank() && insight.description != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "→ ${insight.action}",
                    color = dotColor,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
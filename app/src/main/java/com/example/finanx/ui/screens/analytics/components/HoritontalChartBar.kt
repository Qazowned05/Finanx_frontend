package com.example.finanx.ui.screens.analytics.components

import com.example.finanx.ui.theme.BorderSoft
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.theme.SurfaceWhite
import com.example.finanx.ui.theme.TextDark
import com.example.finanx.ui.theme.TextGray

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

data class BarItem(
    val label: String,
    val value: BigDecimal,
    val share: Float,      // 0..1
    val badge: String? = null
)

@Composable
fun HorizontalBarChartCard(
    title: String,
    items: List<BarItem>,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

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
                color = GoldAccent,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(12.dp))

            items.forEachIndexed { index, item ->
                if (index > 0) Spacer(Modifier.height(10.dp))
                BarRow(item = item, barColor = barColor)
            }
        }
    }
}

@Composable
private fun BarRow(item: BarItem, barColor: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = TextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = money(item.value),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { item.share.coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = barColor,
                trackColor = BorderSoft
            )
            if (item.badge != null) {
                Text(
                    text = item.badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGray,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}

private fun money(value: BigDecimal): String =
    NumberFormat.getCurrencyInstance(Locale("es", "PE")).format(value)
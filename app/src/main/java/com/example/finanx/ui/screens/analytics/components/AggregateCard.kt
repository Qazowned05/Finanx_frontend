package com.example.finanx.ui.screens.analytics.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.openapitools.client.models.Aggregate
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AggregateCard(
    aggregate: Aggregate,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, BorderSoft)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (!aggregate.title.isNullOrBlank()) {
                Text(
                    text = aggregate.title!!,
                    style = MaterialTheme.typography.labelMedium,
                    color = GoldAccent,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AggStat("Total", money(aggregate.totalAmount))
                AggStat("Promedio", money(aggregate.averageAmount))
                AggStat("N°", aggregate.occurrenceCount?.toString() ?: "–")
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AggStat("Máx.", money(aggregate.max))
                AggStat("Mín.", money(aggregate.min))
            }
        }
    }
}

@Composable
private fun AggStat(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextGray)
        Text(value, fontWeight = FontWeight.Bold, color = TextDark,
            style = MaterialTheme.typography.bodySmall)
    }
}

private fun money(value: BigDecimal?): String {
    val fmt = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
    return fmt.format(value ?: BigDecimal.ZERO)
}

package com.example.finanx.ui.screens.budget.recurrent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.StoneGray
import com.example.finanx.ui.theme.SurfaceWhite
import com.example.finanx.ui.util.toReadableDate
import org.openapitools.client.models.BudgetScheduleResponse
import java.math.BigDecimal


@Composable
fun RecurringBudgetCard(bs: BudgetScheduleResponse, onPause: () -> Unit, onResume: () -> Unit, onCancel: () -> Unit) {
    val isPaused = bs.state == org.openapitools.client.models.ScheduleResponse.State.PAUSED
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(bs.description ?: bs.categoryName ?: "Presupuesto recurrente", style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
                Text("S/ ${bs.limitAmount ?: BigDecimal.ZERO}", style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
            }
            Text("Próxima ejecución: ${bs.nextRunAt?.toLocalDate().toReadableDate()}", style = MaterialTheme.typography.labelMedium, color = StoneGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = if (isPaused) onResume else onPause, modifier = Modifier.weight(1f)) {
                    Icon(if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isPaused) "Reanudar" else "Pausar")
                }
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancelar", color = ExpenseRed)
                }
            }
        }
    }
}
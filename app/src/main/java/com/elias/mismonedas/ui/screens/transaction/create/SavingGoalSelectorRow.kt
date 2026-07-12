package com.elias.mismonedas.ui.screens.transaction.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.elias.mismonedas.data.model.SelectOption

@Composable
fun SavingGoalSelectorRow(
    goals: List<SelectOption>,
    selectedGoalId: Long?,
    onGoalSelected: (Long?) -> Unit,
    onCreateGoal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardWidth = 104.dp
    val cardHeight = 120.dp

    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(goals) { goal ->
            val selected = selectedGoalId == goal.id
            val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            val containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
            Card(
                modifier = Modifier
                    .size(width = cardWidth, height = cardHeight)
                    .drawBehind {
                        val strokeWidthPx = if (selected) 2.dp.toPx() else 1.dp.toPx()
                        drawRect(color = borderColor, style = Stroke(width = strokeWidthPx))
                    }
                    .clickable(onClick = {
                        if (selected) onGoalSelected(null) else onGoalSelected(goal.id)
                    }),
                colors = CardDefaults.cardColors(
                    containerColor = containerColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Savings, contentDescription = null, modifier = Modifier.size(36.dp), tint = if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified)
                    Text(
                        goal.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        item {
            val primaryColor = MaterialTheme.colorScheme.primary
            val strokeWidthPx = with(LocalDensity.current) { 2.dp.toPx() }

            Card(
                modifier = Modifier
                    .size(width = cardWidth, height = cardHeight)
                    .clickable(onClick = onCreateGoal)
                    .drawBehind {
                        val stroke = Stroke(width = strokeWidthPx, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f))
                        drawRect(color = primaryColor, style = stroke)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Crear meta", modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
                    Text("Crear meta", textAlign = TextAlign.Center, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

package com.example.finanx.ui.screens.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.screens.saving.goalIcons
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.util.toReadableDate
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetResponse
import java.math.BigDecimal

fun BudgetResponse.State?.toDomain(): BudgetState? = when (this) {
    BudgetResponse.State.ACTIVE -> BudgetState.ACTIVE
    BudgetResponse.State.CANCELLED -> BudgetState.CANCELLED
    BudgetResponse.State.FINALIZED -> BudgetState.FINALIZED
    else -> null
}

fun BudgetExecutionResponse.State?.toDomain(): BudgetState? = when (this) {
    BudgetExecutionResponse.State.ACTIVE -> BudgetState.ACTIVE
    BudgetExecutionResponse.State.CANCELLED -> BudgetState.CANCELLED
    BudgetExecutionResponse.State.FINALIZED -> BudgetState.FINALIZED
    else -> null
}

fun BudgetResponse.Health?.toDomain(): BudgetHealth? = when (this) {
    BudgetResponse.Health.EXCEEDED -> BudgetHealth.EXCEEDED
    BudgetResponse.Health.NEAR_LIMIT -> BudgetHealth.NEAR_LIMIT
    BudgetResponse.Health.OK -> BudgetHealth.OK
    else -> null
}

fun BudgetExecutionResponse.Health?.toDomain(): BudgetHealth? = when (this) {
    BudgetExecutionResponse.Health.EXCEEDED -> BudgetHealth.EXCEEDED
    BudgetExecutionResponse.Health.NEAR_LIMIT -> BudgetHealth.NEAR_LIMIT
    BudgetExecutionResponse.Health.OK -> BudgetHealth.OK
    else -> null
}

enum class BudgetState {
    ACTIVE, CANCELLED, FINALIZED
}

enum class BudgetHealth {
    EXCEEDED, NEAR_LIMIT, OK
}
@Composable
fun BudgetCard(
    budget: BudgetExecutionResponse,
    iconIndex: Int,
    onCancel: () -> Unit,
    onDisable: () -> Unit,
    onEdit: () -> Unit
) {
    BudgetCardContent(
        description = budget.description.orEmpty(),
        categoryName = budget.categoryName.orEmpty(),
        limitAmount = budget.limitAmount ?: BigDecimal.ZERO,
        spentAmount = budget.spentAmount,
        remainingAmount = budget.remainingAmount,
        progressPercentage = budget.executionPercentage,
        state = budget.state.toDomain(),
        health = budget.health.toDomain(),
        createdAt = budget.createdAt?.toReadableDate(),
        iconIndex = iconIndex,
        showProgress = true,
        onCancel = onCancel,
        onDisable = onDisable,
        onEdit = onEdit
    )
}

@Composable
fun BudgetCard(
    budget: BudgetResponse,
    iconIndex: Int,
    onCancel: () -> Unit,
    onDisable: () -> Unit,
    onEdit: () -> Unit
) {
    BudgetCardContent(
        description = budget.description.orEmpty(),
        categoryName = budget.categoryName.orEmpty(),
        limitAmount = budget.limitAmount ?: BigDecimal.ZERO,
        spentAmount = null,
        remainingAmount = null,
        progressPercentage = null,
        state = budget.state.toDomain(),
        health = budget.health.toDomain(),
        createdAt = budget.createdAt?.toReadableDate(),
        iconIndex = iconIndex,
        showProgress = false,
        onCancel = onCancel,
        onDisable = onDisable,
        onEdit = onEdit
    )
}

@Composable
private fun BudgetCardContent(
    description: String,
    categoryName: String,
    limitAmount: BigDecimal,
    spentAmount: BigDecimal?,
    remainingAmount: BigDecimal?,
    progressPercentage: BigDecimal?,
    state: BudgetState?,
    health: BudgetHealth?,
    createdAt: String?,
    iconIndex: Int,
    showProgress: Boolean,
    onCancel: () -> Unit,
    onDisable: () -> Unit,
    onEdit: () -> Unit
) {
    val accentColor = MaterialTheme.colorScheme.primary
    var showOptions by remember { mutableStateOf(false) }

    val healthColor = when (health) {
        BudgetHealth.EXCEEDED -> MaterialTheme.colorScheme.error
        BudgetHealth.NEAR_LIMIT -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.primary
    }

    val healthLabel = when (health) {
        BudgetHealth.EXCEEDED -> "Excedido"
        BudgetHealth.NEAR_LIMIT -> "Por agotar"
        BudgetHealth.OK -> "Equilibrado"
        else -> "—"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = {}, onLongClick = { showOptions = true }),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = goalIcons[iconIndex],
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        description,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(categoryName, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                }
                if (health != null) {
                    Box(
                        modifier = Modifier
                            .background(healthColor.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            healthLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = healthColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Límite: ${limitAmount.toPen()}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                if (spentAmount != null) {
                    Text("Gastado: ${spentAmount.toPen()}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                }
            }

            if (remainingAmount != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Disponible: ${remainingAmount.toPen()}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
            }

            if (showProgress && progressPercentage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                val progress = (progressPercentage.toFloat() / 100f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                    color = healthColor,
                    trackColor = accentColor.copy(alpha = 0.12f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${progressPercentage.toInt()}% ejecutado",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (createdAt != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Creado: $createdAt", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
            }
        }
    }

    if (showOptions) {
        AlertDialog(
            onDismissRequest = { showOptions = false },
            title = { Text("Opciones de presupuesto") },
            text = { Text("Elige una acción para este presupuesto") },
            confirmButton = {
                TextButton(onClick = {
                    onEdit()
                    showOptions = false
                }) { Text("Editar") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = {
                        onDisable()
                        showOptions = false
                    }) { Text("Eliminar") }
                    TextButton(onClick = {
                        onCancel()
                        showOptions = false
                    }) { Text("Cancelar") }
                }
            }
        )
    }
}

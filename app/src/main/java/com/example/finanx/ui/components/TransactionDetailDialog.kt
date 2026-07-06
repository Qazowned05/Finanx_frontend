package com.example.finanx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.TransactionType
import com.example.finanx.data.model.TransactionRecord
import com.example.finanx.ui.util.toLabel
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.util.toReadableDate

@Composable
fun TransactionDetailDialog(
    transaction: TransactionRecord,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Detalle del movimiento",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailRow("Descripción", transaction.description)
                DetailRow("Tipo", transaction.type.toMovementLabel())
                DetailRow("Monto", transaction.amount.toPen())
                DetailRow("Método", transaction.paymentMethod.toLabel())
                DetailRow("Fecha", transaction.createdAt.toReadableDate())

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Relaciones",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )

                DetailRow("Categoría", transaction.categoryName)
                DetailRow(
                    label = "Presupuesto",
                    value = "No informado por el servicio"
                )
                DetailRow(
                    label = "Meta de ahorro",
                    value = transaction.savingGoalName
                        ?: transaction.savingGoalId?.let { "Meta #$it" }
                        ?: "Sin meta asociada"
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

private fun TransactionType.toMovementLabel(): String =
    when (this) {
        TransactionType.INCOME -> "Ingreso"
        TransactionType.SPENT -> "Gasto"
    }

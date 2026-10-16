package com.elias.mismonedas.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.util.toCurrency
import com.elias.mismonedas.ui.util.toReadableDate
import org.openapitools.client.models.TransactionResponse
import java.math.BigDecimal

// todo verificar uso
@Composable
fun TransactionDetailDialog(
    transaction: TransactionResponse,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = transaction.reasonDescription ?: "Movimiento",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                DetailRow("Monto", (transaction.amount ?: BigDecimal.ZERO).toCurrency(transaction.walletCurrency ?: "PEN"))
                DetailRow("Categoría", transaction.categoryName ?: "Sin categoría")
                DetailRow("Método de pago", transaction.paymentMethod?.name ?: "Otro")
                DetailRow("Fecha", transaction.createdAt.toReadableDate())

                if (transaction.savingGoalId != null) {
                    DetailRow("Meta de ahorro", "Meta #${transaction.savingGoalId}")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Light,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = value)
        Spacer(Modifier.height(4.dp))
    }
}

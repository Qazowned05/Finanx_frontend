package com.example.finanx.ui.screens.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.DetailRow
import com.example.finanx.ui.viewmodel.TransactionViewModel
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.util.toReadableDate

@Composable
fun TransactionDetailScreen(
    transactionId: Long?,
    viewModel: TransactionViewModel,
    onBack: () -> Unit
) {
    val tx = transactionId?.let { id -> viewModel.state.transactions.find { it.id == id } }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Text(
                text = "Detalle del movimiento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (tx == null) {
            Text("Movimiento no encontrado", color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onBack) {
                Text("Volver")
            }
            return
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DetailRow("Descripción", tx.description)
                DetailRow("Tipo", tx.type.name.lowercase().replaceFirstChar { it.uppercase() })
                DetailRow("Monto", tx.amount.toPen())
                DetailRow("Método", tx.paymentMethod.name.lowercase().replaceFirstChar { it.uppercase() })
                DetailRow("Fecha", tx.createdAt.toReadableDate())

                DetailRow("Categoría", tx.categoryName)
                DetailRow("Meta de ahorro", tx.savingGoalName ?: tx.savingGoalId?.let { "Meta #$it" } ?: "Sin meta asociada")

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = onBack) { Text("Cerrar") }
                }
            }
        }
    }
}

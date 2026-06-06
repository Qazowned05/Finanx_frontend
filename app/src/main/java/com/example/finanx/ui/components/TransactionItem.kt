package com.example.finanx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.data.models.Transaction

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(40.dp)) // Icono de ejemplo
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.description, fontWeight = FontWeight.Bold)
                if (transaction.transactionScheduleId != null) {
                    Text("(Programada)", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                text = "${if (transaction.type == "EXPENSE") "- PEN" else "+ PEN"} ${transaction.amount}",
                color = if (transaction.type == "EXPENSE") Color.Red else Color.Green,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

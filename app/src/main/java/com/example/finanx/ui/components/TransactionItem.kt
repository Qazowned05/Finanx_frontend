package com.example.finanx.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.TransactionType
import com.example.finanx.data.model.TransactionRecord
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen

import com.example.finanx.ui.util.toLabel
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.util.toReadableDate

@Composable
fun TransactionRecordItem(
    transaction: TransactionRecord,
    modifier: Modifier = Modifier
) {
    val isIncome =
        transaction.type == TransactionType.INCOME

    val movementColor =
        if (isIncome) IncomeGreen else ExpenseRed

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = movementColor.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isIncome) {
                        Icons.Default.ArrowDownward
                    } else {
                        Icons.Default.ArrowUpward
                    },
                    contentDescription = null,
                    tint = movementColor
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.description,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = transaction.categoryName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = buildString {
                        append(transaction.paymentMethod.toLabel())
                        append(" · ")
                        append(transaction.createdAt.toReadableDate())
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = buildString {
                    append(if (isIncome) "+ " else "- ")
                    append(transaction.amount.toPen())
                },
                fontWeight = FontWeight.Bold,
                color = movementColor
            )
        }
    }
}
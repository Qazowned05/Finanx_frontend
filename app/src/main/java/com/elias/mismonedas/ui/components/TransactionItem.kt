package com.elias.mismonedas.ui.components

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
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.theme.ExpenseRed
import com.elias.mismonedas.ui.theme.IncomeGreen

import com.elias.mismonedas.ui.util.toCurrency
import com.elias.mismonedas.ui.util.toLabel
import com.elias.mismonedas.ui.util.toReadableDate
import org.openapitools.client.models.TransactionResponse
import org.openapitools.client.models.TransactionType
import java.math.BigDecimal

@Composable
fun TransactionRecordItem(
    transaction: TransactionResponse,
    modifier: Modifier = Modifier
) {
    val isIncome =
        transaction.type == TransactionType.INCOME

    val movementColor =
        if (isIncome) IncomeGreen else ExpenseRed

    val imageId = transaction.receiptImageId
    val hasReceipt = imageId != null && imageId > 0L

    FinanxCard(modifier = modifier.fillMaxWidth()) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = transaction.reasonDescription ?: "Movimiento",
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (hasReceipt) {
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = "Recibo adjunto",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Text(
                    text = transaction.categoryName ?: "Sin categoria",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = buildString {
                        append(transaction.paymentMethod?.toLabel() ?: "Otro")
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
                    append((transaction.amount ?: BigDecimal.ZERO).toCurrency(transaction.walletCurrency ?: "PEN"))
                },
                fontWeight = FontWeight.Bold,
                color = movementColor
            )
        }
    }
}

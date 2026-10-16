package com.elias.mismonedas.ui.screens.transaction

import android.util.Log
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.components.FinanxCard
import com.elias.mismonedas.ui.theme.ExpenseRed
import com.elias.mismonedas.ui.theme.IncomeGreen
import com.elias.mismonedas.ui.util.toCurrency
import com.elias.mismonedas.ui.util.toLabel
import com.elias.mismonedas.ui.util.toReadableDate
import com.elias.mismonedas.ui.util.toReadableType
import com.elias.mismonedas.ui.viewmodel.ReceiptViewModel
import com.elias.mismonedas.ui.viewmodel.TransactionViewModel
import org.openapitools.client.models.TransactionResponse
import org.openapitools.client.models.TransactionType

@Composable
fun TransactionDetailScreen(
    transactionId: Long?,
    viewModel: TransactionViewModel,
    receiptViewModel: ReceiptViewModel,
    //todo verificar uso
    onBack: () -> Unit
) {
    val tx = transactionId?.let { id -> viewModel.state.transactions.find { it.id == id } }

    val imageId = tx?.receiptImageId
    val hasReceiptImage = imageId != null && imageId > 0L

    LaunchedEffect(imageId) {
        if (imageId != null && imageId > 0L) {
            receiptViewModel.loadReceiptImage(imageId)
        }
    }

    if (tx == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Movimiento no encontrado",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    val isIncome = tx.type == TransactionType.INCOME
    val accentColor = if (isIncome) IncomeGreen else ExpenseRed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(accentColor.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column {
                Text(
                    text = tx.reasonDescription ?: "Sin descripcion",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tx.type?.toReadableType() ?: "Movimiento",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        FinanxCard {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                DetailInfoRow(
                    icon = Icons.Default.Receipt,
                    label = "Monto",
                    value = (tx.amount ?: java.math.BigDecimal.ZERO).toCurrency(tx.walletCurrency ?: "PEN"),
                    valueColor = accentColor,
                    bold = true
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                DetailInfoRow(
                    icon = Icons.Default.Category,
                    label = "Categoria",
                    value = tx.categoryName ?: "Sin categoria"
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                DetailInfoRow(
                    icon = Icons.Default.CreditCard,
                    label = "Metodo de pago",
                    value = tx.paymentMethod?.toLabel() ?: "No especificado"
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                DetailInfoRow(
                    icon = Icons.Default.DateRange,
                    label = "Fecha",
                    value = tx.createdAt.toReadableDate()
                )
            }
        }

        if (hasReceiptImage) {
            val imageBytes by remember { derivedStateOf { receiptViewModel.receiptImageBytes } }
            val isLoading by remember { derivedStateOf { receiptViewModel.receiptImageLoading } }

            Log.d("TxDetail", "imageId=$imageId, isLoading=$isLoading, imageBytes=${imageBytes?.size}")

            FinanxCard {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Comprobante",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    when {
                        isLoading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(3f / 4f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 2.5.dp)
                            }
                        }
                        imageBytes != null -> {
                            val bitmap = remember(imageBytes) {
                                Log.d("TxDetail", "Decoding bitmap: ${imageBytes!!.size} bytes")
                                val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes!!.size)
                                Log.d("TxDetail", "Bitmap result: $bmp (${bmp?.width}x${bmp?.height})")
                                bmp
                            }
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Imagen del recibo",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                        }
                    }
                }
            }
        }

        if (tx.savingGoalId != null) {
            FinanxCard {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Meta de ahorro",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Meta #${tx.savingGoalId}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun DetailInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    bold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
            color = valueColor
        )
    }
}

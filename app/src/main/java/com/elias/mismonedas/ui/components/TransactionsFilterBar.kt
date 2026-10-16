package com.elias.mismonedas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.data.model.CategoryOption
import com.elias.mismonedas.ui.theme.ExpenseRed
import com.elias.mismonedas.ui.theme.IncomeGreen
import org.openapitools.client.models.TransactionType
import org.openapitools.client.models.WalletResponse

const val ALL_TRANSACTIONS_CATEGORY = "Todos"

private data class UiCategory(val id: Long?, val label: String, val type: TransactionType?)

@Composable
fun TransactionsFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    categories: List<CategoryOption>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit,
    onDateClick: () -> Unit,
    selectedType: TransactionType?,
    onTypeSelected: (TransactionType?) -> Unit,
    wallets: List<WalletResponse>,
    selectedWalletId: Long?,
    onWalletSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryItems = remember(categories) {
        buildList {
            add(UiCategory(null, ALL_TRANSACTIONS_CATEGORY, null))
            addAll(
                categories
                    .filter { it.label.isNotBlank() }
                    .mapNotNull { option ->
                        val id = option.id ?: return@mapNotNull null
                        UiCategory(id, option.label, option.type)
                    }
            )
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Buscar movimientos") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            trailingIcon = {
                IconButton(onClick = onDateClick) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Seleccionar fecha"
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        LazyRow(
            state = rememberLazyListState(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = categoryItems,
                key = { it.id ?: -1L }
            ) { category ->
                TransactionCategoryCard(
                    label = category.label,
                    type = category.type,
                    selected = selectedCategoryId == category.id,
                    onClick = { onCategorySelected(category.id) }
                )
            }
        }

        LazyRow(
            state = rememberLazyListState(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedType == null,
                    onClick = { onTypeSelected(null) },
                    label = { Text("Todos") }
                )
            }
            item {
                FilterChip(
                    selected = selectedType == TransactionType.INCOME,
                    onClick = { onTypeSelected(TransactionType.INCOME) },
                    label = { Text("Ingresos") }
                )
            }
            item {
                FilterChip(
                    selected = selectedType == TransactionType.SPENT,
                    onClick = { onTypeSelected(TransactionType.SPENT) },
                    label = { Text("Gastos") }
                )
            }
        }

        if (wallets.size > 1) {
            LazyRow(
                state = rememberLazyListState(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedWalletId == null,
                        onClick = { onWalletSelected(null) },
                        label = { Text("Todas las billeteras") }
                    )
                }
                items(
                    items = wallets,
                    key = { it.id ?: -1L }
                ) { wallet ->
                    FilterChip(
                        selected = selectedWalletId == wallet.id,
                        onClick = { onWalletSelected(wallet.id) },
                        label = { Text(wallet.currencyCode ?: "Billetera ${wallet.id}") }
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionCategoryCard(
    label: String,
    type: TransactionType?,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val readableType = when (type) {
        TransactionType.SPENT -> "Gasto"
        TransactionType.INCOME -> "Ingreso"
        null -> "-"
    }
    val typeColor = when (type) {
        TransactionType.INCOME -> IncomeGreen
        TransactionType.SPENT -> ExpenseRed
        null -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    FinanxCard(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = readableType,
                style = MaterialTheme.typography.bodySmall,
                color = typeColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

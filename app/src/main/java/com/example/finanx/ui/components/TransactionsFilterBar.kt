package com.example.finanx.ui.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.finanx.data.model.CategoryOption
import com.example.finanx.data.model.TransactionType
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.util.toReadableType

const val ALL_TRANSACTIONS_CATEGORY = "Todos"

private data class UiCategory(val id: Long?, val label: String, val type: TransactionType?)

@Composable
fun TransactionsFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    categories: List<CategoryOption>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryItems = remember(categories) {
        buildList {
            add(UiCategory(null, ALL_TRANSACTIONS_CATEGORY, null))
            addAll(
                categories
                    .filter { it.label.isNotBlank() }
                    .distinctBy { it.label }
                    .map { UiCategory(it.id, it.label, it.type) }
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
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por motivo o categoría") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
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
                key = { it.label }
            ) { category ->
                TransactionCategoryCard(
                    label = category.label,
                    type = category.type,
                    selected = selectedCategory == category.label || (category.label == ALL_TRANSACTIONS_CATEGORY && selectedCategory.isBlank()),
                    onClick = { onCategorySelected(category.label) }
                )
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
    Card(
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
                text = type?.toReadableType() ?: "-",
                style = MaterialTheme.typography.bodySmall,
                color = when (type) {
                    TransactionType.INCOME -> IncomeGreen
                    TransactionType.SPENT -> ExpenseRed
                    null -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

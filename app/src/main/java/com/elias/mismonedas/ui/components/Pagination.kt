package com.elias.mismonedas.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Dispara [onLoadMore] cuando el último ítem visible está a [buffer] ítems del final.
 *
 * Pensado para infinite scroll: no recrea la lista ni salta, solo pide la siguiente
 * página cuando el usuario se acerca al borde inferior.
 */
@Composable
fun LoadMoreEffect(
    listState: LazyListState,
    enabled: Boolean,
    itemCount: Int,
    buffer: Int = 4,
    onLoadMore: () -> Unit
) {
    val shouldLoad by remember(listState) {
        derivedStateOf {
            val visible = listState.layoutInfo.visibleItemsInfo
            val lastVisible = visible.lastOrNull()?.index ?: -1
            val total = listState.layoutInfo.totalItemsCount
            total > 0 && lastVisible >= total - 1 - buffer
        }
    }
    LaunchedEffect(shouldLoad, enabled, itemCount) {
        if (enabled && shouldLoad) onLoadMore()
    }
}

/**
 * Footer con spinner mientras se carga la siguiente página.
 */
@Composable
fun LoadMoreFooter(
    isLoadingMore: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isLoadingMore) return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 2.dp,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * Muestra un aviso de "no hay más" cuando se llegó al final.
 */
@Composable
fun EndOfListFooter(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    if (!visible) return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay más resultados",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

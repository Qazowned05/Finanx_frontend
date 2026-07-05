package com.example.finanx.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RefreshableList(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    indicatorAlignment: Alignment = Alignment.TopCenter,
    content: @Composable () -> Unit
) {
    val state = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    Box(
        modifier = modifier.pullRefresh(state)
    ) {
        content()

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = state,
            modifier = Modifier.align(indicatorAlignment),
            contentColor = MaterialTheme.colorScheme.primary
        )
    }
}

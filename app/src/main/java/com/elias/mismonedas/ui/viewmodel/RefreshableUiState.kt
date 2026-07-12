package com.elias.mismonedas.ui.viewmodel

/**
 * Marker interface for UI states that support pull-to-refresh.
 * Implementations should expose a boolean indicating whether a pull-to-refresh is active.
 */
interface RefreshableUiState {
    val isRefreshing: Boolean
}
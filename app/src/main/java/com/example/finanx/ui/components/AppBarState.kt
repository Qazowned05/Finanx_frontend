package com.example.finanx.ui.components

/**
 * Estado del TopAppBar compartido por todas las screens a través del Scaffold principal.
 * Cada screen lo actualiza vía LaunchedEffect usando el callback onAppBarState.
 */
data class AppBarState(
    val title: String = "",
    val subtitle: String? = null,
    val showBack: Boolean = false,
    val onBack: (() -> Unit)? = null,
    val onRefresh: (() -> Unit)? = null
)
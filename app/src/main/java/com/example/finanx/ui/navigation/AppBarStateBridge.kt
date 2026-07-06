package com.example.finanx.ui.navigation

import com.example.finanx.ui.components.AppBarState

/**
 * AnalyticsScreen y ChatScreen todavía usan `onAppBarState: (AppBarState) -> Unit`
 * porque su TopBar cambia por razones internas a la pantalla (toggle de
 * refresco, sesión de chat activa, etc). En vez de tocar esas screens,
 * adaptamos su callback a [TopBarConfig] para que sigan publicando a través
 * del mismo [TopBarController] que todo lo demás.
 */
fun AppBarState.toTopBarConfig(onBackOverride: (() -> Unit)? = null): TopBarConfig = TopBarConfig(
    title = title,
    subtitle = subtitle,
    showBack = showBack,
    onBack = onBackOverride ?: onBack,
    onRefresh = onRefresh
)

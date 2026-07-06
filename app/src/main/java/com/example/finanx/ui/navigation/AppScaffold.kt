package com.example.finanx.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.finanx.ui.components.FinanxTopAppBar

/**
 * Reemplaza al Scaffold + TopBar + BottomBar que antes vivían embebidos
 * dentro de AppNavigation.kt. No sabe nada de pantallas concretas: solo
 * conoce la forma del back stack y la config publicada vía [SetTopBar].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    backStack: NavBackStack<NavKey>,
    notificationCount: Int,
    onFinn: () -> Unit,
    onNotifications: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToTab: (TopLevelDestination) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable (Modifier) -> Unit
) {
    var topBarConfig by remember { mutableStateOf(TopBarConfig()) }
    val controller = remember { TopBarController { topBarConfig = it } }

    val currentDestination = backStack.lastOrNull()
    val showBottomBar = currentDestination is TopLevelDestination
    val defaultShowBack = backStack.size > 1
    val defaultOnBack: () -> Unit = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            FinanxTopAppBar(
                title = topBarConfig.title,
                subtitle = topBarConfig.subtitle,
                showBack = topBarConfig.showBack ?: defaultShowBack,
                onBack = topBarConfig.onBack ?: defaultOnBack,
                onRefresh = topBarConfig.onRefresh,
                onFinn = onFinn,
                onNotifications = onNotifications,
                onProfile = onProfile,
                onLogout = onLogout,
                notificationCount = notificationCount
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
                    AppDestinations.bottomBarDestinations.forEach { tab ->
                        val selected = currentDestination == tab
                        NavigationBarItem(
                            selected = selected,
                            onClick = { if (!selected) onNavigateToTab(tab) },
                            icon = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        CompositionLocalProvider(
            LocalTopBarController provides controller,
            LocalSnackbarHostState provides snackbarHostState
        ) {
            content(Modifier.padding(innerPadding))
        }
    }
}

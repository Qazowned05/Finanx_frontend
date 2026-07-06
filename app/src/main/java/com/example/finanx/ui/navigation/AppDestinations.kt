package com.example.finanx.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Todas las pantallas de la app viven bajo esta jerarquía sellada.
 * Reemplaza al antiguo `Screen(route: String, ...)` y a las rutas string
 * usadas por Navigation 2 (`"transaction_detail/{id}"`, etc.).
 *
 * Cada key es @Serializable + NavKey para que el back stack sobreviva
 * cambios de configuración y, opcionalmente, la muerte del proceso.
 */
@Serializable
sealed interface AppDestination : NavKey

/**
 * Marca los 4 destinos raíz que viven en el bottom bar. Se usa para:
 *  - decidir cuándo se muestra el bottom bar (estructural, no una lista aparte)
 *  - decidir cuándo NO se muestra la flecha de back en el TopBar
 */
sealed interface TopLevelDestination : AppDestination {
    val label: String
    val icon: ImageVector
}

object AppDestinations {

    @Serializable
    data object Home : TopLevelDestination {
        override val label = "Inicio"
        override val icon = Icons.Default.Home
    }

    @Serializable
    data object Transactions : TopLevelDestination {
        override val label = "Movimientos"
        override val icon = Icons.Default.List
    }

    @Serializable
    data object SavingGoals : TopLevelDestination {
        override val label = "Metas"
        override val icon = Icons.Default.Savings
    }

    @Serializable
    data object More : TopLevelDestination {
        override val label = "Más"
        override val icon = Icons.Default.Menu
    }

    val bottomBarDestinations: List<TopLevelDestination> = listOf(Home, Transactions, SavingGoals, More)

    @Serializable
    data class TransactionDetail(val id: Long) : AppDestination

    @Serializable
    data object CreateTransaction : AppDestination

    @Serializable
    data object Categories : AppDestination

    @Serializable
    data object CreateSavingGoal : AppDestination

    @Serializable
    data object Analysis : AppDestination

    @Serializable
    data object Chat : AppDestination

    @Serializable
    data object Budgets : AppDestination

    @Serializable
    data object CreateBudget : AppDestination

    @Serializable
    data class EditBudget(val id: Long) : AppDestination

    @Serializable
    data object ScheduledTransactions : AppDestination

    @Serializable
    data object ScheduleChoice : AppDestination

    @Serializable
    data object CreateTransactionSchedule : AppDestination

    @Serializable
    data object CreateBudgetSchedule : AppDestination

    @Serializable
    data object Notifications : AppDestination

    @Serializable
    data object Profile : AppDestination

    @Serializable
    data object ProfileResetData : AppDestination

    @Serializable
    data object ProfileDeleteAccount : AppDestination

    /** Destino inicial de la sesión autenticada. */
    val START_DESTINATION: AppDestination = Home
}

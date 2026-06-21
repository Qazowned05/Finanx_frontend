package com.example.finanx.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finanx.ui.screens.AdminScreen
import com.example.finanx.ui.screens.AnalysisScreen
import com.example.finanx.ui.screens.HomeScreen
import com.example.finanx.ui.screens.TransactionsScreen
import com.example.finanx.ui.screens.analytics.AnalyticsScreen
import com.example.finanx.ui.screens.auth.LoginScreen
import com.example.finanx.ui.screens.auth.RegisterScreen
import com.example.finanx.ui.screens.budget.BudgetListScreen
import com.example.finanx.ui.screens.category.CategoryListScreen
import com.example.finanx.ui.screens.notification.NotificacionesScreen
import com.example.finanx.ui.screens.profile.ProfileScreen
import com.example.finanx.ui.screens.saving.SavingGoalScreen
import com.example.finanx.ui.screens.transaction.create.CreateTransactionScreen
import com.example.finanx.ui.screens.transaction.schedule.CreateScheduledTransactionScreen
import com.example.finanx.ui.screens.transaction.schedule.ScheduledTransactionsScreen
import com.example.finanx.ui.viewmodel.AuthState
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.BudgetViewModel
import com.example.finanx.ui.viewmodel.CategoryViewModel
import com.example.finanx.ui.viewmodel.HomeViewModel
import com.example.finanx.ui.viewmodel.ScheduledTransactionViewModel
import com.example.finanx.ui.viewmodel.SavingGoalViewModel
import com.example.finanx.ui.viewmodel.TransactionViewModel
import com.example.finanx.ui.viewmodel.UserViewModel
import org.openapitools.client.models.UserResponse

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home :
        Screen("home", "Inicio", Icons.Default.Home)

    data object Transactions :
        Screen("transactions", "Movimientos", Icons.Default.List)

    data object Calendar :
        Screen(
            "budgets",
            "Presupuestos",
            Icons.Default.AccountBalanceWallet
        )

    data object Analysis :
        Screen("analysis", "Análisis", Icons.Default.Analytics)

    data object More :
        Screen("more", "Más", Icons.Default.Menu)

    data object Categories :
        Screen("categories", "Categorías", Icons.Default.Category)

    data object SavingGoals :
        Screen("savinggoals", "Metas de Ahorro", Icons.Default.Savings)

    data object CreateTransaction :
        Screen(
            "create_transaction",
            "Nuevo movimiento",
            Icons.Default.Add
        )

    data object ScheduledTransactions :
        Screen(
            "scheduled_transactions",
            "Programaciones",
            Icons.Default.EventRepeat
        )

    data object CreateScheduledTransaction :
        Screen(
            "create_scheduled_transaction",
            "Nueva programación",
            Icons.Default.EventRepeat
        )

    data object Profile :
        Screen("profile", "Perfil", Icons.Default.Person)

    data object Notifications :
        Screen(
            "notifications",
            "Notificaciones",
            Icons.Default.Notifications
        )

    data object AnalyticsDashboard :
        Screen(
            "analytics_dashboard",
            "Resumen analítico",
            Icons.Default.Assessment
        )
}

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    val authState = authViewModel.authState

    var showRegister by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Idle) {
            showRegister = false
        }
    }

    when (authState) {
        is AuthState.Success -> {
            val user = authState.user
            val userId = user.id

            if (userId != null) {
                AppNavigation(
                    user = user,
                    authViewModel = authViewModel
                )
            } else {
                LaunchedEffect(Unit) {
                    authViewModel.logout()
                }
            }
        }

        else -> {
            if (showRegister) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onBackToLogin = {
                        showRegister = false
                    }
                )
            } else {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = {
                        showRegister = true
                    }
                )
            }
        }
    }
}

@Composable
private fun AppNavigation(
    user: UserResponse,
    authViewModel: AuthViewModel
) {
    val userId = requireNotNull(user.id)
    val displayName = user.displayName()

    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val budgetViewModel: BudgetViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val savingGoalViewModel: SavingGoalViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()
    val scheduledTransactionViewModel: ScheduledTransactionViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    /*
     * Solo cinco destinos principales.
     *
     * Categorías y metas se abren desde "Más", por lo que no deben
     * volver a aparecer en la barra inferior.
     */
    val bottomNavigationScreens = listOf(
        Screen.Home,
        Screen.Transactions,
        Screen.Calendar,
        Screen.Analysis,
        Screen.Categories,
        Screen.SavingGoals,
        Screen.More
    )

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    val showBottomBar =
        bottomNavigationScreens.any {
            it.route == currentRoute
        }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ) {
                    bottomNavigationScreens.forEach { screen ->
                        val selected =
                            currentRoute == screen.route

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    navController.navigate(screen.route) {
                                        popUpTo(
                                            navController
                                                .graph
                                                .findStartDestination()
                                                .id
                                        ) {
                                            saveState = true
                                        }

                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = {
                                Text(screen.title)
                            },
                            colors =
                                NavigationBarItemDefaults.colors(
                                    selectedIconColor =
                                        MaterialTheme.colorScheme.primary,
                                    selectedTextColor =
                                        MaterialTheme.colorScheme.primary,
                                    indicatorColor =
                                        MaterialTheme
                                            .colorScheme
                                            .primaryContainer
                                )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                LaunchedEffect(userId) {
                    homeViewModel.load(
                        userId = userId,
                        userName = displayName
                    )
                }

                HomeScreen(
                    state = homeViewModel.state,
                    onNewTransaction = {
                        navController.navigate(
                            Screen.CreateTransaction.route
                        )
                    },
                    onOpenTransactions = {
                        navController.navigate(
                            Screen.Transactions.route
                        )
                    },
                    onNotifications = {
                        navController.navigate(
                            Screen.Notifications.route
                        )
                    },
                    onProfile = {
                        navController.navigate(
                            Screen.Profile.route
                        )
                    },
                    onLogout = {
                        authViewModel.logout()
                    },
                    onRetry = {
                        homeViewModel.refresh()
                    }
                )
            }

            composable(Screen.Transactions.route) {
                LaunchedEffect(userId) {
                    transactionViewModel.load(userId)
                }

                TransactionsScreen(
                    state = transactionViewModel.state,
                    onQueryChange =
                        transactionViewModel::setQuery,
                    onRefresh =
                        transactionViewModel::refresh,
                    onNewTransaction = {
                        navController.navigate(
                            Screen.CreateTransaction.route
                        )
                    },
                    onOpenSchedules = {
                        navController.navigate(
                            Screen.ScheduledTransactions.route
                        )
                    },
                    onProfile = {
                        navController.navigate(
                            Screen.Profile.route
                        )
                    },
                    onLogout = {
                        authViewModel.logout()
                    }
                )
            }

            composable(Screen.Calendar.route) {
                BudgetListScreen(
                    userId = userId,
                    bvm = budgetViewModel,
                    authViewModel = authViewModel,
                    onNavigateToProfile = {
                        navController.navigate(
                            Screen.Profile.route
                        )
                    }
                )
            }

            composable(Screen.Analysis.route) {
                AnalysisScreen(
                    authViewModel = authViewModel
                )
            }

            composable(Screen.More.route) {
                AdminScreen(
                    authViewModel = authViewModel,
                    onNavigateToCategories = {
                        navController.navigate(
                            Screen.Categories.route
                        )
                    },
                    onNavigateToSavingGoals = {
                        navController.navigate(
                            Screen.SavingGoals.route
                        )
                    },
                    onNavigateToProfile = {
                        navController.navigate(
                            Screen.Profile.route
                        )
                    },
                    onNavigateToAnalyticsDashboard = {
                        navController.navigate(
                            Screen.AnalyticsDashboard.route
                        )
                    }
                )
            }

            composable(Screen.Categories.route) {
                CategoryListScreen(
                    userId = userId,
                    viewModel = categoryViewModel,
                    authViewModel = authViewModel
                )
            }

            composable(Screen.SavingGoals.route) {
                SavingGoalScreen(
                    userId = userId,
                    viewModel = savingGoalViewModel,
                    authViewModel = authViewModel
                )
            }

            composable(Screen.CreateTransaction.route) {
                /*
                 * Si el usuario llega desde Home antes de entrar a
                 * Movimientos, aún podrían no haberse cargado las
                 * categorías, motivos y metas.
                 */
                LaunchedEffect(userId) {
                    val options =
                        transactionViewModel
                            .state
                            .formOptions

                    if (
                        options.categories.isEmpty() &&
                        !transactionViewModel.state.isLoading
                    ) {
                        transactionViewModel.load(userId)
                    }
                }

                CreateTransactionScreen(
                    userId = userId,
                    formOptions =
                        transactionViewModel.state.formOptions,
                    isLoading =
                        transactionViewModel.state.isLoading,
                    loadError =
                        transactionViewModel.state.error,
                    saveState =
                        transactionViewModel.state.saveState,
                    onBack = {
                        navController.popBackStack()
                    },
                    onRetry = {
                        transactionViewModel.load(userId)
                    },
                    onSubmit = {
                        transactionViewModel.createTransaction(it)
                    },
                    onSaved = {
                        /*
                         * Evita que Success vuelva a disparar la
                         * navegación al recomponer.
                         */
                        transactionViewModel.consumeSaveState()

                        /*
                         * Actualiza Home después de crear el
                         * movimiento.
                         */
                        homeViewModel.load(
                            userId = userId,
                            userName = displayName
                        )

                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.ScheduledTransactions.route) {
                LaunchedEffect(userId) {
                    scheduledTransactionViewModel.load(userId)
                }

                ScheduledTransactionsScreen(
                    state = scheduledTransactionViewModel.state,
                    onQueryChange =
                        scheduledTransactionViewModel::setQuery,
                    onRefresh =
                        scheduledTransactionViewModel::refresh,
                    onNewSchedule = {
                        navController.navigate(
                            Screen.CreateScheduledTransaction.route
                        )
                    },
                    onPauseSchedule =
                        scheduledTransactionViewModel::pauseSchedule,
                    onResumeSchedule =
                        scheduledTransactionViewModel::resumeSchedule,
                    onCancelSchedule =
                        scheduledTransactionViewModel::cancelSchedule,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.CreateScheduledTransaction.route) {
                LaunchedEffect(userId) {
                    val options =
                        scheduledTransactionViewModel
                            .state
                            .formOptions

                    if (
                        options.categories.isEmpty() &&
                        !scheduledTransactionViewModel.state.isLoading
                    ) {
                        scheduledTransactionViewModel.load(userId)
                    }
                }

                CreateScheduledTransactionScreen(
                    userId = userId,
                    state = scheduledTransactionViewModel.state,
                    onBack = {
                        navController.popBackStack()
                    },
                    onRetry = {
                        scheduledTransactionViewModel.load(userId)
                    },
                    onSubmit = {
                        scheduledTransactionViewModel.createSchedule(it)
                    },
                    onSaved = {
                        scheduledTransactionViewModel.consumeSaveState()
                        homeViewModel.load(
                            userId = userId,
                            userName = displayName
                        )
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    user = user,
                    userId = userId,
                    viewModel = userViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Notifications.route) {
                NotificacionesScreen(
                    userId = userId
                )
            }

            composable(Screen.AnalyticsDashboard.route) {
                AnalyticsScreen(
                    userId = userId
                )
            }
        }
    }
}

private fun UserResponse.displayName(): String {
    val nameAndLastname = listOfNotNull(
        name?.takeIf(String::isNotBlank),
        lastname?.takeIf(String::isNotBlank)
    ).joinToString(" ")

    return fullName
        ?.takeIf(String::isNotBlank)
        ?: nameAndLastname.takeIf(String::isNotBlank)
        ?: username?.takeIf(String::isNotBlank)
        ?: "Usuario"
}

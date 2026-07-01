package com.example.finanx.ui

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finanx.ui.components.AppBarState
import com.example.finanx.ui.components.FinanxTopAppBar
import com.example.finanx.ui.screens.HomeScreen
import com.example.finanx.ui.screens.TransactionsScreen
import com.example.finanx.ui.screens.analytics.AnalyticsScreen
import com.example.finanx.ui.screens.auth.LoginScreen
import com.example.finanx.ui.screens.auth.RegisterScreen
import com.example.finanx.ui.screens.budget.BudgetListScreen
import com.example.finanx.ui.screens.category.CategoryListScreen
import com.example.finanx.ui.screens.chat.ChatScreen
import com.example.finanx.ui.screens.notification.NotificacionesScreen
import com.example.finanx.ui.screens.profile.ProfileScreen
import com.example.finanx.ui.screens.saving.SavingGoalScreen
import com.example.finanx.ui.screens.transaction.create.CreateTransactionFormState
import com.example.finanx.ui.screens.transaction.create.CreateTransactionScreen
import com.example.finanx.ui.screens.transaction.schedule.ScheduledTransactionsScreen
import com.example.finanx.ui.theme.GoldAccent
import com.example.finanx.ui.viewmodel.*
import org.openapitools.client.models.UserResponse

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    // Bottom bar — 5 ítems
    data object Home : Screen("home", "Inicio", Icons.Default.Home)
    data object Transactions : Screen("transactions", "Movimientos", Icons.Default.List)
    data object Categories : Screen("categories", "Categorías", Icons.Default.Category)
    data object SavingGoals : Screen("savinggoals", "Metas", Icons.Default.Savings)

    data object More : Screen("more", "Más", Icons.Default.Menu)


    // Secundarias — accesibles desde "Más" o navegación directa
    data object Analysis : Screen("analysis", "Análisis", Icons.Default.Analytics)
    data object Chat : Screen("chat", "Finn", Icons.Default.AutoAwesome)
    data object Budgets : Screen("budgets", "Presupuestos", Icons.Default.AccountBalanceWallet)
    data object ScheduledTransactions : Screen("scheduled_transactions", "Programaciones", Icons.Default.EventRepeat)
    data object Notifications :
        Screen("notifications", "Notificaciones", Icons.Default.Notifications)

    data object Profile : Screen("profile", "Perfil", Icons.Default.Person)
    data object CreateTransaction :
        Screen("create_transaction", "Nuevo movimiento", Icons.Default.Add)

    data object CreateScheduledTransaction :
        Screen("create_scheduled_transaction", "Nueva programación", Icons.Default.EventRepeat)
}

private val bottomBarScreens = listOf(
    Screen.Home,
    Screen.Transactions,
    Screen.Categories,
    Screen.SavingGoals,
    Screen.More
)

private val bottomBarRoutes = bottomBarScreens.map { it.route }.toSet()

private val secondaryRoutes = setOf(
    Screen.Analysis.route,
    Screen.Notifications.route,
    Screen.Profile.route,
    Screen.CreateTransaction.route,
    Screen.CreateScheduledTransaction.route,
    Screen.ScheduledTransactions.route,
    Screen.Chat.route
)

@Composable
fun MainScreen(authViewModel: AuthViewModel = viewModel()) {
    val authState = authViewModel.authState
    var showRegister by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.restoreSession()
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Idle) showRegister = false
    }

    when (authState) {
        is AuthState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GoldAccent)
            }
        }

        is AuthState.Success -> {
            val user = authState.user
            val userId = user.id
            if (userId != null) {
                AppNavigation(user = user, authViewModel = authViewModel)
            } else {
                LaunchedEffect(Unit) { authViewModel.logout() }
            }
        }

        else -> {
            if (showRegister) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onBackToLogin = { showRegister = false }
                )
            } else {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = { showRegister = true }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppNavigation(user: UserResponse, authViewModel: AuthViewModel) {
    val userId = requireNotNull(user.id)
    val context = LocalContext.current
    val activity = context as? Activity

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // ViewModels
    val analyticsViewModel: AnalyticsViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val budgetViewModel: BudgetViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val savingGoalViewModel: SavingGoalViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()
    val scheduledTransactionViewModel: ScheduledTransactionViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel()

    var appBarState by remember { mutableStateOf(AppBarState()) }

    var showMoreSheet by remember { mutableStateOf(false) }
    var customBackHandler by remember { mutableStateOf<(() -> Unit)?>(null) }

    val showBottomBar = currentRoute in bottomBarRoutes
    val showBack = currentRoute in secondaryRoutes

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    if (showMoreSheet) {
        MoreBottomSheet(
            onDismiss = { showMoreSheet = false },
            onAnalysis = {
                showMoreSheet = false
                navController.navigate(Screen.Analysis.route)
            },
            onBudgets = { showMoreSheet = false; navController.navigate(Screen.Budgets.route) },
            onScheduled = {
                showMoreSheet = false; navController.navigate(Screen.ScheduledTransactions.route)
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            FinanxTopAppBar(
                title = appBarState.title,
                subtitle = appBarState.subtitle,
                showBack = showBack,
                onBack = {  customBackHandler?.invoke() ?: navController.popBackStack() },
                onRefresh = appBarState.onRefresh,
                onFinn = { navController.navigate(Screen.Chat.route) },
                onNotifications = { navController.navigate(Screen.Notifications.route) },
                onProfile = { navController.navigate(Screen.Profile.route) },
                onLogout = { authViewModel.logout(activity) }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    bottomBarScreens.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                when (screen) {
                                    Screen.More -> {
                                        showMoreSheet = true
                                    }
                                    else -> {
                                        if (!selected) navigateTo(screen.route)
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = { Text(screen.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                    // "Más" como ítem extra si hubiera overflow, aquí como FAB alternativo
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
                    appBarState = AppBarState(title = "Inicio")
                    homeViewModel.load(userId = userId, userName = user.displayName())
                }
                HomeScreen(
                    state = homeViewModel.state,
                    onNewTransaction = { navController.navigate(Screen.CreateTransaction.route) },
                    onOpenTransactions = { navigateTo(Screen.Transactions.route) },
                    onNotifications = { navController.navigate(Screen.Notifications.route) },
                    onProfile = { navController.navigate(Screen.Profile.route) },
                    onLogout = { authViewModel.logout(activity) },
                    onRetry = { homeViewModel.refresh() }
                )
            }

            composable(Screen.Transactions.route) {
                LaunchedEffect(userId) {
                    appBarState = AppBarState(title = "Movimientos")
                    transactionViewModel.load(userId)
                }
                TransactionsScreen(
                    state = transactionViewModel.state,
                    onQueryChange = transactionViewModel::setQuery,
                    onRefresh = transactionViewModel::refresh,
                    onNewTransaction = { navController.navigate(Screen.CreateTransaction.route) },
                    onOpenSchedules = { navController.navigate(Screen.ScheduledTransactions.route) },
                    onNotifications = { navController.navigate(Screen.Notifications.route) },
                    onProfile = { navController.navigate(Screen.Profile.route) },
                    onLogout = { authViewModel.logout(activity) }
                )
            }

            composable(Screen.Categories.route) {
                LaunchedEffect(userId) {
                    appBarState = AppBarState(title = "Categorías")
                }
                CategoryListScreen(
                    userId = userId,
                    viewModel = categoryViewModel,
                    authViewModel = authViewModel,
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                )
            }

            composable(Screen.SavingGoals.route) {
                LaunchedEffect(userId) {
                    appBarState = AppBarState(title = "Metas de ahorro")
                }
                SavingGoalScreen(
                    userId = userId,
                    viewModel = savingGoalViewModel,
                    authViewModel = authViewModel,
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                )
            }

            composable(Screen.Analysis.route) {
                AnalyticsScreen(
                    userId = userId,
                    viewModel = analyticsViewModel,
                    onAppBarState = { appBarState = it }
                )
            }

            composable(Screen.Chat.route) {
                ChatScreen(
                    userId = userId,
                    viewModel = chatViewModel,
                    onAppBarState = { appBarState = it }
                )
            }

            composable(Screen.Budgets.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Presupuestos", showBack = true)
                }
                BudgetListScreen(
                    userId = userId,
                    bvm = budgetViewModel,
                    authViewModel = authViewModel,
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) }
                )
            }

            composable(Screen.ScheduledTransactions.route) {
                LaunchedEffect(userId) {
                    appBarState = AppBarState(title = "Programaciones", showBack = true)
                    scheduledTransactionViewModel.load(userId)
                }
                ScheduledTransactionsScreen(
                    state = scheduledTransactionViewModel.state,
                    onQueryChange = scheduledTransactionViewModel::setQuery,
                    onRefresh = scheduledTransactionViewModel::refresh,
                    onNewSchedule = { navController.navigate(Screen.CreateScheduledTransaction.route) },
                    onPauseSchedule = scheduledTransactionViewModel::pauseSchedule,
                    onResumeSchedule = scheduledTransactionViewModel::resumeSchedule,
                    onCancelSchedule = scheduledTransactionViewModel::cancelSchedule,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.CreateTransaction.route) {
                var step by rememberSaveable { mutableIntStateOf(1) }

                LaunchedEffect(step) {
                    appBarState = AppBarState(
                        title = "Nuevo movimiento",
                        subtitle = "Paso $step de 2",
                        showBack = true
                    )

                    customBackHandler = {
                        if (step == 2) step = 1
                        else navController.popBackStack()
                    }
                }

                DisposableEffect(Unit) {
                    onDispose { customBackHandler = null }
                }
                CreateTransactionScreen(
                    userId = userId,
                    formOptions = transactionViewModel.state.formOptions,
                    isLoading = transactionViewModel.state.isLoading,
                    loadError = transactionViewModel.state.error,
                    saveState = transactionViewModel.state.saveState,
                    step = step,
                    onStepChange = { step = it },
                    onBack = { navController.popBackStack() },
                    onRetry = { transactionViewModel.load(userId) },
                    onSubmit = { transactionViewModel.createTransaction(it) },
                    onSaved = {
                        transactionViewModel.consumeSaveState()
                        homeViewModel.load(userId = userId, userName = user.displayName())
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Profile.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Mi perfil", showBack = true)
                }
                ProfileScreen(
                    user = user,
                    userId = userId,
                    viewModel = userViewModel,
                    onLogout = { authViewModel.logout(activity) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Notifications.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Notificaciones", showBack = true)
                }
                NotificacionesScreen(
                    userId = userId,
                    onBack = { navController.popBackStack() },
                    onProfile = { navController.navigate(Screen.Profile.route) },
                    onLogout = { authViewModel.logout(activity) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoreBottomSheet(
    onDismiss: () -> Unit,
    onAnalysis: () -> Unit,
    onBudgets: () -> Unit,
    onScheduled: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        ListItem(
            headlineContent = { Text("Análisis") },
            leadingContent = {
                Icon(Icons.Default.Analytics, contentDescription = null)
            },
            modifier = Modifier.clickable(onClick = onAnalysis)
        )
        ListItem(
            headlineContent = { Text("Presupuestos") },
            leadingContent = {
                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null)
            },
            modifier = Modifier.clickable(onClick = onBudgets)
        )
        ListItem(
            headlineContent = { Text("Programaciones") },
            leadingContent = {
                Icon(Icons.Default.EventRepeat, contentDescription = null)
            },
            modifier = Modifier.clickable(onClick = onScheduled)
        )
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
    }
}

private fun UserResponse.displayName(): String {
    val nameAndLastname = listOfNotNull(
        name?.takeIf(String::isNotBlank),
        lastname?.takeIf(String::isNotBlank)
    ).joinToString(" ")

    return fullName?.takeIf(String::isNotBlank)
        ?: nameAndLastname.takeIf(String::isNotBlank)
        ?: username?.takeIf(String::isNotBlank)
        ?: "Usuario"
}
package com.example.finanx.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.DeleteForever
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finanx.ui.components.AppBarState
import com.example.finanx.ui.components.FinanxTopAppBar
import com.example.finanx.ui.screens.FunctionalitiesScreen
import com.example.finanx.ui.screens.HomeScreen
import com.example.finanx.ui.screens.TransactionsScreen
import com.example.finanx.ui.screens.transaction.TransactionDetailScreen
import com.example.finanx.ui.screens.analytics.AnalyticsScreen
import com.example.finanx.ui.screens.budget.BudgetListScreen
import com.example.finanx.ui.screens.budget.CreateBudgetScreen
import com.example.finanx.ui.screens.budget.EditBudgetScreen
import com.example.finanx.ui.screens.category.CategoryListScreen
import com.example.finanx.ui.screens.chat.ChatScreen
import com.example.finanx.ui.screens.notification.NotificationsScreen
import com.example.finanx.ui.screens.profile.ProfileScreen
import com.example.finanx.ui.screens.profile.DeleteAccountScreen
import com.example.finanx.ui.screens.profile.ResetDataScreen
import com.example.finanx.ui.screens.saving.SavingGoalScreen
import com.example.finanx.ui.screens.saving.CreateSavingGoalScreen
import com.example.finanx.ui.screens.transaction.create.CreateTransactionScreen
import com.example.finanx.ui.screens.schedule.CreateBudgetScheduleScreen
import com.example.finanx.ui.screens.schedule.CreateTransactionScheduleScreen
import com.example.finanx.ui.screens.schedule.ScheduleChoiceScreen
import com.example.finanx.ui.screens.schedule.ScheduledTransactionsScreen
import com.example.finanx.ui.theme.BrownPrimary
import com.example.finanx.ui.theme.TextLight
import com.example.finanx.ui.viewmodel.AnalyticsViewModel
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.BudgetViewModel
import com.example.finanx.ui.viewmodel.CategoryState
import com.example.finanx.ui.viewmodel.CategoryViewModel
import com.example.finanx.ui.viewmodel.ChatViewModel
import com.example.finanx.ui.viewmodel.HomeViewModel
import com.example.finanx.ui.viewmodel.NotificationViewModel
import com.example.finanx.ui.viewmodel.SavingGoalViewModel
import com.example.finanx.ui.viewmodel.ScheduleFormState
import com.example.finanx.ui.viewmodel.ScheduleViewModel
import com.example.finanx.ui.viewmodel.TransactionViewModel
import com.example.finanx.ui.viewmodel.visibleSteps
import com.example.finanx.ui.viewmodel.UserViewModel
import org.openapitools.client.models.UserResponse
import kotlin.collections.contains


sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Inicio", Icons.Default.Home)
    data object Transactions : Screen("transactions", "Movimientos", Icons.Default.List)
    data object Categories : Screen("categories", "Categorías", Icons.Default.Category)
    data object SavingGoals : Screen("savinggoals", "Metas", Icons.Default.Savings)

    data object More : Screen("functionalities", "Más", Icons.Default.Menu)


    data object Analysis : Screen("analysis", "Análisis", Icons.Default.Analytics)
    data object Chat : Screen("chat", "Finn", Icons.Default.AutoAwesome)
    data object Budgets : Screen("budgets", "Presupuestos", Icons.Default.AccountBalanceWallet)
    data object ScheduledTransactions :
        Screen("scheduled_transactions", "Programaciones", Icons.Default.EventRepeat)
    data object ScheduleChoice :
        Screen("schedule_choice", "Nueva programación", Icons.Default.Add)
    data object CreateTransactionSchedule :
        Screen("create_transaction_schedule", "Programar movimiento", Icons.Default.EventRepeat)
    data object CreateBudgetSchedule :
        Screen("create_budget_schedule", "Programar presupuesto", Icons.Default.AccountBalanceWallet)
    data object Notifications :
        Screen("notifications", "Notificaciones", Icons.Default.Notifications)
    data object Profile : Screen("profile", "Perfil", Icons.Default.Person)
    data object ProfileResetData : Screen("profile/reset-data", "Empezar de Cero", Icons.Default.RestartAlt)
    data object ProfileDeleteAccount : Screen("profile/delete-account", "Eliminar Cuenta", Icons.Default.DeleteForever)
    data object CreateTransaction :
        Screen("create_transaction", "Nuevo movimiento", Icons.Default.Add)
    data object CreateSavingGoal :
        Screen("create_saving_goal", "Crear meta", Icons.Default.Savings)
    data object CreateBudget : Screen("create_budget", "Nuevo presupuesto", Icons.Default.Add)
}


private val bottomBarScreens = listOf(
    Screen.Home,
    Screen.Transactions,
    Screen.SavingGoals,
    Screen.More
)

private val bottomBarRoutes = bottomBarScreens.map { it.route }.toSet()

private val secondaryRoutes = setOf(
    Screen.Analysis.route,
    Screen.Notifications.route,
    Screen.Profile.route,
    Screen.ProfileResetData.route,
    Screen.ProfileDeleteAccount.route,
    Screen.Categories.route,
    Screen.CreateTransaction.route,
    Screen.CreateSavingGoal.route,
    "transaction_detail/{id}",
    Screen.ScheduledTransactions.route,
    Screen.ScheduleChoice.route,
    Screen.CreateTransactionSchedule.route,
    Screen.CreateBudgetSchedule.route,
    Screen.Chat.route,
    Screen.More.route
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(user: UserResponse, authViewModel: AuthViewModel) {
    val userId = requireNotNull(user.id)
    val context = LocalContext.current
    val activity = context as? Activity

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }

    val analyticsViewModel: AnalyticsViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val budgetViewModel: BudgetViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val savingGoalViewModel: SavingGoalViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()
    val scheduleViewModel: ScheduleViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()

    var appBarState by remember { mutableStateOf(AppBarState()) }

    var customBackHandler by remember { mutableStateOf<(() -> Unit)?>(null) }

    // callbacks for screens to register an "unsaved changes" check callable
    var hasUnsavedCreateTransaction: (() -> Boolean)? = null
    var hasUnsavedCreateTransactionSchedule: (() -> Boolean)? = null
    var hasUnsavedCreateBudgetSchedule: (() -> Boolean)? = null

    var showConfirmDiscard by remember { mutableStateOf(false) }
    var pendingPopAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val showBottomBar = currentRoute in bottomBarRoutes
    val showBack = currentRoute in secondaryRoutes

    val handleBack = {
        customBackHandler?.invoke() ?: navController.popBackStack()
    }

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    LaunchedEffect(userId) {
        notificationViewModel.fetchAll(userId)
    }

    LaunchedEffect(transactionViewModel.state.actionMessage) {
        val message = transactionViewModel.state.actionMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        transactionViewModel.consumeMessage()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
                FinanxTopAppBar(
                title = appBarState.title,
                subtitle = appBarState.subtitle,
                showBack = showBack,
                onBack = { handleBack() },
                onRefresh = appBarState.onRefresh,
                onFinn = { navController.navigate(Screen.Chat.route) },
                onNotifications = { navController.navigate(Screen.Notifications.route) },
                onProfile = { navController.navigate(Screen.Profile.route) },
                onLogout = { authViewModel.logout(activity) },
                 notificationCount = notificationViewModel.badgeCount
             )
         },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
                    bottomBarScreens.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                when (screen) {
                                    Screen.More -> {
                                        if (!selected) navigateTo(screen.route)
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
        BackHandler(enabled = showBack) {
            handleBack()
        }

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
                    onDeleteTransaction = transactionViewModel::disableTransaction,
                    onOpenTransactionDetails = { transaction ->
                        transaction.id?.let { id ->
                            transactionViewModel.selectTransaction(id)
                            navController.navigate("transaction_detail/$id")
                        }
                    }
                )
            }

            composable("transaction_detail/{id}") { backStackEntry ->
                val idArg = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                LaunchedEffect(idArg) {
                    appBarState = AppBarState(title = "Detalle del movimiento", showBack = true)
                    idArg?.let { transactionViewModel.selectTransaction(it) }
                }
                DisposableEffect(Unit) {
                    onDispose { transactionViewModel.clearSelectedTransaction() }
                }
                TransactionDetailScreen(
                    transactionId = idArg,
                    viewModel = transactionViewModel,
                    onBack = { navController.popBackStack() }
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
                    onCreateGoal = { navController.navigate(Screen.CreateSavingGoal.route) }
                )
            }

            composable(Screen.CreateSavingGoal.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Crear meta", showBack = true)
                }
                DisposableEffect(Unit) { onDispose { customBackHandler = null } }
                CreateSavingGoalScreen(
                    userId = userId,
                    viewModel = savingGoalViewModel,
                    onSuccess = { navController.popBackStack() }
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
                LaunchedEffect(chatViewModel.state.selectedSession) {
                    customBackHandler = chatViewModel.state.selectedSession?.let {
                        { chatViewModel.closeSelectedSession() }
                    }
                }
                DisposableEffect(Unit) {
                    onDispose { customBackHandler = null }
                }
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
                    viewModel = budgetViewModel,
                    onCreateBudget = { navController.navigate(Screen.CreateBudget.route) },
                    onEditBudget = { id -> navController.navigate("edit_budget/$id") }
                )
            }

            composable(Screen.ScheduledTransactions.route) {
                LaunchedEffect(userId) {
                    appBarState = AppBarState(title = "Programaciones", showBack = true)
                    scheduleViewModel.fetchAll(userId)
                }
                ScheduledTransactionsScreen(
                    viewModel = scheduleViewModel,
                    userId = userId,
                    onNewSchedule = { navController.navigate(Screen.ScheduleChoice.route) }
                )
            }

            composable(Screen.ScheduleChoice.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Nueva programación", showBack = true)
                }
                ScheduleChoiceScreen(
                    onChooseTransaction = { navController.navigate(Screen.CreateTransactionSchedule.route) },
                    onChooseBudget = { navController.navigate(Screen.CreateBudgetSchedule.route) }
                )
            }

            composable(Screen.CreateTransactionSchedule.route) {
                LaunchedEffect(userId) {
                    appBarState = AppBarState(title = "Programar movimiento", showBack = true)
                    scheduleViewModel.loadFormOptions(userId)
                    customBackHandler = {
                        val hasUnsaved = hasUnsavedCreateTransactionSchedule?.invoke() == true
                        if (hasUnsaved) {
                            pendingPopAction = { navController.popBackStack() }
                            showConfirmDiscard = true
                        } else {
                            navController.popBackStack()
                        }
                    }
                }
                DisposableEffect(Unit) {
                    onDispose { customBackHandler = null }
                }
                val formState = scheduleViewModel.formState
                val categories = if (formState is ScheduleFormState.Ready)
                    CategoryState.Success(formState.options.categories)
                else
                    CategoryState.Loading
                val reasons = if (formState is ScheduleFormState.Ready)
                    formState.options.reasons else emptyList()
                CreateTransactionScheduleScreen(
                    registerHasUnsaved = { fn -> hasUnsavedCreateTransactionSchedule = fn },
                    userId = userId,
                    categoryState = categories,
                    reasons = reasons,
                    viewModel = scheduleViewModel,
                    onSuccess = {
                        navController.popBackStack(Screen.ScheduledTransactions.route, inclusive = false)
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.CreateBudgetSchedule.route) {
                LaunchedEffect(userId) {
                    appBarState = AppBarState(title = "Programar presupuesto", showBack = true)
                    scheduleViewModel.loadFormOptions(userId)
                    customBackHandler = {
                        val hasUnsaved = hasUnsavedCreateBudgetSchedule?.invoke() == true
                        if (hasUnsaved) {
                            pendingPopAction = { navController.popBackStack() }
                            showConfirmDiscard = true
                        } else {
                            navController.popBackStack()
                        }
                    }
                }
                DisposableEffect(Unit) {
                    onDispose { customBackHandler = null }
                }
                val formState = scheduleViewModel.formState
                val categories = if (formState is ScheduleFormState.Ready)
                    CategoryState.Success(formState.options.categories)
                else
                    CategoryState.Loading
                CreateBudgetScheduleScreen(
                    registerHasUnsaved = { fn -> hasUnsavedCreateBudgetSchedule = fn },
                    userId = userId,
                    categoryState = categories,
                    viewModel = scheduleViewModel,
                    onSuccess = {
                        navController.popBackStack(Screen.ScheduledTransactions.route, inclusive = false)
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.CreateTransaction.route) {
                LaunchedEffect(Unit) {
                    val preserveDraft = navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<Boolean>("preserve_transaction_draft") == true
                    if (!preserveDraft) {
                        transactionViewModel.resetDraft()
                    }
                    // ensure form options and saving goals are loaded
                    val options = transactionViewModel.state.formOptions
                    if (options.categories.isEmpty() && !transactionViewModel.state.isLoading) {
                        transactionViewModel.load(userId)
                    }
                    savingGoalViewModel.fetchSavingGoals(userId)
                }

                LaunchedEffect(transactionViewModel.draft.step, savingGoalViewModel.savingGoalState) {
                    val hasActiveSavingGoals = when (val s = savingGoalViewModel.savingGoalState) {
                        is com.example.finanx.ui.viewmodel.SavingGoalState.Success -> s.goals.isNotEmpty()
                        else -> transactionViewModel.state.formOptions.savingGoals.isNotEmpty()
                    }
                    val steps = transactionViewModel.draft.visibleSteps(hasActiveSavingGoals)
                    val currentIndex = steps.indexOf(transactionViewModel.draft.step)
                    appBarState = if (transactionViewModel.draft.step == com.example.finanx.ui.viewmodel.TransactionStep.TYPE) {
                        AppBarState(title = "", subtitle = null, showBack = true)
                    } else {
                        AppBarState(
                            title = "Nuevo movimiento",
                            subtitle = "Paso ${if (currentIndex >= 0) currentIndex + 1 else 1} de ${steps.size}",
                            showBack = true
                        )
                    }

                    customBackHandler = {
                        if (currentIndex > 0) {
                            transactionViewModel.goToStep(steps[currentIndex - 1])
                        } else {
                            val hasUnsaved = hasUnsavedCreateTransaction?.invoke() == true
                            if (hasUnsaved) {
                                pendingPopAction = { navController.popBackStack() }
                                showConfirmDiscard = true
                            } else {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                DisposableEffect(Unit) {
                    onDispose { customBackHandler = null }
                }

                CreateTransactionScreen(
                    registerHasUnsaved = { fn -> hasUnsavedCreateTransaction = fn },
                    userId = userId,
                    transactionViewModel = transactionViewModel,
                    savingGoalViewModel = savingGoalViewModel,
                    onCreateSavingGoal = {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("preserve_transaction_draft", true)
                        navController.navigate(Screen.CreateSavingGoal.route)
                    },
                    onCreateCategory = { navController.navigate(Screen.Categories.route) },
                    onBack = { navController.popBackStack() },
                    onSaved = {
                        transactionViewModel.resetDraft()
                        transactionViewModel.consumeSaveState()
                        homeViewModel.load(userId = userId, userName = user.displayName())
                        navController.popBackStack()
                    }
                )

            }


            composable(Screen.More.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Para ti", showBack = true)
                }
                FunctionalitiesScreen(
                    onNavigate = { route -> navController.navigate(route) }
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
                    onOpenResetData = { navController.navigate(Screen.ProfileResetData.route) },
                    onOpenDeleteAccount = { navController.navigate(Screen.ProfileDeleteAccount.route) }
                )
            }

            composable(Screen.ProfileResetData.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Empezar de Cero", showBack = true)
                }
                ResetDataScreen(
                    userId = userId,
                    viewModel = userViewModel,
                    onBack = { navController.popBackStack() },
                    onLogout = { authViewModel.logout(activity) }
                )
            }

            composable(Screen.ProfileDeleteAccount.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Eliminar Cuenta", showBack = true)
                }
                DeleteAccountScreen(
                    userId = userId,
                    viewModel = userViewModel,
                    onBack = { navController.popBackStack() },
                    onLogout = { authViewModel.logout(activity) }
                )
            }

            composable(Screen.Notifications.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Notificaciones", showBack = true)
                }
                NotificationsScreen(
                    userId = userId,
                    onBack = { navController.popBackStack() },
                    viewModel = notificationViewModel
                )
            }

            composable(Screen.CreateBudget.route) {
                LaunchedEffect(Unit) {
                    appBarState = AppBarState(title = "Nuevo presupuesto", showBack = true)
                    categoryViewModel.fetchCategories(userId)
                }
                CreateBudgetScreen(
                    userId = userId,
                    categoryState = categoryViewModel.categoryState,
                    viewModel = budgetViewModel,
                    onCancel = { navController.popBackStack() }
                )
            }

            composable("edit_budget/{id}") { backStackEntry ->
                val idArg = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                LaunchedEffect(idArg) {
                    appBarState = AppBarState(title = "Editar presupuesto", showBack = true)
                    idArg?.let { budgetViewModel.fetchDetailedBudgets(userId) }
                }

                val detailed = budgetViewModel.budgetState
                val budget = (detailed as? com.example.finanx.ui.viewmodel.BudgetState.Success)?.budgets?.firstOrNull { it.id == idArg }

                if (budget != null) {
                    EditBudgetScreen(
                        userId = userId,
                        budget = budget,
                        categoryState = categoryViewModel.categoryState,
                        viewModel = budgetViewModel,
                        onCancel = { navController.popBackStack() }
                    )
                } else {
                    // show loading or fallback
                    LaunchedEffect(Unit) { if (detailed is com.example.finanx.ui.viewmodel.BudgetState.Error) appBarState = AppBarState(title = "Presupuestos") }
                }
            }
        }

        // Global confirm-discard dialog used by create/edit flows
        if (showConfirmDiscard) {
            AlertDialog(
                onDismissRequest = { showConfirmDiscard = false; pendingPopAction = null },
                title = { Text("Descartar cambios?") },
                text = { Text("Si descartas, se perderá la información ingresada en el formulario.") },
                confirmButton = {
                    TextButton(onClick = {
                        showConfirmDiscard = false
                        pendingPopAction?.invoke()
                        pendingPopAction = null
                    }) { Text("Descartar") }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDiscard = false; pendingPopAction = null }) { Text("Seguir editando") }
                }
            )
        }
    }
}

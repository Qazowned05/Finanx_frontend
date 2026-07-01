package com.example.finanx.ui.screens.budget

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.screens.budget.recurrent.CreateRecurringDialog
import com.example.finanx.ui.screens.budget.recurrent.RecurringTab
import com.example.finanx.ui.screens.saving.goalIcons
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.viewmodel.*
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetResponse

private enum class BudgetFilter(val label: String) {
    ALL("Todos"),
    CANCELLED("Cancelados")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(
    userId: Long,
    bvm: BudgetViewModel,
    authViewModel: AuthViewModel,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {}
) {
    val scheduleViewModel: ScheduleViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    var budgetToEdit by remember { mutableStateOf<BudgetExecutionResponse?>(null) }

    LaunchedEffect(userId) {
        bvm.fetchDetailedBudgets(userId)
        bvm.fetchBudgets(userId)
        scheduleViewModel.fetchAll(userId)
        categoryViewModel.fetchCategories(userId)
    }

    val detailedState = bvm.budgetState
    val simpleState = bvm.simpleBudgetState
    val snackbarHostState = remember { SnackbarHostState() }

    var search by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(BudgetFilter.ALL) }
    var mainTab by remember { mutableIntStateOf(0) }
    var selectedBudgetId by remember { mutableStateOf<Long?>(null) }
    var showCreateBudget by remember { mutableStateOf(false) }
    var showCreateRecurring by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { if (mainTab == 0) showCreateBudget = true else showCreateRecurring = true },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()) {

            TabRow(
                selectedTabIndex = mainTab,
                containerColor = WarmBackground,
                contentColor = BrownPrimary
            ) {
                Tab(selected = mainTab == 0, onClick = { mainTab = 0 }, text = { Text("Presupuestos") })
                Tab(selected = mainTab == 1, onClick = { mainTab = 1 }, text = { Text("Recurrentes") })
            }

            if (mainTab == 0) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    placeholder = { Text("Buscar presupuesto") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )

                val filters = BudgetFilter.entries
                TabRow(
                    selectedTabIndex = filters.indexOf(selectedFilter),
                    containerColor = WarmBackground,
                    contentColor = BrownPrimary
                ) {
                    filters.forEach { filter ->
                        Tab(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            text = { Text(filter.label) }
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    when (selectedFilter) {

                        BudgetFilter.ALL -> when (detailedState) {
                            is BudgetState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = BrownPrimary
                                )
                            }
                            is BudgetState.Success -> {
                                val filtered = detailedState.budgets.filter { budget ->
                                    search.isBlank() ||
                                            budget.description?.contains(search, ignoreCase = true) == true ||
                                            budget.categoryName?.contains(search, ignoreCase = true) == true
                                }
                                if (filtered.isEmpty()) {
                                    Text(
                                        text = if (search.isEmpty()) "No tienes presupuestos activos"
                                        else "No se encontraron resultados",
                                        modifier = Modifier.align(Alignment.Center),
                                        color = StoneGray
                                    )
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        itemsIndexed(filtered) { index, budget ->
                                            BudgetCard(
                                                budget = budget,
                                                iconIndex = index % goalIcons.size,
                                                onCancel = { bvm.cancelBudget(budget.id ?: 0, userId) },
                                                onDisable = { bvm.disableBudget(budget.id ?: 0, userId) },
                                                onEdit = {
                                                    if (budget.state == BudgetExecutionResponse.State.ACTIVE &&
                                                        budget.health == BudgetExecutionResponse.Health.OK) {
                                                        budgetToEdit = budget
                                                    }
                                                }
                                            )
                                        }
                                        item { Spacer(modifier = Modifier.height(80.dp)) }
                                    }
                                }
                            }
                            is BudgetState.Error -> {
                                Text(
                                    text = detailedState.message,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                                )
                            }
                        }

                        BudgetFilter.CANCELLED -> when (simpleState) {
                            is SimpleBudgetState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = BrownPrimary
                                )
                            }
                            is SimpleBudgetState.Success -> {
                                val cancelled = simpleState.budgets.filter { budget ->
                                    budget.state == BudgetResponse.State.CANCELLED &&
                                            (search.isBlank() ||
                                                    budget.description?.contains(search, ignoreCase = true) == true ||
                                                    budget.categoryName?.contains(search, ignoreCase = true) == true)
                                }
                                if (cancelled.isEmpty()) {
                                    Text(
                                        text = if (search.isEmpty()) "No tienes presupuestos cancelados"
                                        else "No se encontraron resultados",
                                        modifier = Modifier.align(Alignment.Center),
                                        color = StoneGray
                                    )
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        itemsIndexed(cancelled) { index, budget ->
                                            BudgetCard(
                                                budget = budget,
                                                iconIndex = index % goalIcons.size,
                                                onCancel = {},
                                                onDisable = {},
                                                onEdit = {}
                                            )
                                        }
                                        item { Spacer(modifier = Modifier.height(80.dp)) }
                                    }
                                }
                            }
                            is SimpleBudgetState.Error -> {
                                Text(
                                    text = simpleState.message,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                RecurringTab(scheduleViewModel = scheduleViewModel, userId = userId)
            }
        }

        if (showCreateBudget) {
            CreateBudgetDialog(
                userId = userId,
                categoryState = categoryViewModel.categoryState,
                viewModel = bvm,
                onDismiss = { showCreateBudget = false }
            )
        }

        if (showCreateRecurring) {
            CreateRecurringDialog(
                userId = userId,
                categoryState = categoryViewModel.categoryState,
                viewModel = scheduleViewModel,
                onDismiss = { showCreateRecurring = false }
            )
        }
    }

    budgetToEdit?.let { budget ->
        EditBudgetDialog(
            userId = userId,
            budget = budget,
            categoryState = categoryViewModel.categoryState,
            viewModel = bvm,
            onDismiss = { budgetToEdit = null }
        )
    }
}
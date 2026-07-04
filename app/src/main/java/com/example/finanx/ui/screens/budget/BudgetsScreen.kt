package com.example.finanx.ui.screens.budget

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    viewModel: BudgetViewModel,
    onCreateBudget: () -> Unit,
    onEditBudget: (Long) -> Unit,
) {
    val scheduleViewModel: ScheduleViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()

    LaunchedEffect(userId) {
        viewModel.fetchDetailedBudgets(userId)
        viewModel.fetchBudgets(userId)
        scheduleViewModel.fetchAll(userId)
        categoryViewModel.fetchCategories(userId)
    }

    val detailedState = viewModel.budgetState
    val simpleState = viewModel.simpleBudgetState
    val snackbarHostState = remember { SnackbarHostState() }

    var search by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(BudgetFilter.ALL) }
    var selectedBudgetId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateBudget() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()) {
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
                                            onCancel = { viewModel.cancelBudget(budget.id ?: 0, userId) },
                                            onDisable = { viewModel.disableBudget(budget.id ?: 0, userId) },
                                            onEdit = {
                                                if (budget.state == BudgetExecutionResponse.State.ACTIVE &&
                                                    budget.health == BudgetExecutionResponse.Health.OK) {
                                                    budget.id?.let { id -> onEditBudget(id) }
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
        }

        if (showCreateBudget) {
            CreateBudgetDialog(
                userId = userId,
                categoryState = categoryViewModel.categoryState,
                viewModel = viewModel,
                onDismiss = { showCreateBudget = false }
            )
        }
    }

    
}
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.finanx.ui.screens.saving.goalIcons
import com.example.finanx.ui.components.SpotlightOverlay
import com.example.finanx.ui.components.TutorialPreferences
import com.example.finanx.ui.components.tutorialTarget

import com.example.finanx.ui.viewmodel.*
import org.openapitools.client.models.BudgetExecutionResponse
import org.openapitools.client.models.BudgetResponse
import com.example.finanx.ui.components.TutorialController
import com.example.finanx.ui.components.TutorialStep
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private enum class BudgetFilter(val label: String) {
    ALL("Todos"),
    CANCELLED("Cancelados")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(
    userId: Long,
    viewModel: BudgetViewModel,
    scheduleViewModel: ScheduleViewModel,
    categoryViewModel: CategoryViewModel,
    onCreateBudget: () -> Unit,
    onEditBudget: (Long) -> Unit,
) {
    LaunchedEffect(userId) {
        viewModel.fetchDetailedBudgets(userId)
        viewModel.fetchBudgets(userId)
        scheduleViewModel.fetchAll(userId)
        categoryViewModel.fetchCategories(userId)
    }

    val detailedState = viewModel.budgetState
    val simpleState = viewModel.simpleBudgetState
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val targetBounds = remember { mutableStateMapOf<String, Rect>() }
    var tutorialController by remember { mutableStateOf<TutorialController?>(null) }

    LaunchedEffect(Unit) {
        val completed = TutorialPreferences.isCompleted(userId, "tutorial_presupuestos").first()
        if (!completed) {
            tutorialController = TutorialController(
                listOf(
                    TutorialStep("budget_overview", "Los presupuestos te permiten establecer límites de gasto mensual por categoría y recibir alertas cuando te acerques al límite."),
                    TutorialStep("new_budget_button", "Toca el botón + para crear tu primer presupuesto.")
                )
            ) {
                coroutineScope.launch { TutorialPreferences.markCompleted(userId, "tutorial_presupuestos") }
                tutorialController = null
            }
        }
    }

    var search by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(BudgetFilter.ALL) }
    var onlyAlertable by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateBudget() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.tutorialTarget("new_budget_button", targetBounds)
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
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                filters.forEach { filter ->
                    Tab(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        text = { Text(filter.label) }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Solo con alertas activas")
                Switch(
                    checked = onlyAlertable,
                    onCheckedChange = { onlyAlertable = it }
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedFilter) {

                    BudgetFilter.ALL -> when (detailedState) {
                        is BudgetState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        is BudgetState.Success -> {
                            val filtered = detailedState.budgets.filter { budget ->
                                val textMatches = search.isBlank() ||
                                        budget.description?.contains(
                                            search,
                                            ignoreCase = true
                                        ) == true ||
                                        budget.categoryName?.contains(
                                            search,
                                            ignoreCase = true
                                        ) == true
                                val alertMatches = !onlyAlertable || budget.alertable == true
                                textMatches && alertMatches
                            }
                            if (filtered.isEmpty()) {
                                Text(
                                    text = if (search.isEmpty()) "No tienes presupuestos activos"
                                    else "No se encontraron resultados",
                                    modifier = Modifier.align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                com.example.finanx.ui.components.RefreshableList(
                                    isRefreshing = (detailedState as com.example.finanx.ui.viewmodel.BudgetState.Success).isRefreshing,
                                    onRefresh = { viewModel.fetchDetailedBudgets(userId, force = true) },
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        itemsIndexed(filtered) { index, budget ->
                                            BudgetCard(
                                                budget = budget,
                                                iconIndex = index % goalIcons.size,
                                                onCancel = {
                                                    viewModel.cancelBudget(
                                                        budget.id ?: 0,
                                                        userId
                                                    )
                                                },
                                                onDisable = {
                                                    viewModel.disableBudget(
                                                        budget.id ?: 0,
                                                        userId
                                                    )
                                                },
                                                onEdit = {
                                                    if (budget.state == BudgetExecutionResponse.State.ACTIVE &&
                                                        budget.health == BudgetExecutionResponse.Health.OK
                                                    ) {
                                                        budget.id?.let { id -> onEditBudget(id) }
                                                    }
                                                }
                                            )
                                        }
                                        item { Spacer(modifier = Modifier.height(80.dp)) }
                                    }
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
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        is SimpleBudgetState.Success -> {
                            val cancelled = simpleState.budgets.filter { budget ->
                                val textMatches = (search.isBlank() ||
                                                budget.description?.contains(
                                                    search,
                                                    ignoreCase = true
                                                ) == true ||
                                                budget.categoryName?.contains(
                                                    search,
                                                    ignoreCase = true
                                                ) == true)
                                val alertMatches = !onlyAlertable || budget.alertable == true
                                budget.state == BudgetResponse.State.CANCELLED && textMatches && alertMatches
                            }
                            if (cancelled.isEmpty()) {
                                Text(
                                    text = if (search.isEmpty()) "No tienes presupuestos cancelados"
                                    else "No se encontraron resultados",
                                    modifier = Modifier.align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
    }

    LaunchedEffect(tutorialController, tutorialController?.currentIndex) {
        if (tutorialController == null) return@LaunchedEffect
        snapshotFlow { tutorialController?.currentStep?.canAdvance() ?: false }
            .drop(1)
            .distinctUntilChanged()
            .filter { it }
            .collect { tutorialController?.next() }
    }

    if (tutorialController != null) {
        val current = tutorialController?.currentStep
        SpotlightOverlay(
            targetBounds = current?.let { targetBounds[it.targetKey] },
            message = current?.message ?: "",
            isLastStep = tutorialController?.isLastStep ?: false,
            showNextButton = !(current?.isAutoGated ?: false),
            onNext = { tutorialController?.next() },
            onSkip = {
                coroutineScope.launch { TutorialPreferences.markCompleted(userId, "tutorial_presupuestos") }
                tutorialController = null
            }
        )
    }
}

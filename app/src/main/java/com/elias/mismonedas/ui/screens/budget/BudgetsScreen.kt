package com.elias.mismonedas.ui.screens.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.screens.saving.goalIcons
import com.elias.mismonedas.ui.components.LoadMoreEffect
import com.elias.mismonedas.ui.components.LoadMoreFooter
import com.elias.mismonedas.ui.components.RefreshableList
import com.elias.mismonedas.ui.components.SpotlightOverlay
import com.elias.mismonedas.ui.components.TutorialPreferences
import com.elias.mismonedas.ui.components.tutorialTarget

import com.elias.mismonedas.ui.viewmodel.*
import com.elias.mismonedas.ui.util.toReadableDate
import com.elias.mismonedas.ui.components.TutorialController
import com.elias.mismonedas.ui.components.TutorialStep
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.openapitools.client.models.BudgetHealth
import org.openapitools.client.models.BudgetState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(
    userId: Long,
    viewModel: BudgetViewModel,
    state: BudgetListUiState,
    scheduleViewModel: ScheduleViewModel,
    categoryViewModel: CategoryViewModel,
    onCreateBudget: () -> Unit,
    onEditBudget: (Long) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onStateFilter: (BudgetState?) -> Unit,
    onHealthFilter: (BudgetHealth?) -> Unit,
    onDateFilter: (LocalDate?, LocalDate?) -> Unit,
    onClearFilters: () -> Unit,
) {
    LaunchedEffect(userId) {
        viewModel.fetchDetailedBudgets(userId)
        viewModel.fetchBudgetPage(userId)
        scheduleViewModel.fetchAll(userId)
        categoryViewModel.fetchCategories()
    }

    val coroutineScope = rememberCoroutineScope()
    val targetBounds = remember { mutableStateMapOf<String, Rect>() }
    var tutorialController by remember { mutableStateOf<TutorialController?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val listState = rememberLazyListState()

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

    val hasActiveFilters = state.filterState != null ||
            state.filterHealth != null ||
            state.filterFrom != null ||
            state.filterTo != null

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateBudget,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.tutorialTarget("new_budget_button", targetBounds)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo")
            }
        }
    ) { _ ->
        Column(modifier = Modifier.fillMaxSize()) {
            BudgetFilterBar(
                selectedState = state.filterState,
                selectedHealth = state.filterHealth,
                filterDate = state.filterFrom,
                onStateSelected = onStateFilter,
                onHealthSelected = onHealthFilter,
                onDateClick = { showDatePicker = true },
                onClearDate = { onDateFilter(null, null) },
                onClearAll = onClearFilters,
                hasActiveFilters = hasActiveFilters
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading && state.budgets.isEmpty() -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    state.error != null && state.budgets.isEmpty() -> {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center).padding(16.dp)
                        )
                    }

                    else -> {
                        LoadMoreEffect(
                            listState = listState,
                            enabled = state.hasMore && state.budgets.isNotEmpty(),
                            itemCount = state.budgets.size,
                            onLoadMore = onLoadMore
                        )

                        RefreshableList(
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (state.budgets.isEmpty()) {
                                    item {
                                        Text(
                                            text = if (hasActiveFilters) "No se encontraron presupuestos"
                                            else "No tienes presupuestos activos",
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                } else {
                                    itemsIndexed(
                                        items = state.budgets,
                                        key = { index, budget -> budget.id ?: index.toLong() }
                                    ) { index, budget ->
                                        BudgetCard(
                                            budget = budget,
                                            iconIndex = index % goalIcons.size,
                                            onCancel = {
                                                viewModel.cancelBudget(budget.id ?: 0, userId)
                                            },
                                            onDisable = {
                                                viewModel.disableBudget(budget.id ?: 0, userId)
                                            },
                                            onEdit = {
                                                if (budget.state == BudgetState.ACTIVE &&
                                                    budget.health == BudgetHealth.OK
                                                ) {
                                                    budget.id?.let { id -> onEditBudget(id) }
                                                }
                                            }
                                        )
                                    }

                                    item {
                                        LoadMoreFooter(isLoadingMore = state.isLoadingMore)
                                    }

                                    item { Spacer(modifier = Modifier.height(80.dp)) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                        onDateFilter(date, date)
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
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

@Composable
private fun BudgetFilterBar(
    selectedState: BudgetState?,
    selectedHealth: BudgetHealth?,
    filterDate: LocalDate?,
    onStateSelected: (BudgetState?) -> Unit,
    onHealthSelected: (BudgetHealth?) -> Unit,
    onDateClick: () -> Unit,
    onClearDate: () -> Unit,
    onClearAll: () -> Unit,
    hasActiveFilters: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedState == null,
                    onClick = { onStateSelected(null) },
                    label = { Text("Todos") }
                )
            }
            items(BudgetState.entries.toList()) { option ->
                FilterChip(
                    selected = selectedState == option,
                    onClick = { onStateSelected(option) },
                    label = { Text(option.filterLabel()) }
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedHealth == null,
                    onClick = { onHealthSelected(null) },
                    label = { Text("Toda la salud") }
                )
            }
            items(BudgetHealth.entries.toList()) { option ->
                FilterChip(
                    selected = selectedHealth == option,
                    onClick = { onHealthSelected(option) },
                    label = { Text(option.filterLabel()) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = onDateClick,
                label = { Text(filterDate?.toReadableDate() ?: "Filtrar por fecha") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )

            if (filterDate != null) {
                IconButton(onClick = onClearDate) {
                    Icon(Icons.Default.Close, contentDescription = "Quitar fecha")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (hasActiveFilters) {
                TextButton(onClick = onClearAll) {
                    Text("Limpiar")
                }
            }
        }
    }
}

private fun BudgetState.filterLabel(): String = when (this) {
    BudgetState.ACTIVE -> "Activos"
    BudgetState.CANCELLED -> "Cancelados"
    BudgetState.DISABLED -> "Deshabilitados"
    BudgetState.FINALIZED -> "Finalizados"
}

private fun BudgetHealth.filterLabel(): String = when (this) {
    BudgetHealth.OK -> "Equilibrado"
    BudgetHealth.NEAR_LIMIT -> "Por agotar"
    BudgetHealth.EXCEEDED -> "Excedido"
    BudgetHealth.INACTIVE -> "Inactivo"
}

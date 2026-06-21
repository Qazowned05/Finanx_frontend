package com.example.finanx.ui.screens.budget

import android.app.Activity
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finanx.ui.components.ProfileMenuAction
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.viewmodel.*
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import org.openapitools.client.models.Update2Request
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(
    userId: Long,
    bvm: BudgetViewModel,
    authViewModel: AuthViewModel,
    onNavigateToProfile: () -> Unit = {}
) {
    val scheduleViewModel: ScheduleViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()

    LaunchedEffect(userId) {
        bvm.fetchBudgets(userId)
        scheduleViewModel.fetchAll(userId)
        categoryViewModel.fetchCategories(userId)
    }

    val state = bvm.budgetState
    val context = LocalContext.current
    val activity = context as? Activity

    var search by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    var mainTab by remember { mutableStateOf(0) } // 0 = Presupuestos, 1 = Recurrentes
    var showCreateBudget by remember { mutableStateOf(false) }
    var showCreateRecurring by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = { Text("Presupuestos", color = BrownPrimary, style = MaterialTheme.typography.titleLarge) },
                actions = {
                    ProfileMenuAction(
                        onMyProfile = onNavigateToProfile,
                        onLogout = { authViewModel.logout(activity) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmBackground)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { if (mainTab == 0) showCreateBudget = true else showCreateRecurring = true },
                containerColor = BrownPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

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

                val tabs = listOf("Todos", "ACTIVE", "DISABLED", "CANCELLED", "FINALIZED")
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = WarmBackground,
                    contentColor = BrownPrimary,
                    edgePadding = 16.dp
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    when (state) {
                        is BudgetState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrownPrimary)
                        }
                        is BudgetState.Success -> {
                            val filteredBudgets = state.budgets.filter { budget ->
                                val stateOk = when (selectedTab) {
                                    0 -> true
                                    1 -> budget.state == BudgetResponse.State.ACTIVE
                                    2 -> budget.state == BudgetResponse.State.DISABLED
                                    3 -> budget.state == BudgetResponse.State.CANCELLED
                                    else -> budget.state == BudgetResponse.State.FINALIZED
                                }
                                val searchOk = search.isBlank() ||
                                        budget.description?.contains(search, ignoreCase = true) == true ||
                                        budget.categoryName?.contains(search, ignoreCase = true) == true
                                stateOk && searchOk
                            }

                            if (filteredBudgets.isEmpty()) {
                                Text(
                                    text = if (search.isEmpty()) "No tienes presupuestos en esta categoría" else "No se encontraron resultados",
                                    modifier = Modifier.align(Alignment.Center),
                                    color = StoneGray
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(filteredBudgets) { budget ->
                                        BudgetCard(
                                            budget = budget,
                                            onCancel = { bvm.cancelBudget(budget.id ?: 0, userId) },
                                            onDisable = { bvm.disableBudget(budget.id ?: 0, userId) }
                                        )
                                    }
                                    item { Spacer(modifier = Modifier.height(80.dp)) }
                                }
                            }
                        }
                        is BudgetState.Error -> {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.Center).padding(16.dp)
                            )
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
}

@Composable
fun BudgetCard(budget: BudgetResponse, onCancel: () -> Unit, onDisable: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val health = budget.health?.value
    val healthColor = when (health) {
        "EXCEEDED" -> ExpenseRed
        "NEAR_LIMIT" -> GoldAccent
        else -> IncomeGreen
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(budget.description.orEmpty(), style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
                    Text(budget.categoryName.orEmpty(), style = MaterialTheme.typography.labelMedium, color = StoneGray)
                }
                Box(
                    modifier = Modifier.background(healthColor.copy(alpha = 0.12f), RoundedCornerShape(20.dp)).padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(health.orEmpty(), color = healthColor, style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Límite:", color = StoneGray, style = MaterialTheme.typography.bodyMedium)
                Text("S/ ${budget.limitAmount ?: BigDecimal.ZERO}", style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Estado:", color = StoneGray, style = MaterialTheme.typography.bodyMedium)
                Text(budget.state?.value.orEmpty(), color = BrownSecondary, style = MaterialTheme.typography.bodyMedium)
            }
            if (expanded && budget.state == BudgetResponse.State.ACTIVE) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDisable, modifier = Modifier.weight(1f)) {
                        Text("Deshabilitar", color = BrownSecondary)
                    }
                    OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                        Text("Cancelar", color = ExpenseRed)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBudgetDialog(
    userId: Long,
    categoryState: CategoryState,
    viewModel: BudgetViewModel,
    onDismiss: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var limitAmount by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var alertable by remember { mutableStateOf(true) }
    var percentAlertStr by remember { mutableStateOf("80") }
    var categoryExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusMonths(1)) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val actionState = viewModel.actionState

    LaunchedEffect(actionState) {
        if (actionState is BudgetActionState.Success) {
            viewModel.resetActionState()
            onDismiss()
        }
    }

    val parsedLimit = limitAmount.toBigDecimalOrNull()
    val parsedPercent = percentAlertStr.toIntOrNull()
    val canCreate = description.isNotBlank() && parsedLimit != null && selectedCategoryId != null && !endDate.isBefore(startDate)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo presupuesto", color = BrownPrimary) },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp).verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = limitAmount,
                    onValueChange = { limitAmount = it.replace(',', '.') },
                    label = { Text("Monto límite (S/)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (categoryState is CategoryState.Success) {
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = categoryState.categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                            categoryState.categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.name ?: "") },
                                    onClick = { selectedCategoryId = cat.id; categoryExpanded = false }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = startDate.format(formatter),
                    onValueChange = {},
                    label = { Text("Fecha inicio") },
                    modifier = Modifier.fillMaxWidth().clickable { showStartPicker = true },
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = endDate.format(formatter),
                    onValueChange = {},
                    label = { Text("Fecha fin") },
                    modifier = Modifier.fillMaxWidth().clickable { showEndPicker = true },
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp)
                )

                if (showStartPicker) {
                    DatePickerDialog(
                        context,
                        { _, year, month, day -> startDate = LocalDate.of(year, month + 1, day); showStartPicker = false },
                        startDate.year, startDate.monthValue - 1, startDate.dayOfMonth
                    ).show()
                }
                if (showEndPicker) {
                    DatePickerDialog(
                        context,
                        { _, year, month, day -> endDate = LocalDate.of(year, month + 1, day); showEndPicker = false },
                        endDate.year, endDate.monthValue - 1, endDate.dayOfMonth
                    ).show()
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Alertas", color = StoneGray)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = alertable, onCheckedChange = { alertable = it })
                }

                OutlinedTextField(
                    value = percentAlertStr,
                    onValueChange = { percentAlertStr = it.filter { c -> c.isDigit() }.take(3) },
                    label = { Text("Porcentaje alerta") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (actionState is BudgetActionState.Error) {
                    Text(actionState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val request = BudgetRequest(
                        alertable = alertable,
                        limitAmount = parsedLimit ?: BigDecimal.ZERO,
                        description = description,
                        userId = userId,
                        categoryId = selectedCategoryId ?: 0L,
                        start = startDate,
                        end = endDate,
                        percentAlert = parsedPercent
                    )
                    viewModel.createBudget(request, userId)
                },
                enabled = canCreate && actionState !is BudgetActionState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
            ) {
                if (actionState is BudgetActionState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Crear")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = StoneGray) }
        }
    )
}

@Composable
fun RecurringTab(scheduleViewModel: ScheduleViewModel, userId: Long) {
    val state = scheduleViewModel.recurringState

    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is RecurringState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrownPrimary)
            }
            is RecurringState.Success -> {
                if (state.transactionSchedules.isEmpty() && state.budgetSchedules.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Repeat, contentDescription = null, tint = BrownSecondary.copy(alpha = 0.4f), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Sin gastos o presupuestos fijos aún", color = BrownSecondary)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.transactionSchedules) { ts ->
                            RecurringTransactionCard(ts, onPause = { scheduleViewModel.pause(ts.id ?: 0, userId) }, onResume = { scheduleViewModel.resume(ts.id ?: 0, userId) }, onCancel = { scheduleViewModel.cancel(ts.id ?: 0, userId) })
                        }
                        items(state.budgetSchedules) { bs ->
                            RecurringBudgetCard(bs, onPause = { scheduleViewModel.pause(bs.id ?: 0, userId) }, onResume = { scheduleViewModel.resume(bs.id ?: 0, userId) }, onCancel = { scheduleViewModel.cancel(bs.id ?: 0, userId) })
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
            is RecurringState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun RecurringTransactionCard(ts: TransactionScheduleResponse, onPause: () -> Unit, onResume: () -> Unit, onCancel: () -> Unit) {
    val isPaused = ts.state == org.openapitools.client.models.ScheduleResponse.State.PAUSED
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(ts.categoryName ?: "Transacción recurrente", style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
                Text("S/ ${ts.amount ?: BigDecimal.ZERO}", style = MaterialTheme.typography.titleMedium, color = if (ts.transactionType?.value == "INCOME") IncomeGreen else ExpenseRed)
            }
            Text("Próxima ejecución: ${ts.nextRunAt?.toLocalDate() ?: "—"}", style = MaterialTheme.typography.labelMedium, color = StoneGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = if (isPaused) onResume else onPause, modifier = Modifier.weight(1f)) {
                    Icon(if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isPaused) "Reanudar" else "Pausar")
                }
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancelar", color = ExpenseRed)
                }
            }
        }
    }
}

@Composable
fun RecurringBudgetCard(bs: BudgetScheduleResponse, onPause: () -> Unit, onResume: () -> Unit, onCancel: () -> Unit) {
    val isPaused = bs.state == org.openapitools.client.models.ScheduleResponse.State.PAUSED
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(bs.description ?: bs.categoryName ?: "Presupuesto recurrente", style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
                Text("S/ ${bs.limitAmount ?: BigDecimal.ZERO}", style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
            }
            Text("Próxima ejecución: ${bs.nextRunAt?.toLocalDate() ?: "—"}", style = MaterialTheme.typography.labelMedium, color = StoneGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = if (isPaused) onResume else onPause, modifier = Modifier.weight(1f)) {
                    Icon(if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isPaused) "Reanudar" else "Pausar")
                }
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancelar", color = ExpenseRed)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecurringDialog(
    userId: Long,
    categoryState: CategoryState,
    viewModel: ScheduleViewModel,
    onDismiss: () -> Unit
) {
    var kind by remember { mutableStateOf(0) } // 0 = transacción, 1 = presupuesto
    var description by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var transactionType by remember { mutableStateOf(Update2Request.TransactionType.SPENT) }
    var paymentMethod by remember { mutableStateOf(Update2Request.PaymentMethod.CASH) }
    var alertable by remember { mutableStateOf(true) }
    var percentAlertStr by remember { mutableStateOf("80") }
    var recurrenceType by remember { mutableStateOf(RecurrenceRuleRequest.RecurrenceType.MONTHLY) }
    var interval by remember { mutableStateOf("1") }

    val context = LocalContext.current
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusYears(1)) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val actionState = viewModel.actionState
    LaunchedEffect(actionState) {
        if (actionState is RecurringActionState.Success) {
            viewModel.resetActionState()
            onDismiss()
        }
    }

    val parsedAmount = amountStr.toBigDecimalOrNull()
    val parsedInterval = interval.toIntOrNull() ?: 1
    val parsedPercent = percentAlertStr.toIntOrNull() ?: 0
    val canCreate = selectedCategoryId != null &&
            (if (kind == 0) parsedAmount != null else description.isNotBlank() && parsedAmount != null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo recurrente", color = BrownPrimary) },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(max = 460.dp).verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = kind == 0, onClick = { kind = 0 }, label = { Text("Transacción") })
                    FilterChip(selected = kind == 1, onClick = { kind = 1 }, label = { Text("Presupuesto") })
                }

                if (categoryState is CategoryState.Success) {
                    ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = !categoryExpanded }) {
                        OutlinedTextField(
                            value = categoryState.categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                            categoryState.categories.forEach { cat ->
                                DropdownMenuItem(text = { Text(cat.name ?: "") }, onClick = { selectedCategoryId = cat.id; categoryExpanded = false })
                            }
                        }
                    }
                }

                if (kind == 1) {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it.replace(',', '.') },
                    label = { Text(if (kind == 0) "Monto (S/)" else "Límite (S/)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (kind == 0) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = transactionType == Update2Request.TransactionType.SPENT, onClick = { transactionType = Update2Request.TransactionType.SPENT }, label = { Text("Gasto") })
                        FilterChip(selected = transactionType == Update2Request.TransactionType.INCOME, onClick = { transactionType = Update2Request.TransactionType.INCOME }, label = { Text("Ingreso") })
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Alertas", color = StoneGray)
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(checked = alertable, onCheckedChange = { alertable = it })
                    }
                    OutlinedTextField(
                        value = percentAlertStr,
                        onValueChange = { percentAlertStr = it.filter { c -> c.isDigit() }.take(3) },
                        label = { Text("Porcentaje alerta") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Text("Recurrencia", style = MaterialTheme.typography.titleMedium, color = BrownPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        RecurrenceRuleRequest.RecurrenceType.DAILY to "Diario",
                        RecurrenceRuleRequest.RecurrenceType.WEEKLY to "Semanal",
                        RecurrenceRuleRequest.RecurrenceType.MONTHLY to "Mensual",
                        RecurrenceRuleRequest.RecurrenceType.YEARLY to "Anual"
                    ).forEach { (value, label) ->
                        FilterChip(selected = recurrenceType == value, onClick = { recurrenceType = value }, label = { Text(label) })
                    }
                }

                OutlinedTextField(
                    value = interval,
                    onValueChange = { interval = it.filter { c -> c.isDigit() }.take(2) },
                    label = { Text("Cada cuántos periodos") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = startDate.format(formatter),
                    onValueChange = {},
                    label = { Text("Fecha inicio") },
                    modifier = Modifier.fillMaxWidth().clickable { showStartPicker = true },
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = endDate.format(formatter),
                    onValueChange = {},
                    label = { Text("Fecha fin") },
                    modifier = Modifier.fillMaxWidth().clickable { showEndPicker = true },
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp)
                )

                if (showStartPicker) {
                    DatePickerDialog(context, { _, y, m, d -> startDate = LocalDate.of(y, m + 1, d); showStartPicker = false }, startDate.year, startDate.monthValue - 1, startDate.dayOfMonth).show()
                }
                if (showEndPicker) {
                    DatePickerDialog(context, { _, y, m, d -> endDate = LocalDate.of(y, m + 1, d); showEndPicker = false }, endDate.year, endDate.monthValue - 1, endDate.dayOfMonth).show()
                }

                if (actionState is RecurringActionState.Error) {
                    Text(actionState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val catId = selectedCategoryId ?: return@Button
                    if (kind == 0) {
                        viewModel.createTransactionSchedule(
                            userId = userId,
                            categoryId = catId,
                            amount = parsedAmount ?: BigDecimal.ZERO,
                            paymentMethod = paymentMethod,
                            transactionType = transactionType,
                            recurrenceType = recurrenceType,
                            interval = parsedInterval,
                            start = startDate,
                            end = endDate
                        )
                    } else {
                        viewModel.createBudgetSchedule(
                            userId = userId,
                            categoryId = catId,
                            alertable = alertable,
                            percentAlert = parsedPercent,
                            limitAmount = parsedAmount ?: BigDecimal.ZERO,
                            description = description,
                            recurrenceType = recurrenceType,
                            interval = parsedInterval,
                            start = startDate,
                            end = endDate
                        )
                    }
                },
                enabled = canCreate && actionState !is RecurringActionState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
            ) {
                if (actionState is RecurringActionState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Crear")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = StoneGray) }
        }
    )
}
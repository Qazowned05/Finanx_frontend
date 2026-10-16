package com.elias.mismonedas.ui.screens.subscription

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.components.ErrorMessage
import com.elias.mismonedas.ui.components.LoadMoreEffect
import com.elias.mismonedas.ui.components.LoadMoreFooter
import com.elias.mismonedas.ui.components.RefreshableList
import com.elias.mismonedas.ui.util.toReadableDate
import com.elias.mismonedas.ui.util.toCurrency
import com.elias.mismonedas.ui.viewmodel.SubscriptionActionState
import com.elias.mismonedas.ui.viewmodel.SubscriptionUIState
import com.elias.mismonedas.ui.viewmodel.SubscriptionViewModel
import org.openapitools.client.models.PaymentResponse
import org.openapitools.client.models.PaymentState
import org.openapitools.client.models.SubscriptionResponse
import org.openapitools.client.models.SubscriptionState
import java.time.Instant
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    userId: Long,
    viewModel: SubscriptionViewModel,
    onBack: () -> Unit
) {
    val state = viewModel.state
    val actionState = viewModel.actionState
    val listState = rememberLazyListState()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LaunchedEffect(actionState) {
        if (actionState is SubscriptionActionState.Success) {
            viewModel.resetActionState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Suscripción") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        when (state) {
            is SubscriptionUIState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is SubscriptionUIState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    ErrorMessage(
                        message = (state as SubscriptionUIState.Error).message,
                        onRetry = { viewModel.load() }
                    )
                }
            }
            is SubscriptionUIState.Loaded -> {
                val data = state as SubscriptionUIState.Loaded
                val hasActiveFilters = data.filterState != null ||
                        data.filterFrom != null ||
                        data.filterTo != null

                LoadMoreEffect(
                    listState = listState,
                    enabled = data.hasMore && !data.isLoadingPayments && !data.isRefreshing,
                    itemCount = data.payments.size,
                    onLoadMore = { viewModel.loadMore() }
                )

                RefreshableList(
                    isRefreshing = data.isRefreshing,
                    onRefresh = { viewModel.refresh() },
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            PlanCard(plan = data.plan, onCancel = {
                                data.plan?.id?.let { viewModel.cancelSubscription(it) }
                            })
                        }

                        item {
                            PaymentHistoryFilterBar(
                                selectedState = data.filterState,
                                hasDateFilter = data.filterFrom != null || data.filterTo != null,
                                onStateSelected = { viewModel.setStateFilter(it) },
                                onDateClick = { showDatePicker = true },
                                onClear = { viewModel.clearFilters() }
                            )
                        }

                        item {
                            Text("Historial de pagos", style = MaterialTheme.typography.titleSmall)
                        }

                        when {
                            data.paymentsError != null -> {
                                item {
                                    ErrorMessage(
                                        message = data.paymentsError,
                                        onRetry = { viewModel.refresh() }
                                    )
                                }
                            }

                            data.isLoadingPayments -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            data.payments.isEmpty() -> {
                                item {
                                    Text(
                                        text = if (hasActiveFilters) {
                                            "No hay pagos con esos filtros"
                                        } else {
                                            "Todavía no hay pagos registrados"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            else -> {
                                items(data.payments, key = { it.id ?: 0 }) { payment ->
                                    PaymentHistoryItem(payment)
                                }
                            }
                        }

                        item {
                            LoadMoreFooter(isLoadingMore = data.isLoadingMore)
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
                        viewModel.setDateFilter(date, date)
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
}

@Composable
private fun PaymentHistoryFilterBar(
    selectedState: PaymentState?,
    hasDateFilter: Boolean,
    onStateSelected: (PaymentState?) -> Unit,
    onDateClick: () -> Unit,
    onClear: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = selectedState == null,
                    onClick = { onStateSelected(null) },
                    label = { Text("Todos") }
                )
            }
            items(PaymentState.entries, key = { it.name }) { paymentState ->
                FilterChip(
                    selected = selectedState == paymentState,
                    onClick = { onStateSelected(paymentState) },
                    label = { Text(paymentState.value) }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = hasDateFilter,
                onClick = onDateClick,
                label = { Text("Fecha") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )

            if (selectedState != null || hasDateFilter) {
                TextButton(onClick = onClear) {
                    Text("Limpiar")
                }
            }
        }
    }
}

@Composable
private fun PlanCard(
    plan: SubscriptionResponse?,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Plan actual", style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
            Spacer(Modifier.height(4.dp))
            Text(
                plan?.plan?.value ?: "FREE",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            if (plan != null) {
                Spacer(Modifier.height(8.dp))
                Text("Ciclo: ${plan.billingCycle?.value ?: "—"}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text("Estado: ${plan.state?.value ?: "—"}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                plan.amount?.let {
                    Text("Costo: ${it.toCurrency(plan.currency ?: "PEN")}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                plan.currentPeriodEnd?.let {
                    Text("Vence: ${it.toReadableDate()}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                if (plan.state == SubscriptionState.ACTIVE || plan.state == SubscriptionState.PENDING) {
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(onClick = onCancel) {
                        Text("Cancelar suscripción")
                    }
                }
            } else {
                Spacer(Modifier.height(8.dp))
                Text("Sin suscripción activa", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}

@Composable
private fun PaymentHistoryItem(payment: PaymentResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Receipt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(payment.state?.value ?: "—", fontWeight = FontWeight.SemiBold)
                Text(payment.createdAt.toReadableDate(), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            payment.amount?.let {
                Text(it.toCurrency(payment.currency ?: "PEN"), fontWeight = FontWeight.Bold)
            }
        }
    }
}

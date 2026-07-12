package com.elias.mismonedas.ui.screens.subscription

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.util.toCurrency
import com.elias.mismonedas.ui.util.toReadableDate
import com.elias.mismonedas.ui.viewmodel.SubscriptionActionState
import com.elias.mismonedas.ui.viewmodel.SubscriptionState
import com.elias.mismonedas.ui.viewmodel.SubscriptionViewModel
import org.openapitools.client.models.PaymentResponse
import org.openapitools.client.models.SubscriptionResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    userId: Long,
    viewModel: SubscriptionViewModel,
    onBack: () -> Unit
) {
    val state = viewModel.state
    val actionState = viewModel.actionState

    LaunchedEffect(Unit) {
        viewModel.load(userId)
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
            is SubscriptionState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is SubscriptionState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text((state as SubscriptionState.Error).message, color = MaterialTheme.colorScheme.error)
                }
            }
            is SubscriptionState.Loaded -> {
                val data = state as SubscriptionState.Loaded
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { PlanCard(plan = data.plan, onCancel = {
                        data.plan?.id?.let { viewModel.cancelSubscription(it, userId) }
                    }) }

                    if (data.payments.isNotEmpty()) {
                        item {
                            Text("Historial de pagos", style = MaterialTheme.typography.titleSmall)
                        }
                        items(data.payments, key = { it.id ?: 0 }) { payment ->
                            PaymentHistoryItem(payment)
                        }
                    }
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
                if (plan.state == SubscriptionResponse.State.ACTIVE || plan.state == SubscriptionResponse.State.PENDING) {
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

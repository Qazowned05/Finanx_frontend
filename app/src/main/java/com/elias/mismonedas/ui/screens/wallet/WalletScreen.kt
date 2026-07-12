package com.elias.mismonedas.ui.screens.wallet

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
import com.elias.mismonedas.ui.components.EmptyStateCard
import com.elias.mismonedas.ui.util.toCurrency
import com.elias.mismonedas.ui.viewmodel.WalletActionState
import com.elias.mismonedas.ui.viewmodel.WalletListState
import com.elias.mismonedas.ui.viewmodel.WalletViewModel
import org.openapitools.client.models.WalletResponse
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    userId: Long,
    viewModel: WalletViewModel,
    onBack: () -> Unit
) {
    val state = viewModel.walletListState
    val actionState = viewModel.actionState
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadWallets(userId)
    }

    LaunchedEffect(actionState) {
        if (actionState is WalletActionState.Success) {
            viewModel.resetActionState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Billeteras") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Crear") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is WalletListState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is WalletListState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadWallets(userId) }) {
                            Text("Reintentar")
                        }
                    }
                }
                is WalletListState.Success -> {
                    if (state.wallets.isEmpty()) {
                        EmptyStateCard(
                            icon = Icons.Default.AccountBalanceWallet,
                            title = "Sin billeteras",
                            subtitle = "Crea tu primera billetera para empezar",
                            actionLabel = "Crear",
                            onAction = { showCreateDialog = true }
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.wallets, key = { it.id ?: 0 }) { wallet ->
                                WalletCard(
                                    wallet = wallet,
                                    onSetPrimary = {
                                        wallet.id?.let { id ->
                                            viewModel.setPrimary(userId, id, wallet.currencyCode ?: "PEN")
                                        }
                                    },
                                    onDisable = {
                                        wallet.id?.let { viewModel.disableWallet(userId, it) }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateWalletDialog(
            onConfirm = { currencyCode ->
                viewModel.createWallet(userId, currencyCode)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }
}

@Composable
private fun WalletCard(
    wallet: WalletResponse,
    onSetPrimary: () -> Unit,
    onDisable: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currency = wallet.currencyCode ?: "PEN"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (wallet.primary == true)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (wallet.primary == true) Icons.Default.AccountBalanceWallet
                else Icons.Default.MonetizationOn,
                contentDescription = null,
                tint = if (wallet.primary == true) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(currency, fontWeight = FontWeight.Bold)
                    if (wallet.primary == true) {
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Principal",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    (wallet.balance ?: BigDecimal.ZERO).toCurrency(currency),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    if (wallet.primary != true) {
                        DropdownMenuItem(
                            text = { Text("Establecer como principal") },
                            onClick = { expanded = false; onSetPrimary() }
                        )
                    }
                    if (wallet.primary != true) {
                        DropdownMenuItem(
                            text = { Text("Deshabilitar", color = MaterialTheme.colorScheme.error) },
                            onClick = { expanded = false; onDisable() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateWalletDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCurrency by remember { mutableStateOf("PEN") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva billetera") },
        text = {
            Column {
                Text("Selecciona la moneda:", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                val currencies = listOf(
                    "PEN" to "PEN — Sol peruano",
                    "USD" to "USD — Dólar estadounidense",
                    "EUR" to "EUR — Euro",
                    "COP" to "COP — Peso colombiano",
                    "MXN" to "MXN — Peso mexicano",
                    "CLP" to "CLP — Peso chileno",
                    "ARS" to "ARS — Peso argentino",
                    "BOB" to "BOB — Boliviano"
                )
                currencies.forEach { (code, display) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedCurrency == code,
                            onClick = { selectedCurrency = code }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(display)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedCurrency) }) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

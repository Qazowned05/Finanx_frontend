package com.example.finanx.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.ui.components.ProfileMenuAction
import com.example.finanx.ui.components.TransactionRecordItem
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.util.toPen
import com.example.finanx.ui.viewmodel.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onNewTransaction: () -> Unit,
    onOpenTransactions: () -> Unit,
    onNotifications: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis finanzas",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = onNotifications
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones"
                        )
                    }

                    ProfileMenuAction(
                        onMyProfile = onProfile,
                        onLogout = onLogout
                    )
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                LoadingContent(
                    modifier = Modifier.padding(paddingValues)
                )
            }

            state.error != null -> {
                ErrorContent(
                    message = state.error.orEmpty(),
                    onRetry = onRetry,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                HomeContent(
                    state = state,
                    onNewTransaction = onNewTransaction,
                    onOpenTransactions = onOpenTransactions,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onRetry
        ) {
            Text("Reintentar")
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onNewTransaction: () -> Unit,
    onOpenTransactions: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Hola, ${state.userName}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            BalanceCard(
                balance = state.balance.toPen()
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeSummaryCard(
                    title = "Ingresos del mes",
                    amount = state.monthlyIncome.toPen(),
                    amountColor = IncomeGreen,
                    modifier = Modifier.weight(1f)
                )

                HomeSummaryCard(
                    title = "Gastos del mes",
                    amount = state.monthlyExpenses.toPen(),
                    amountColor = ExpenseRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            HomeActions(
                onNewTransaction = onNewTransaction,
                onOpenTransactions = onOpenTransactions
            )
        }

        item {
            Text(
                text = "Movimientos recientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (state.recentTransactions.isEmpty()) {
            item {
                Text(
                    text = "Todavía no tienes movimientos.",
                    color = TextGray
                )
            }
        } else {
            items(
                items = state.recentTransactions,
                key = { transaction ->
                    transaction.id
                }
            ) { transaction ->
                TransactionRecordItem(
                    transaction = transaction
                )
            }
        }
    }
}

@Composable
private fun BalanceCard(
    balance: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Saldo actual",
                color = TextGray
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = balance,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Calculado con tus movimientos activos",
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )
        }
    }
}

@Composable
private fun HomeActions(
    onNewTransaction: () -> Unit,
    onOpenTransactions: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onNewTransaction,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text("Nuevo")
        }

        OutlinedButton(
            onClick = onOpenTransactions,
            modifier = Modifier.weight(1f)
        ) {
            Text("Ver movimientos")
        }
    }
}

@Composable
private fun HomeSummaryCard(
    title: String,
    amount: String,
    amountColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = amount,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}
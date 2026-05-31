package com.example.finanx.ui.screens.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.data.model.BudgetResponse
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.viewmodel.BudgetState
import com.example.finanx.ui.viewmodel.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(userId: Long, viewModel: BudgetViewModel) {
    LaunchedEffect(userId) {
        viewModel.fetchBudgets(userId)
    }

    val state = viewModel.budgetState

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Presupuestos", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (state) {
                is BudgetState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is BudgetState.Success -> {
                    if (state.budgets.isEmpty()) {
                        Text("No tienes presupuestos creados", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.budgets) { budget ->
                                BudgetCard(budget)
                            }
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
    }
}

@Composable
fun BudgetCard(budget: BudgetResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(budget.description, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = BluePrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Límite:", color = Color.Gray)
                Text("PEN ${budget.limitAmount}", fontWeight = FontWeight.SemiBold)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Estado:", color = Color.Gray)
                Text(budget.state, color = if (budget.state == "ACTIVE") Color(0xFF2F855A) else Color.Gray)
            }
        }
    }
}

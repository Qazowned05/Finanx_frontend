package com.example.finanx.ui.screens.budget

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.components.ProfileMenuAction
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.BudgetState
import com.example.finanx.ui.viewmodel.BudgetViewModel
import org.openapitools.client.models.BudgetResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(userId: Long, viewModel: BudgetViewModel, authViewModel: AuthViewModel) {
    LaunchedEffect(userId) {
        viewModel.fetchBudgets(userId)
    }

    val state = viewModel.budgetState
    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Presupuestos", fontWeight = FontWeight.Bold) },
                actions = {
                    ProfileMenuAction(
                        onMyProfile = { /* TODO: navegar a perfil */ },
                        onLogout = { authViewModel.logout(activity) }
                    )
                }
            )
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
                            items(count = state.budgets.size) { index ->
                                BudgetCard(state.budgets[index])
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
            Text(budget.description.orEmpty(), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = BluePrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Límite:", color = Color.Gray)
                Text("PEN ${budget.limitAmount ?: 0}", fontWeight = FontWeight.SemiBold)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Estado:", color = Color.Gray)
                val state = budget.state?.value
                Text(state.orEmpty(), color = if (state == "ACTIVE") Color(0xFF2F855A) else Color.Gray)
            }
        }
    }
}

package com.example.finanx.ui.screens.budget

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.components.ProfileMenuAction
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.theme.LightBackground
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.BudgetState
import com.example.finanx.ui.viewmodel.BudgetViewModel
import org.openapitools.client.models.BudgetResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListScreen(
    userId: Long,
    viewModel: BudgetViewModel,
    authViewModel: AuthViewModel,
    onNavigateToProfile: () -> Unit = {}
) {
    LaunchedEffect(userId) {
        viewModel.fetchBudgets(userId)
    }

    val state = viewModel.budgetState
    val context = LocalContext.current
    val activity = context as? Activity

    var search by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = LightBackground,
        topBar = {
            TopAppBar(
                title = { Text("Presupuestos", fontWeight = FontWeight.Bold) },
                actions = {
                    ProfileMenuAction(
                        onMyProfile = onNavigateToProfile,
                        onLogout = { authViewModel.logout(activity) }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = BluePrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Presupuesto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar presupuesto") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            val tabs = listOf("Todos", "ACTIVE", "DISABLED", "CANCELLED", "FINALIZED")
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (state) {
                    is BudgetState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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

                            val searchOk = budget.description
                                ?.contains(search, ignoreCase = true) ?: false

                            stateOk && searchOk
                        }

                        if (filteredBudgets.isEmpty()) {
                            Text(
                                text = if (search.isEmpty()) "No tienes presupuestos en esta categoría" else "No se encontraron resultados",
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Gray
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(filteredBudgets) { budget ->
                                    BudgetCard(budget = budget)
                                }

                                item {
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Button(
                                        onClick = { showDialog = true },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null)
                                        Spacer(modifier = Modifier.padding(4.dp))
                                        Text("Nuevo Presupuesto")
                                    }
                                    Spacer(modifier = Modifier.height(80.dp))
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

        if (showDialog) {
            CreateBudgetDialog(
                onDismiss = { showDialog = false }
            )
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
                val stateValue = budget.state?.value
                Text(stateValue.orEmpty(), color = if (stateValue == "ACTIVE") Color(0xFF2F855A) else Color.Gray)
            }
        }
    }
}

@Composable
fun CreateBudgetDialog(onDismiss: () -> Unit) {
    var description by remember { mutableStateOf("") }
    var limitAmount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Presupuesto", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
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
                    onValueChange = { limitAmount = it },
                    label = { Text("Monto Límite") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}
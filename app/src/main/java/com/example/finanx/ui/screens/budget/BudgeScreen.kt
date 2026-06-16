package com.example.finanx.ui.screens.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.openapitools.client.models.BudgetResponse
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.theme.LightBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen() {

    var search by remember { mutableStateOf("") }

    var selectedTab by remember { mutableStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }

    val budgets = listOf(

        BudgetResponse(
            id = 1,
            description = "Comida",
            limitAmount = java.math.BigDecimal(800),
            state = BudgetResponse.State.ACTIVE
        ),

        BudgetResponse(
            id = 2,
            description = "Servicios",
            limitAmount = java.math.BigDecimal(600),
            state = BudgetResponse.State.ACTIVE
        ),

        BudgetResponse(
            id = 3,
            description = "Ahorros",
            limitAmount = java.math.BigDecimal(1000),
            state = BudgetResponse.State.ACTIVE
        ),

        BudgetResponse(
            id = 4,
            description = "Salidas",
            limitAmount = java.math.BigDecimal(350),
            state = BudgetResponse.State.DISABLED
        )

    )

    Scaffold(

        containerColor = LightBackground,

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Presupuestos",
                        fontWeight = FontWeight.Bold
                    )

                }

            )

        },

        floatingActionButton = {

            FloatingActionButton(

                onClick = {

                    showDialog = true

                },

                containerColor = BluePrimary

            ) {

                Icon(
                    Icons.Default.Add,
                    contentDescription = null
                )

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
                onValueChange = {
                    search = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = {
                    Text("Buscar presupuesto")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )

            val tabs = listOf(
                "Todos",
                "ACTIVE",
                "DISABLED",
                "CANCELLED",
                "FINALIZED"
            )

            TabRow(
                selectedTabIndex = selectedTab
            ) {

                tabs.forEachIndexed { index, title ->

                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                        },
                        text = {
                            Text(title)
                        }
                    )

                }

            }

            val filteredBudgets = budgets.filter {

                val stateOk = when (selectedTab) {

                    0 -> true

                    1 -> it.state == BudgetResponse.State.ACTIVE

                    2 -> it.state == BudgetResponse.State.DISABLED

                    3 -> it.state == BudgetResponse.State.CANCELLED

                    else -> it.state == BudgetResponse.State.FINALIZED

                }

                val searchOk =
                    it.description
                        ?.contains(search, ignoreCase = true)
                        ?: false

                stateOk && searchOk

            }

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize(),

                contentPadding = PaddingValues(16.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {

                items(filteredBudgets) { budget ->

                    BudgetCard(
                        budget = budget
                    )

                }

                item {

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    Button(

                        onClick = {

                            showDialog = true

                        },

                        modifier = Modifier.fillMaxWidth(),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary
                        )

                    ) {

                        Icon(
                            Icons.Default.Add,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.padding(4.dp)
                        )

                        Text(
                            "Nuevo Presupuesto"
                        )

                    }

                    Spacer(
                        modifier = Modifier.height(80.dp)
                    )

                }

            }

        }

        if (showDialog) {

            CreateBudgetDialog(

                onDismiss = {

                    showDialog = false

                }

            )

        }
    }
    }

@Composable
fun CreateBudgetDialog(onDismiss: () -> Unit) {
    var description by remember { mutableStateOf("") }
    var limitAmount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Nuevo Presupuesto")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = limitAmount,
                    onValueChange = { limitAmount = it },
                    label = { Text("Monto Límite") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancelar")
            }
        }
    )
}
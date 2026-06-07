package com.example.finanx.ui.screens.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finanx.ui.components.SegmentedButton
import com.example.finanx.ui.viewmodel.CategoryState
import com.example.finanx.ui.viewmodel.TransactionViewModel
import org.openapitools.client.models.CategoryResponse
import org.openapitools.client.models.TransactionRequest
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel,
    userId: Long
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionRequest.Type.SPENT.value) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<CategoryResponse?>(null) }

    LaunchedEffect(key1 = userId) {
        viewModel.fetchCategories(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Transacción") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Tipo", style = MaterialTheme.typography.bodyLarge)
            SegmentedButton(items = listOf("Gasto", "Ingreso")) { index ->
                type = if (index == 0) TransactionRequest.Type.SPENT.value else TransactionRequest.Type.INCOME.value
            }

            when (val state = viewModel.categoryState) {
                is CategoryState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is CategoryState.Success -> {
                    val categories = state.categories
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCategory?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name ?: "") },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                is CategoryState.Error -> {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amountDecimal = amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    selectedCategory?.id?.let {
                        viewModel.createTransaction(
                            userId = userId,
                            description = description,
                            amount = amountDecimal,
                            type = type,
                            categoryId = it,
                            paymentMethod = TransactionRequest.PaymentMethod.CASH.value
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = description.isNotBlank() && amount.toBigDecimalOrNull() != null && selectedCategory != null
            ) {
                Text("GUARDAR TRANSACCIÓN")
            }
        }
    }
}

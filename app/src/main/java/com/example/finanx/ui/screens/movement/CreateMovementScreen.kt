package com.example.finanx.ui.screens.movement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.elias.finanx.dto.category.CategoryResponse
import com.example.finanx.ui.components.SegmentedButton
import com.example.finanx.ui.viewmodel.CategoryState
import com.example.finanx.ui.viewmodel.MovementViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMovementScreen(
    navController: NavController,
    viewModel: MovementViewModel,
    userId: Long
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("EXPENSE") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<CategoryResponse?>(null) }

    // Cargar categorías al entrar en la pantalla
    LaunchedEffect(key1 = userId) {
        viewModel.fetchCategories(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Movimiento") },
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
                type = if (index == 0) "EXPENSE" else "INCOME"
                selectedCategory = null // Resetear categoría al cambiar
            }

            // Selector de Categoría
            when (val state = viewModel.categoryState) {
                is CategoryState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is CategoryState.Success -> {
                    val filteredCategories = state.categories.filter { it.type == type }
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
                            filteredCategories.forEach { category ->
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
                        viewModel.createMovement(
                            userId = userId,
                            description = description,
                            amount = amountDecimal,
                            type = type,
                            categoryId = it,
                            paymentMethod = "CASH"
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = description.isNotBlank() && amount.toBigDecimalOrNull() != null && selectedCategory != null
            ) {
                Text("GUARDAR MOVIMIENTO")
            }
        }
    }
}

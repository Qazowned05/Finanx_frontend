package com.example.finanx.ui.screens.category

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.CategoryState
import com.example.finanx.ui.viewmodel.CategoryViewModel
import org.openapitools.client.models.CategoryResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    userId: Long,
    viewModel: CategoryViewModel,
    authViewModel: AuthViewModel
) {
    LaunchedEffect(userId) {
        viewModel.fetchCategories(userId)
    }

    val state = viewModel.categoryState
    val context = LocalContext.current
    val activity = context as? Activity

    var showCreateDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<CategoryResponse?>(null) }

    // Snackbar para mensajes de éxito
    val snackbarHostState = remember { SnackbarHostState() }
    val actionSuccess = viewModel.actionSuccess
    LaunchedEffect(actionSuccess) {
        if (actionSuccess != null) {
            snackbarHostState.showSnackbar(actionSuccess)
            viewModel.clearActionSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mis Categorías", fontWeight = FontWeight.Bold) },
                actions = {
                    ProfileMenuAction(
                        onMyProfile = { },
                        onLogout = { authViewModel.logout(activity) }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = BluePrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear categoría", tint = Color.White)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (state) {
                is CategoryState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CategoryState.Success -> {
                    if (state.categories.isEmpty()) {
                        Text(
                            "No tienes categorías creadas",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(count = state.categories.size) { index ->
                                CategoryCard(
                                    category = state.categories[index],
                                    onEdit = { categoryToEdit = state.categories[index] },
                                    onDisable = { viewModel.disableCategory(userId, state.categories[index].id!!) }
                                )
                            }
                        }
                    }
                }
                is CategoryState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
            }
        }
    }

    // Dialog para crear categoría
    if (showCreateDialog) {
        CategoryFormDialog(
            title = "Nueva Categoría",
            onConfirm = { name, description ->
                viewModel.createCategory(userId, name, description)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }

    // Dialog para editar categoría
    categoryToEdit?.let { category ->
        CategoryFormDialog(
            title = "Editar Categoría",
            initialName = category.name.orEmpty(),
            initialDescription = category.description.orEmpty(),
            onConfirm = { name, description ->
                viewModel.updateCategory(userId, category.id!!, name, description)
                categoryToEdit = null
            },
            onDismiss = { categoryToEdit = null }
        )
    }
}

@Composable
fun CategoryCard(
    category: CategoryResponse,
    onEdit: () -> Unit,
    onDisable: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    category.name.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BluePrimary
                )
                if (!category.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(category.description!!, color = Color.Gray, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                val active = category.active == true
                Text(
                    if (active) "Activa" else "Inactiva",
                    color = if (active) Color(0xFF2F855A) else Color.Gray,
                    fontSize = 12.sp
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = BluePrimary)
            }
            IconButton(onClick = onDisable) {
                Icon(Icons.Default.Delete, contentDescription = "Desactivar", tint = Color.Red)
            }
        }
    }
}

@Composable
fun CategoryFormDialog(
    title: String,
    initialName: String = "",
    initialDescription: String = "",
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name, description) },
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
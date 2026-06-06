package com.example.finanx.ui.screens.category

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

val categoryColors = listOf(
    Color(0xFF6C63FF), Color(0xFFFF6584), Color(0xFF43C6AC),
    Color(0xFFF7971E), Color(0xFF4776E6), Color(0xFFFF5F6D),
    Color(0xFF11998E), Color(0xFFFC5C7D), Color(0xFF6A82FB)
)

val categoryIcons = listOf(
    Icons.Default.ShoppingCart, Icons.Default.Home, Icons.Default.DirectionsCar,
    Icons.Default.Restaurant, Icons.Default.HealthAndSafety, Icons.Default.School,
    Icons.Default.Flight, Icons.Default.Sports, Icons.Default.Category
)

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
    var categorySelected by remember { mutableStateOf<CategoryResponse?>(null) }
    var categoryToDisable by remember { mutableStateOf<CategoryResponse?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val actionSuccess = viewModel.actionSuccess
    LaunchedEffect(actionSuccess) {
        if (actionSuccess != null) {
            snackbarHostState.showSnackbar(actionSuccess)
            viewModel.clearActionSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color(0xFFEEF2FF),
            topBar = {
                TopAppBar(
                    title = { Text("Categorías", fontWeight = FontWeight.Bold, color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6B8FD6)
                    ),
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
                    containerColor = BluePrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva categoría", tint = Color.White)
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
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Category,
                                    contentDescription = null,
                                    tint = Color(0xFFCBD5E0),
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Sin categorías", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Toca + para agregar una", color = Color.Gray, fontSize = 13.sp)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(count = state.categories.size) { index ->
                                    val category = state.categories[index]
                                    CategoryCard(
                                        category = category,
                                        colorIndex = index % categoryColors.size,
                                        iconIndex = index % categoryIcons.size,
                                        isSelected = categorySelected?.id == category.id,
                                        onLongPress = { categorySelected = category },
                                        onDismiss = { categorySelected = null }
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



    }

    // Menu de opciones al mantener presionado
    categorySelected?.let { category ->
        val active = category.active == true
        AlertDialog(
            onDismissRequest = { categorySelected = null },
            title = { Text(category.name.orEmpty(), fontWeight = FontWeight.Bold) },
            text = { Text("¿Qué deseas hacer con esta categoría?") },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            categoryToDisable = category
                            categorySelected = null
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Eliminar")
                    }
                    Button(
                        onClick = {
                            categoryToEdit = category
                            categorySelected = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { categorySelected = null }) { Text("Cancelar") }
            }
        )
    }

    // Dialogo de confirmacion de desactivar
    categoryToDisable?.let { category ->
        AlertDialog(
            onDismissRequest = { categoryToDisable = null },
            containerColor = Color.White,
            title = {
                Text(
                    "¿Eliminar esta categoría?",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            text = {
                Text(
                    "Estás a punto de eliminar \"${category.name.orEmpty()}\". " +
                            "Las transacciones existentes no se verán afectadas, pero esta categoría " +
                            "ya no estará disponible para nuevos registros.",
                    color = Color(0xFF4A5568)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.disableCategory(userId, category.id!!)
                        categoryToDisable = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Sí, eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDisable = null }) { Text("Cancelar") }
            }
        )
    }

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryCard(
    category: CategoryResponse,
    colorIndex: Int,
    iconIndex: Int,
    isSelected: Boolean,
    onLongPress: () -> Unit,
    onDismiss: () -> Unit
) {
    val active = category.active == true
    val accentColor = if (active) categoryColors[colorIndex] else Color(0xFFCBD5E0)
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFFFCDD2) else Color.White,
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { if (isSelected) onDismiss() },
                onLongClick = onLongPress
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcons[iconIndex],
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    category.name.orEmpty(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = if (isSelected) Color.Red else Color(0xFF1A202C)
                )
                if (!category.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        category.description!!,
                        color = Color(0xFF718096),
                        fontSize = 13.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (active) Color(0xFF2F855A) else Color(0xFFCBD5E0))
            )
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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
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
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
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
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.CategoryState
import com.example.finanx.ui.viewmodel.CategoryViewModel
import org.openapitools.client.models.CategoryResponse

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
            containerColor = WarmBackground,
            topBar = {
                TopAppBar(
                    title = { Text("Categorías", fontWeight = FontWeight.Bold, color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BrownPrimary
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
                    containerColor = BrownPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva categoría", tint = Color.White)
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                when (state) {
                    is CategoryState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrownPrimary)
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
                                    tint = StoneGray,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Sin categorías", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = BrownPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Toca + para agregar una", color = StoneGray, fontSize = 13.sp)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                val groupedCategories = state.categories.groupBy { it.type?.value ?: "SPENT" }

                                groupedCategories.forEach { (type, categories) ->
                                    // Barra discriminadora por tipo
                                    item {
                                        Text(
                                            text = if (type == "INCOME") "Ingresos" else "Gastos",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (type == "INCOME") IncomeGreen else ExpenseRed,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    if (type == "INCOME") IncomeGreen.copy(alpha = 0.08f)
                                                    else ExpenseRed.copy(alpha = 0.08f),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(vertical = 6.dp, horizontal = 12.dp)
                                        )
                                    }

                                    items(categories.size) { index ->
                                        val category = categories[index]
                                        CategoryCard(
                                            category = category,
                                            iconIndex = index % categoryIcons.size,
                                            isSelected = categorySelected?.id == category.id,
                                            onLongPress = { categorySelected = category },
                                            onDismiss = { categorySelected = null }
                                        )
                                    }
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
    categorySelected?.let { category ->
        AlertDialog(
            onDismissRequest = { categorySelected = null },
            title = { Text(category.name.orEmpty(), fontWeight = FontWeight.Bold, color = BrownPrimary) },
            text = { Text("¿Qué deseas hacer con esta categoría?", color = StoneGray) },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            categoryToDisable = category
                            categorySelected = null
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ExpenseRed)
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
                        colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { categorySelected = null }) { Text("Cancelar", color = StoneGray) }
            }
        )
    }

    categoryToDisable?.let { category ->
        AlertDialog(
            onDismissRequest = { categoryToDisable = null },
            containerColor = SurfaceWhite,
            title = {
                Text(
                    "¿Eliminar esta categoría?",
                    fontWeight = FontWeight.Bold,
                    color = ExpenseRed
                )
            },
            text = {
                Text(
                    "Estás a punto de eliminar \"${category.name.orEmpty()}\". " +
                            "Las transacciones existentes no se verán afectadas, pero esta categoría " +
                            "ya no estará disponible para nuevos registros.",
                    color = StoneGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.disableCategory(userId, category.id!!)
                        categoryToDisable = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed)
                ) {
                    Text("Sí, eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDisable = null }) { Text("Cancelar", color = StoneGray) }
            }
        )
    }

    if (showCreateDialog) {
        CategoryFormDialog(
            title = "Nueva Categoría",
            onConfirm = { name, description, selectedType ->
                viewModel.createCategory(userId, name, description, selectedType)
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
            initialType = category.type ?: org.openapitools.client.models.CategoryResponse.Type.SPENT,
            onConfirm = { name, description, selectedType ->
                val requestType = if (selectedType.value == "INCOME")
                    org.openapitools.client.models.CategoryRequest.Type.INCOME
                else
                    org.openapitools.client.models.CategoryRequest.Type.SPENT

                viewModel.updateCategory(userId, category.id!!, name, description, requestType)
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
    iconIndex: Int,
    isSelected: Boolean,
    onLongPress: () -> Unit,
    onDismiss: () -> Unit
) {
    val active = category.active == true
    val accentColor = if (active) IncomeGreen else StoneGray
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) ExpenseRed.copy(alpha = 0.08f) else SurfaceWhite,
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
                    color = if (isSelected) ExpenseRed else BrownSecondary
                )
                if (!category.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        category.description!!,
                        color = StoneGray,
                        fontSize = 13.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (active) IncomeGreen else StoneGray)
            )
        }
    }
}

@Composable
fun CategoryFormDialog(
    title: String,
    initialName: String = "",
    initialDescription: String = "",
    initialType: CategoryResponse.Type = CategoryResponse.Type.SPENT,
    onConfirm: (String, String, org.openapitools.client.models.CategoryRequest.Type) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }

    val initialRequestType = if (initialType.value == "INCOME") {
        org.openapitools.client.models.CategoryRequest.Type.INCOME
    } else {
        org.openapitools.client.models.CategoryRequest.Type.SPENT
    }

    var type by remember { mutableStateOf(initialRequestType) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold, color = BrownPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = type == org.openapitools.client.models.CategoryRequest.Type.SPENT,
                            onClick = { type = org.openapitools.client.models.CategoryRequest.Type.SPENT }
                        )
                        Text("Gasto", fontSize = 14.sp, color = ExpenseRed)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = type == org.openapitools.client.models.CategoryRequest.Type.INCOME,
                            onClick = { type = org.openapitools.client.models.CategoryRequest.Type.INCOME }
                        )
                        Text("Ingreso", fontSize = 14.sp, color = IncomeGreen)
                    }
                }

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
                onClick = { if (name.isNotBlank()) onConfirm(name, description, type) },
                colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary)
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = StoneGray) }
        }
    )
}

package com.example.finanx.ui.screens.category

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import com.example.finanx.ui.components.EmptyStateCard
import com.example.finanx.ui.components.FinanxOutlinedTextField
import com.example.finanx.ui.components.SpotlightOverlay
import com.example.finanx.ui.components.TutorialPreferences
import com.example.finanx.ui.components.tutorialTarget
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.viewmodel.AuthViewModel
import com.example.finanx.ui.viewmodel.CategoryState
import com.example.finanx.ui.viewmodel.CategoryViewModel
import com.example.finanx.ui.components.tutorial.TutorialController
import com.example.finanx.ui.components.tutorial.TutorialStep
import org.openapitools.client.models.CategoryResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
    authViewModel: AuthViewModel,
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    LaunchedEffect(userId) {
        viewModel.fetchCategories(userId)
    }

    val state = viewModel.categoryState

    var showCreateDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<CategoryResponse?>(null) }
    var categorySelected by remember { mutableStateOf<CategoryResponse?>(null) }
    var categoryToDisable by remember { mutableStateOf<CategoryResponse?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val targetBounds = remember { mutableStateMapOf<String, Rect>() }
    var tutorialController by remember { mutableStateOf<TutorialController?>(null) }

    LaunchedEffect(Unit) {
        val completed = TutorialPreferences.isCompleted(context, "tutorial_categorias").first()
        if (!completed) {
            tutorialController = TutorialController(
                listOf(
                    TutorialStep("new_category_button", "Crea una categoría para clasificar tus movimientos."),
                    TutorialStep("category_type_toggle", "Cada categoría es de gasto o de ingreso — no se puede cambiar después de creada."),
                    TutorialStep("category_list_item", "Toca una categoría para editar o eliminar.")
                )
            ) {
                coroutineScope.launch { TutorialPreferences.markCompleted(context, "tutorial_categorias") }
                tutorialController = null
            }
        }
    }

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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.tutorialTarget("new_category_button", targetBounds)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva categoría", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (state) {
                is CategoryState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
                }
                is CategoryState.Success -> {
                    if (state.categories.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp)
                        ) {
                            EmptyStateCard(
                                icon = Icons.Default.Category,
                                title = "Sin categorías",
                                subtitle = "Toca + para agregar una",
                                actionLabel = "Nueva categoría",
                                onAction = { showCreateDialog = true }
                            )
                        }
                    } else {
                        com.example.finanx.ui.components.RefreshableList(
                            isRefreshing = (state as? com.example.finanx.ui.viewmodel.CategoryState.Success)?.isRefreshing
                                ?: false,
                            onRefresh = { viewModel.fetchCategories(userId, force = true) },
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                val groupedCategories = state.categories.groupBy { it.type?.value ?: "SPENT" }

                                groupedCategories.forEach { (type, categories) ->

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
                                            onDismiss = { categorySelected = null },
                                            modifier = if (index == 0) {
                                                Modifier.tutorialTarget("category_list_item", targetBounds)
                                            } else {
                                                Modifier
                                            }
                                        )
                                    }
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
    categorySelected?.let { category ->
        AlertDialog(
            onDismissRequest = { categorySelected = null },
            title = { Text(category.name.orEmpty(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
            text = { Text("¿Qué deseas hacer con esta categoría?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { categorySelected = null }) { Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        )
    }

    categoryToDisable?.let { category ->
        AlertDialog(
            onDismissRequest = { categoryToDisable = null },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    Text("Sí, eliminar", color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDisable = null }) { Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant) }
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
            onDismiss = { showCreateDialog = false },
            tutorialBounds = targetBounds
        )
    }

    if (tutorialController != null) {
        val current = tutorialController!!.currentStep
        SpotlightOverlay(
            targetBounds = current?.let { targetBounds[it.targetKey] },
            message = current?.message ?: "",
            isLastStep = tutorialController?.isLastStep ?: false,
            onNext = { tutorialController?.next() },
            onSkip = {
                coroutineScope.launch { TutorialPreferences.markCompleted(context, "tutorial_categorias") }
                tutorialController = null
            }
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
            onDismiss = { categoryToEdit = null },
            tutorialBounds = targetBounds
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
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val active = category.active == true
    val accentColor = if (active) IncomeGreen else MaterialTheme.colorScheme.onSurfaceVariant
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) ExpenseRed.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceContainer,
        label = ""
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { if (isSelected) onDismiss() },
                onLongClick = onLongPress
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
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
                    color = if (isSelected) ExpenseRed else MaterialTheme.colorScheme.onSurface
                )
                if (!category.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        category.description!!,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (active) IncomeGreen else MaterialTheme.colorScheme.onSurfaceVariant)
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
    onDismiss: () -> Unit,
    tutorialBounds: MutableMap<String, Rect>? = null
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
        title = { Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = if (tutorialBounds != null) {
                        Modifier.fillMaxWidth().tutorialTarget("category_type_toggle", tutorialBounds)
                    } else {
                        Modifier.fillMaxWidth()
                    },
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

                FinanxOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre",
                    singleLine = true
                )
                FinanxOutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Descripción"
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name, description, type) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    )
}

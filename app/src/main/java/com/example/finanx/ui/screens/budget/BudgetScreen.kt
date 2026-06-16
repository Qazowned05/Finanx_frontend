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
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.BudgetRequest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.math.BigDecimal
import android.app.DatePickerDialog
import java.util.Calendar
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.theme.LightBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen() {

    var search by remember { mutableStateOf("") }

    var selectedTab by remember { mutableStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }

    // Mantener la lista de ejemplo estable entre recomposiciones para evitar problemas de renderizado
    val budgets = remember {
        listOf(
            BudgetResponse(
                id = 1,
                description = "Comida",
                limitAmount = BigDecimal(800),
                state = BudgetResponse.State.ACTIVE
            ),
            BudgetResponse(
                id = 2,
                description = "Servicios",
                limitAmount = BigDecimal(600),
                state = BudgetResponse.State.ACTIVE
            ),
            BudgetResponse(
                id = 3,
                description = "Ahorros",
                limitAmount = BigDecimal(1000),
                state = BudgetResponse.State.ACTIVE
            ),
            BudgetResponse(
                id = 4,
                description = "Salidas",
                limitAmount = BigDecimal(350),
                state = BudgetResponse.State.DISABLED
            )
        )
    }

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
            // Mostrar FAB solo en pestañas "Todos" (0) o "ACTIVE" (1)
            if (selectedTab == 0 || selectedTab == 1) {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = BluePrimary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null
                    )
                }
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

                items(items = filteredBudgets, key = { it.id ?: 0L }) { budget ->
                    BudgetCard(
                        budget = budget
                    )
                }

                item {

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    // Mostrar botón "Nuevo Presupuesto" sólo en pestañas "Todos" o "ACTIVE"
                    if (selectedTab == 0 || selectedTab == 1) {
                        Button(
                            onClick = { showDialog = true },
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

                },

                onCreate = { budgetRequest ->
                    // TODO: llamar al ViewModel para crear el presupuesto
                    println("Crear presupuesto: $budgetRequest")
                    showDialog = false
                }

            )

        }
    }
}

@Composable
fun CreateBudgetDialog(onDismiss: () -> Unit, onCreate: (BudgetRequest) -> Unit) {
    var description by remember { mutableStateOf("") }
    var limitAmount by remember { mutableStateOf("") }
    val categories = listOf(1L to "Comida", 2L to "Transporte", 3L to "Servicios", 4L to "Entretenimiento", 5L to "Otros")
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    var alertable by remember { mutableStateOf(true) }
    var percentAlertStr by remember { mutableStateOf("80") }

    // Date picker state
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    fun parseBigDecimal(value: String): BigDecimal? {
        return try {
            BigDecimal(value)
        } catch (e: Exception) {
            null
        }
    }

    // no-op: dates are selected with DatePickerDialog and stored in startDate/endDate

    val parsedLimit = parseBigDecimal(limitAmount)
    val parsedStart = startDate
    val parsedEnd = endDate
    val parsedPercent = percentAlertStr.toIntOrNull()

    // Evitar uso de '!!' comprobando null antes
    val canCreate = description.isNotBlank() && parsedLimit != null && parsedStart != null && parsedEnd != null && !parsedEnd.isBefore(parsedStart)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Presupuesto") },
        text = {
            // Allow the dialog content to scroll and limit its max height so it fits on small screens
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(scrollState),
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
                    onValueChange = { value ->
                        // allow digits, dot and comma — normalize comma to dot
                        val normalized = value.replace(',', '.')
                        limitAmount = normalized
                    },
                    label = { Text("Monto Límite (ej. 100.00)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(text = "Categoría", fontWeight = FontWeight.Bold)
                // Mostrar categorías en filas con hasta 2 elementos por fila para evitar overflow
                val indexedCategories = categories.mapIndexed { idx, pair -> idx to pair }
                val rows = indexedCategories.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    rows.forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            rowItems.forEach { (index, pair) ->
                                Row(modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp)) {
                                    RadioButton(
                                        selected = selectedCategoryIndex == index,
                                        onClick = { selectedCategoryIndex = index }
                                    )
                                    Spacer(modifier = Modifier.padding(start = 4.dp))
                                    Text(
                                        text = pair.second,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                            // Si la fila tiene un solo elemento, ocupa el espacio restante
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Fecha inicio (DatePicker)
                OutlinedTextField(
                    value = startDate?.format(formatter) ?: "",
                    onValueChange = {},
                    label = { Text("Fecha inicio (YYYY-MM-DD)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartPicker = true },
                    readOnly = true
                )

                // Fecha fin (DatePicker)
                OutlinedTextField(
                    value = endDate?.format(formatter) ?: "",
                    onValueChange = {},
                    label = { Text("Fecha fin (YYYY-MM-DD)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndPicker = true },
                    readOnly = true
                )

                // DatePickerDialogs
                if (showStartPicker) {
                    val now = startDate ?: LocalDate.now()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            startDate = LocalDate.of(year, month + 1, dayOfMonth)
                            showStartPicker = false
                        },
                        now.year,
                        now.monthValue - 1,
                        now.dayOfMonth
                    ).show()
                }

                if (showEndPicker) {
                    val now = endDate ?: LocalDate.now()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            endDate = LocalDate.of(year, month + 1, dayOfMonth)
                            showEndPicker = false
                        },
                        now.year,
                        now.monthValue - 1,
                        now.dayOfMonth
                    ).show()
                }

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text("Alertas")
                    Spacer(modifier = Modifier.padding(8.dp))
                    Switch(checked = alertable, onCheckedChange = { alertable = it })
                }

                OutlinedTextField(
                    value = percentAlertStr,
                    onValueChange = { newVal ->
                        // allow only numbers and limit to 3 chars
                        val filtered = newVal.filter { it.isDigit() }.take(3)
                        percentAlertStr = if (filtered.isEmpty()) "0" else filtered
                    },
                    label = { Text("Porcentaje alerta (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Construir BudgetRequest y llamar al callback
                    // Convertir LocalDate a OffsetDateTime requerido por el modelo
                    val zone = ZoneId.systemDefault()
                    val startOffset = (parsedStart ?: LocalDate.now()).atStartOfDay(zone).toOffsetDateTime()
                    val endOffset = (parsedEnd ?: LocalDate.now()).atStartOfDay(zone).toOffsetDateTime()

                    val budgetRequest = BudgetRequest(
                        alertable = alertable,
                        limitAmount = parsedLimit ?: BigDecimal.ZERO,
                        description = description,
                        userId = 1L, // TODO: reemplazar por usuario real
                        categoryId = categories[selectedCategoryIndex].first,
                        start = startOffset,
                        end = endOffset,
                        percentAlert = parsedPercent
                    )
                    onCreate(budgetRequest)
                },
                enabled = canCreate,
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
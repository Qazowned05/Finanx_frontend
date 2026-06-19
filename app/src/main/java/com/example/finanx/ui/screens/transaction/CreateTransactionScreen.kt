package com.example.finanx.ui.screens.transaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.MovementType
import com.example.finanx.data.model.NewTransactionCommand
import com.example.finanx.data.model.PaymentMethod
import com.example.finanx.data.model.SelectOption
import com.example.finanx.data.model.TransactionFormOptions
import com.example.finanx.ui.theme.ExpenseRed
import com.example.finanx.ui.theme.IncomeGreen
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.viewmodel.TransactionSaveState
import com.example.finanx.ui.viewmodel.TransactionUiState
import java.math.BigDecimal

private const val TOTAL_STEPS = 2

private val moneyInputRegex =
    Regex("""^\d{0,9}([.,]\d{0,2})?$""")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionScreen(
    userId: Long,
    state: TransactionUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onSubmit: (NewTransactionCommand) -> Unit,
    onSaved: () -> Unit
) {
    var step by rememberSaveable {
        mutableIntStateOf(1)
    }

    var type by rememberSaveable {
        mutableStateOf(MovementType.SPENT)
    }

    var amountText by rememberSaveable {
        mutableStateOf("")
    }

    var selectedCategory by remember {
        mutableStateOf<SelectOption?>(null)
    }

    var selectedReason by remember {
        mutableStateOf<SelectOption?>(null)
    }

    var useNewReason by rememberSaveable {
        mutableStateOf(false)
    }

    var newReason by rememberSaveable {
        mutableStateOf("")
    }

    var paymentMethod by rememberSaveable {
        mutableStateOf(PaymentMethod.CASH)
    }

    var selectedSavingGoal by remember {
        mutableStateOf<SelectOption?>(null)
    }

    val isSaving =
        state.saveState is TransactionSaveState.Saving

    val parsedAmount = remember(amountText) {
        amountText
            .replace(",", ".")
            .toBigDecimalOrNull()
    }

    val reasons = state.formOptions.reasons

    /*
     * Cuando no hay motivos existentes, la única opción posible
     * es crear uno nuevo.
     */
    val shouldUseNewReason =
        useNewReason || reasons.isEmpty()

    val reasonIsValid =
        if (shouldUseNewReason) {
            newReason.isNotBlank()
        } else {
            selectedReason != null
        }

    val canSave =
        parsedAmount != null &&
                parsedAmount.signum() > 0 &&
                selectedCategory != null &&
                reasonIsValid &&
                !isSaving

    LaunchedEffect(state.saveState) {
        if (
            state.saveState is
                    TransactionSaveState.Success
        ) {
            onSaved()
        }
    }

    /*
     * En el segundo paso, el botón físico de atrás regresa al
     * primer paso. Mientras guarda, bloquea la salida accidental.
     */
    BackHandler(
        enabled = step == 2 || isSaving
    ) {
        if (!isSaving && step == 2) {
            step = 1
        }
    }

    Scaffold(
        containerColor =
            MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Nuevo movimiento",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Paso $step de $TOTAL_STEPS",
                            style =
                                MaterialTheme.typography.labelMedium,
                            color = TextGray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        enabled = !isSaving,
                        onClick = {
                            if (step == 2) {
                                step = 1
                            } else {
                                onBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (step) {
            1 -> {
                TransactionStepOne(
                    paddingValues = paddingValues,
                    type = type,
                    amountText = amountText,
                    parsedAmount = parsedAmount,
                    onTypeChange = { newType ->
                        type = newType

                        /*
                         * Si el usuario retrocede y cambia el tipo,
                         * obligamos a escoger nuevamente la categoría.
                         */
                        selectedCategory = null
                    },
                    onAmountChange = { newValue ->
                        if (newValue.matches(moneyInputRegex)) {
                            amountText = newValue
                        }
                    },
                    onContinue = {
                        step = 2
                    }
                )
            }

            else -> {
                TransactionStepTwo(
                    paddingValues = paddingValues,
                    type = type,
                    amount = requireNotNull(parsedAmount),
                    options = state.formOptions,
                    selectedCategory = selectedCategory,
                    selectedReason = selectedReason,
                    useNewReason = shouldUseNewReason,
                    newReason = newReason,
                    paymentMethod = paymentMethod,
                    selectedSavingGoal = selectedSavingGoal,
                    isLoading = state.isLoading,
                    loadError = state.error,
                    saveState = state.saveState,
                    onCategorySelected = {
                        selectedCategory = it
                    },
                    onReasonModeChange = { createNew ->
                        useNewReason = createNew

                        if (createNew) {
                            selectedReason = null
                        }
                    },
                    onReasonSelected = {
                        selectedReason = it
                    },
                    onNewReasonChange = {
                        newReason = it
                    },
                    onPaymentMethodSelected = {
                        paymentMethod = it
                    },
                    onSavingGoalSelected = {
                        selectedSavingGoal = it
                    },
                    onBackStep = {
                        step = 1
                    },
                    onRetry = onRetry,
                    onSave = {
                        val category =
                            selectedCategory
                                ?: return@TransactionStepTwo

                        val amount =
                            parsedAmount
                                ?: return@TransactionStepTwo

                        onSubmit(
                            NewTransactionCommand(
                                userId = userId,
                                categoryId = category.id,
                                amount = amount,
                                paymentMethod = paymentMethod,
                                type = type,
                                reasonId = if (
                                    shouldUseNewReason
                                ) {
                                    null
                                } else {
                                    selectedReason?.id
                                },
                                newReason = if (
                                    shouldUseNewReason
                                ) {
                                    newReason.trim()
                                } else {
                                    null
                                },
                                savingGoalId =
                                    selectedSavingGoal?.id
                            )
                        )
                    },
                    canSave = canSave
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionStepOne(
    paddingValues: PaddingValues,
    type: MovementType,
    amountText: String,
    parsedAmount: BigDecimal?,
    onTypeChange: (MovementType) -> Unit,
    onAmountChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    val typeOptions = listOf(
        MovementType.SPENT to "Gasto",
        MovementType.INCOME to "Ingreso"
    )

    val invalidAmount =
        amountText.isNotBlank() &&
                (
                        parsedAmount == null ||
                                parsedAmount.signum() <= 0
                        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "¿Qué movimiento deseas registrar?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Primero indica si el dinero ingresó o salió de tu cuenta y registra el monto.",
            color = TextGray,
            style = MaterialTheme.typography.bodyMedium
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            typeOptions.forEachIndexed {
                    index,
                    option ->

                val optionType = option.first
                val label = option.second

                SegmentedButton(
                    selected = type == optionType,
                    onClick = {
                        onTypeChange(optionType)
                    },
                    shape =
                        SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = typeOptions.size
                        ),
                    label = {
                        Text(label)
                    }
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                    if (type == MovementType.INCOME) {
                        IncomeGreen.copy(alpha = 0.10f)
                    } else {
                        ExpenseRed.copy(alpha = 0.10f)
                    }
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (
                        type == MovementType.INCOME
                    ) {
                        "Registrar ingreso"
                    } else {
                        "Registrar gasto"
                    },
                    fontWeight = FontWeight.SemiBold,
                    color = if (
                        type == MovementType.INCOME
                    ) {
                        IncomeGreen
                    } else {
                        ExpenseRed
                    }
                )
            }
        }

        OutlinedTextField(
            value = amountText,
            onValueChange = onAmountChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Monto")
            },
            placeholder = {
                Text("0.00")
            },
            prefix = {
                Text("S/ ")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            isError = invalidAmount,
            supportingText = {
                when {
                    invalidAmount -> {
                        Text(
                            text = "Ingresa un monto mayor que cero"
                        )
                    }

                    else -> {
                        Text(
                            text = "Puedes ingresar hasta dos decimales"
                        )
                    }
                }
            },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onContinue,
            enabled =
                parsedAmount != null &&
                        parsedAmount.signum() > 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Continuar",
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Icon(
                imageVector =
                    Icons.Default.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionStepTwo(
    paddingValues: PaddingValues,
    type: MovementType,
    amount: BigDecimal,
    options: TransactionFormOptions,
    selectedCategory: SelectOption?,
    selectedReason: SelectOption?,
    useNewReason: Boolean,
    newReason: String,
    paymentMethod: PaymentMethod,
    selectedSavingGoal: SelectOption?,
    isLoading: Boolean,
    loadError: String?,
    saveState: TransactionSaveState,
    onCategorySelected: (SelectOption) -> Unit,
    onReasonModeChange: (Boolean) -> Unit,
    onReasonSelected: (SelectOption) -> Unit,
    onNewReasonChange: (String) -> Unit,
    onPaymentMethodSelected: (PaymentMethod) -> Unit,
    onSavingGoalSelected: (SelectOption?) -> Unit,
    onBackStep: () -> Unit,
    onRetry: () -> Unit,
    onSave: () -> Unit,
    canSave: Boolean
) {
    if (
        isLoading &&
        options.categories.isEmpty()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    if (options.categories.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {
            Text(
                text = loadError
                    ?: "No tienes categorías activas para registrar el movimiento.",
                color = if (loadError != null) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onRetry
            ) {
                Text("Reintentar")
            }
        }

        return
    }

    val saveError =
        (saveState as? TransactionSaveState.Error)
            ?.message

    val isSaving =
        saveState is TransactionSaveState.Saving

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Completa los detalles",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (
                            type == MovementType.INCOME
                        ) {
                            "Ingreso"
                        } else {
                            "Gasto"
                        },
                        color = TextGray
                    )

                    Text(
                        text = "S/ ${amount.toPlainString()}",
                        style =
                            MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (
                            type == MovementType.INCOME
                        ) {
                            IncomeGreen
                        } else {
                            ExpenseRed
                        }
                    )
                }

                OutlinedButton(
                    onClick = onBackStep,
                    enabled = !isSaving
                ) {
                    Text("Editar")
                }
            }
        }

        SelectOptionDropdown(
            label = "Categoría",
            placeholder = "Selecciona una categoría",
            options = options.categories,
            selectedOption = selectedCategory,
            onSelected = onCategorySelected,
            enabled = !isSaving
        )

        Text(
            text = "La categoría debe corresponder al tipo de movimiento seleccionado.",
            color = TextGray,
            style = MaterialTheme.typography.labelSmall
        )

        HorizontalDivider()

        Text(
            text = "Motivo o descripción",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        /*
         * El backend requiere reasonId o description.
         * Cuando se escribe una descripción, el backend crea un
         * nuevo motivo para el usuario.
         */
        if (options.reasons.isNotEmpty()) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = !useNewReason,
                    onClick = {
                        onReasonModeChange(false)
                    },
                    enabled = !isSaving,
                    shape =
                        SegmentedButtonDefaults.itemShape(
                            index = 0,
                            count = 2
                        ),
                    label = {
                        Text("Usar existente")
                    }
                )

                SegmentedButton(
                    selected = useNewReason,
                    onClick = {
                        onReasonModeChange(true)
                    },
                    enabled = !isSaving,
                    shape =
                        SegmentedButtonDefaults.itemShape(
                            index = 1,
                            count = 2
                        ),
                    label = {
                        Text("Crear nuevo")
                    }
                )
            }
        }

        if (
            useNewReason ||
            options.reasons.isEmpty()
        ) {
            OutlinedTextField(
                value = newReason,
                onValueChange = onNewReasonChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Descripción")
                },
                placeholder = {
                    Text(
                        if (type == MovementType.INCOME) {
                            "Ejemplo: Pago de sueldo"
                        } else {
                            "Ejemplo: Compra semanal"
                        }
                    )
                },
                enabled = !isSaving,
                singleLine = true,
                supportingText = {
                    Text(
                        "Se guardará como un nuevo motivo"
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )
        } else {
            SelectOptionDropdown(
                label = "Motivo",
                placeholder = "Selecciona un motivo",
                options = options.reasons,
                selectedOption = selectedReason,
                onSelected = onReasonSelected,
                enabled = !isSaving
            )
        }

        PaymentMethodDropdown(
            selected = paymentMethod,
            onSelected = onPaymentMethodSelected,
            enabled = !isSaving
        )

        SelectOptionDropdown(
            label = "Meta de ahorro",
            placeholder = "Sin meta de ahorro",
            options = options.savingGoals,
            selectedOption = selectedSavingGoal,
            onSelected = {
                onSavingGoalSelected(it)
            },
            enabled = !isSaving,
            allowEmptySelection = true,
            emptySelectionLabel = "Sin meta de ahorro"
        )

        Text(
            text = "La meta de ahorro es opcional.",
            color = TextGray,
            style = MaterialTheme.typography.labelSmall
        )

        if (saveError != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme
                            .colorScheme
                            .errorContainer
                )
            ) {
                Text(
                    text = saveError,
                    modifier = Modifier.padding(14.dp),
                    color =
                        MaterialTheme
                            .colorScheme
                            .onErrorContainer
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackStep,
                enabled = !isSaving,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Atrás")
            }

            Button(
                onClick = onSave,
                enabled = canSave,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.size(6.dp)
                    )

                    Text(
                        text = "Guardar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectOptionDropdown(
    label: String,
    placeholder: String,
    options: List<SelectOption>,
    selectedOption: SelectOption?,
    onSelected: (SelectOption) -> Unit,
    enabled: Boolean,
    allowEmptySelection: Boolean = false,
    emptySelectionLabel: String = ""
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = it
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption?.label
                ?: if (allowEmptySelection) {
                    emptySelectionLabel
                } else {
                    ""
                },
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            enabled = enabled,
            label = {
                Text(label)
            },
            placeholder = {
                Text(placeholder)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            if (allowEmptySelection) {
                DropdownMenuItem(
                    text = {
                        Text(emptySelectionLabel)
                    },
                    onClick = {
                        /*
                         * El caso nulo se procesa por separado
                         * mediante un SelectOption temporal.
                         */
                        expanded = false
                    }
                )
            }

            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option.label)
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentMethodDropdown(
    selected: PaymentMethod,
    onSelected: (PaymentMethod) -> Unit,
    enabled: Boolean
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = it
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selected.label(),
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            enabled = enabled,
            label = {
                Text("Método de pago")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            PaymentMethod.entries.forEach { method ->
                DropdownMenuItem(
                    text = {
                        Text(method.label())
                    },
                    onClick = {
                        onSelected(method)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun PaymentMethod.label(): String =
    when (this) {
        PaymentMethod.CASH ->
            "Efectivo"

        PaymentMethod.CREDIT_CARD ->
            "Tarjeta de crédito"

        PaymentMethod.DEBIT_CARD ->
            "Tarjeta de débito"

        PaymentMethod.BANK_TRANSFER ->
            "Transferencia bancaria"

        PaymentMethod.YAPE ->
            "Yape"

        PaymentMethod.PLIN ->
            "Plin"

        PaymentMethod.OTHER ->
            "Otro"
    }
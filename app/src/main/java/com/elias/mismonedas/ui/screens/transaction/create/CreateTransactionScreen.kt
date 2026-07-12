package com.elias.mismonedas.ui.screens.transaction.create

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elias.mismonedas.data.model.CategoryOption
import com.elias.mismonedas.data.model.NewTransactionCommand
import com.elias.mismonedas.data.model.SelectOption
import com.elias.mismonedas.data.preferences.TransactionQuickPrefs
import com.elias.mismonedas.ui.components.FinanxOutlinedTextField
import com.elias.mismonedas.ui.components.ReasonAutocomplete
import com.elias.mismonedas.ui.theme.ExpenseRed
import com.elias.mismonedas.ui.theme.IncomeGreen
import com.elias.mismonedas.ui.viewmodel.SavingGoalState
import com.elias.mismonedas.ui.viewmodel.TransactionDraftField
import com.elias.mismonedas.ui.viewmodel.TransactionDraftState
import com.elias.mismonedas.ui.viewmodel.TransactionSaveState
import org.openapitools.client.models.TransactionRequest
import com.elias.mismonedas.ui.viewmodel.TransactionViewModel
import com.elias.mismonedas.ui.viewmodel.SavingGoalViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateTransactionScreen(
    registerHasUnsaved: ((() -> Boolean) -> Unit)? = null,
    userId: Long,
    transactionViewModel: TransactionViewModel,
    savingGoalViewModel: SavingGoalViewModel,
    onCreateSavingGoal: () -> Unit,
    onCreateCategory: () -> Unit,
    onSaved: () -> Unit,
    ocrAmount: String = "",
    ocrMerchant: String = "",
    ocrReceiptId: Long = 0,
    ocrCurrency: String = "PEN",
    ocrPaymentMethod: String = "",
    ocrTransactionType: String = "",
    ocrCategoryId: Long = 0,
    modifier: Modifier = Modifier
) {
    val draft = transactionViewModel.draft
    val options = transactionViewModel.state.formOptions
    val scrollState = rememberScrollState()

    val hasActiveSavingGoals = when (val state = savingGoalViewModel.savingGoalState) {
        is SavingGoalState.Success -> state.goals.any { it.active == true && it.state == org.openapitools.client.models.SavingGoalResponse.State.RUNNING }
        else -> options.savingGoals.isNotEmpty()
    }
    val isSaving = transactionViewModel.state.saveState is TransactionSaveState.Saving
    val saveError = (transactionViewModel.state.saveState as? TransactionSaveState.Error)?.message

    var attemptedSubmit by remember { mutableStateOf(false) }

    val hasUnsaved = draft.type != null ||
        draft.categoryId != null ||
        draft.amountText.isNotBlank() ||
        draft.reasonId != null ||
        draft.reasonText.isNotBlank() ||
        draft.paymentMethod != null ||
        draft.savingGoalId != null

    registerHasUnsaved?.invoke { hasUnsaved }

    LaunchedEffect(ocrAmount, ocrMerchant, ocrReceiptId, ocrCurrency, ocrPaymentMethod, ocrTransactionType, ocrCategoryId) {
        if (ocrAmount.isBlank() && ocrMerchant.isBlank()) return@LaunchedEffect

        transactionViewModel.prefillFromOcr(
            amount = ocrAmount,
            merchant = ocrMerchant,
            receiptId = ocrReceiptId,
            currency = ocrCurrency,
            paymentMethodValue = ocrPaymentMethod,
            transactionTypeValue = ocrTransactionType,
            categoryId = ocrCategoryId,
            availableCategories = options.categories
        )
    }

    var walletCurrency by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(userId) {
        walletCurrency = transactionViewModel.getWalletCurrency(userId)
    }

    val showCurrencyMismatch = ocrReceiptId > 0 &&
        walletCurrency != null &&
        !ocrCurrency.equals(walletCurrency, ignoreCase = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TypeSelectorCompact(
            selected = draft.type,
            onSelect = { type ->
                attemptedSubmit = false
                transactionViewModel.updateType(type)
                val prefCatId = TransactionQuickPrefs.lastCategoryId(type)
                if (prefCatId != null && options.categories.any { it.id == prefCatId }) {
                    transactionViewModel.updateCategory(prefCatId)
                }
            }
        )

        if (showCurrencyMismatch) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Moneda diferente detectada",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "Recibo en $ocrCurrency, tu wallet es $walletCurrency",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = draft.convertCurrency,
                            onCheckedChange = { transactionViewModel.updateConvertCurrency(it) }
                        )
                        Text(
                            text = "Convertir monto automáticamente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        if (draft.type != null) {
            val filteredCategories = options.categories
                .filter { it.type == draft.type }
                .sortedBy { it.label.lowercase() }

            CategorySelectionGrid(
                categories = filteredCategories,
                selectedId = draft.categoryId,
                onSelect = { id ->
                    attemptedSubmit = false
                    transactionViewModel.updateCategory(id)
                },
                onCreateCategory = onCreateCategory
            )
        }

        if (draft.categoryId != null) {
            val categoryLabel = options.categories.firstOrNull { it.id == draft.categoryId }?.label

            DetailsSummaryCard(
                type = draft.type,
                categoryLabel = categoryLabel,
                amountText = draft.amountText
            )

            val parsedAmount = draft.amountText.replace(",", ".").toBigDecimalOrNull()
            val amountError = (attemptedSubmit && (parsedAmount == null || parsedAmount.signum() <= 0)) ||
                draft.fieldErrors.contains(TransactionDraftField.AMOUNT)
            val paymentError = (attemptedSubmit && draft.paymentMethod == null) ||
                draft.fieldErrors.contains(TransactionDraftField.PAYMENT_METHOD)

            FinanxOutlinedTextField(
                value = draft.amountText,
                onValueChange = transactionViewModel::updateAmount,
                label = "Monto",
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                enabled = !isSaving,
                isError = amountError,
                supportingText = when {
                    amountError && draft.amountText.isBlank() -> "Ingresa un monto"
                    amountError -> "Ingresa un monto válido"
                    else -> "Puedes ingresar hasta dos decimales"
                }
            )

            ReasonAutocomplete(
                text = draft.reasonText,
                reasons = options.reasons,
                enabled = !isSaving,
                onReasonChange = transactionViewModel::updateReason,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Método de pago",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TransactionRequest.PaymentMethod.entries.forEach { method ->
                        FilterChip(
                            selected = draft.paymentMethod == method,
                            onClick = {
                                if (!isSaving) {
                                    transactionViewModel.updatePaymentMethod(method)
                                }
                            },
                            label = { Text(method.toSpanishLabel()) }
                        )
                    }
                }
                if (paymentError) {
                    Text(
                        text = "Selecciona un método de pago",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (saveError != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = saveError,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (draft.type == TransactionRequest.Type.INCOME && hasActiveSavingGoals) {
                Text(
                    text = "Meta de ahorro (opcional)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                SavingGoalInlineRow(
                    goals = options.savingGoals,
                    selectedId = draft.savingGoalId,
                    onSelect = transactionViewModel::updateSavingGoal,
                    onCreate = onCreateSavingGoal
                )
            }

            Button(
                onClick = {
                    attemptedSubmit = true
                    submitDraft(
                        userId = userId,
                        draft = draft,
                        transactionViewModel = transactionViewModel,
                        saveNow = true
                    )
                },
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Guardar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    LaunchedEffect(transactionViewModel.state.saveState) {
        if (transactionViewModel.state.saveState is TransactionSaveState.Success) {
            onSaved()
        }
    }
}

// --- Type Selector with Icons (#3) ---

@Composable
private fun TypeSelectorCompact(
    selected: TransactionRequest.Type?,
    onSelect: (TransactionRequest.Type) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TypeOption(
            type = TransactionRequest.Type.SPENT,
            label = "Gasto",
            icon = Icons.Default.TrendingDown,
            isSelected = selected == TransactionRequest.Type.SPENT,
            selectedContainer = MaterialTheme.colorScheme.errorContainer,
            selectedContent = MaterialTheme.colorScheme.onErrorContainer,
            onClick = { onSelect(TransactionRequest.Type.SPENT) }
        )
        TypeOption(
            type = TransactionRequest.Type.INCOME,
            label = "Ingreso",
            icon = Icons.Default.TrendingUp,
            isSelected = selected == TransactionRequest.Type.INCOME,
            selectedContainer = MaterialTheme.colorScheme.primaryContainer,
            selectedContent = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = { onSelect(TransactionRequest.Type.INCOME) }
        )
    }
}

@Composable
private fun RowScope.TypeOption(
    type: TransactionRequest.Type,
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    selectedContainer: Color,
    selectedContent: Color,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) selectedContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) selectedContent else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        modifier = Modifier.weight(1f)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

// --- Category Grid Visual (#4) ---

private val categoryColorPalette = listOf(
    Color(0xFF5B8A72),
    Color(0xFFC17F4E),
    Color(0xFF6B8E9E),
    Color(0xFFB8737A),
    Color(0xFF7B9E6B),
    Color(0xFFA67C52),
    Color(0xFF7A8FA6),
    Color(0xFFC48793),
    Color(0xFF8FAD6B),
    Color(0xFFB58A6E)
)

private fun categoryColor(id: Long): Color =
    categoryColorPalette[(id % categoryColorPalette.size).toInt()]

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategorySelectionGrid(
    categories: List<CategoryOption>,
    selectedId: Long?,
    onSelect: (Long) -> Unit,
    onCreateCategory: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Selecciona una categoría",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )

        if (categories.isEmpty()) {
            Text(
                text = "No hay categorías para este tipo.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Button(
                onClick = onCreateCategory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear categoría")
            }
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedId == category.id
                    val color = categoryColor(category.id)
                    val bgColor = if (isSelected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                    val borderColor = if (isSelected) color else MaterialTheme.colorScheme.outlineVariant

                    Surface(
                        onClick = { onSelect(category.id) },
                        shape = RoundedCornerShape(12.dp),
                        color = bgColor,
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = borderColor
                        ),
                        modifier = Modifier.width(80.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = category.label.take(1).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Text(
                                text = category.label,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                color = if (isSelected) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Surface(
                    onClick = onCreateCategory,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.width(80.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear categoría",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "Nueva",
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// --- Summary Card ---

@Composable
private fun DetailsSummaryCard(
    type: TransactionRequest.Type?,
    categoryLabel: String?,
    amountText: String
) {
    val isIncome = type == TransactionRequest.Type.INCOME
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (isIncome) "Ingreso" else "Gasto",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = categoryLabel ?: "Sin categoría",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (amountText.isNotBlank()) {
                Text(
                    text = "S/ $amountText",
                    color = if (isIncome) IncomeGreen else ExpenseRed,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// --- Inline Saving Goal Row ---

@Composable
private fun SavingGoalInlineRow(
    goals: List<SelectOption>,
    selectedId: Long?,
    onSelect: (Long?) -> Unit,
    onCreate: () -> Unit
) {
    val cardWidth = 90.dp
    val cardHeight = 100.dp

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(goals) { goal ->
            val selected = selectedId == goal.id
            val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            val containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
            Card(
                modifier = Modifier
                    .size(width = cardWidth, height = cardHeight)
                    .clickable(onClick = {
                        if (selected) onSelect(null) else onSelect(goal.id)
                    }),
                colors = CardDefaults.cardColors(containerColor = containerColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified
                    )
                    Text(
                        text = goal.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        item {
            val primaryColor = MaterialTheme.colorScheme.primary
            Card(
                modifier = Modifier
                    .size(width = cardWidth, height = cardHeight)
                    .clickable(onClick = onCreate)
                    .drawBehind {
                        val stroke = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                        )
                        drawRect(color = primaryColor, style = stroke)
                    },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear meta",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Crear meta",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

// --- Submit Logic ---

private fun submitDraft(
    userId: Long,
    draft: TransactionDraftState,
    transactionViewModel: TransactionViewModel,
    saveNow: Boolean
): Boolean {
    val amount = draft.amountText.replace(",", ".").toBigDecimalOrNull()
    val categoryId = draft.categoryId
    val paymentMethod = draft.paymentMethod
    val type = draft.type

    val errors = buildSet {
        if (categoryId == null) add(TransactionDraftField.CATEGORY)
        if (amount == null || amount.signum() <= 0) add(TransactionDraftField.AMOUNT)
        if (paymentMethod == null) add(TransactionDraftField.PAYMENT_METHOD)
    }
    if (errors.isNotEmpty() || type == null) {
        transactionViewModel.setFieldErrors(errors)
        return false
    }

    transactionViewModel.setFieldErrors(emptySet())
    if (!saveNow) return true
    val safeCategoryId = categoryId ?: return false
    val safeAmount = amount ?: return false
    val safePaymentMethod = paymentMethod ?: return false

    transactionViewModel.createTransaction(
        NewTransactionCommand(
            userId = userId,
            categoryId = safeCategoryId,
            amount = safeAmount,
            paymentMethod = safePaymentMethod,
            type = type,
            reasonId = draft.reasonId,
            newReason = if (draft.reasonId == null) draft.reasonText.trim().ifBlank { null } else null,
            savingGoalId = if (type == TransactionRequest.Type.INCOME) draft.savingGoalId else null,
            receiptId = draft.receiptId,
            convertCurrency = draft.convertCurrency
        )
    )
    return true
}

private fun TransactionRequest.PaymentMethod.toSpanishLabel(): String {
    return when (this) {
        TransactionRequest.PaymentMethod.CASH -> "Efectivo"
        TransactionRequest.PaymentMethod.CREDIT_CARD -> "Tarjeta crédito"
        TransactionRequest.PaymentMethod.DEBIT_CARD -> "Tarjeta débito"
        TransactionRequest.PaymentMethod.BANK_TRANSFER -> "Transferencia"
        TransactionRequest.PaymentMethod.YAPE -> "Yape"
        TransactionRequest.PaymentMethod.PLIN -> "Plin"
        TransactionRequest.PaymentMethod.OTHER -> "Otro"
    }
}

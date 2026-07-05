package com.example.finanx.ui.screens.transaction.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.NewTransactionCommand
import com.example.finanx.data.model.PaymentMethod
import com.example.finanx.data.model.SelectOption
import com.example.finanx.data.model.TransactionType
import com.example.finanx.ui.components.SpotlightOverlay
import com.example.finanx.ui.components.TutorialPreferences
import com.example.finanx.ui.components.tutorial.TutorialController
import com.example.finanx.ui.components.tutorial.TutorialStep
import com.example.finanx.ui.components.tutorialTarget
import com.example.finanx.ui.viewmodel.SavingGoalState
import com.example.finanx.ui.viewmodel.TransactionDraftField
import com.example.finanx.ui.viewmodel.TransactionDraftState
import com.example.finanx.ui.viewmodel.TransactionSaveState
import com.example.finanx.ui.viewmodel.TransactionStep
import com.example.finanx.ui.viewmodel.TransactionViewModel
import com.example.finanx.ui.viewmodel.visibleSteps
import com.example.finanx.ui.viewmodel.SavingGoalViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun CreateTransactionScreen(
    registerHasUnsaved: ((() -> Boolean) -> Unit)? = null,
    userId: Long,
    transactionViewModel: TransactionViewModel,
    savingGoalViewModel: SavingGoalViewModel,
    onCreateSavingGoal: () -> Unit,
    onCreateCategory: () -> Unit,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val draft = transactionViewModel.draft
    val options = transactionViewModel.state.formOptions
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val targetBounds = remember { mutableStateMapOf<String, Rect>() }
    var tutorialController by remember { mutableStateOf<TutorialController?>(null) }

    val hasActiveSavingGoals = when (val state = savingGoalViewModel.savingGoalState) {
        is SavingGoalState.Success -> state.goals.any { it.active == true && it.state == org.openapitools.client.models.SavingGoalResponse.State.RUNNING }
        else -> options.savingGoals.isNotEmpty()
    }
    val isSaving = transactionViewModel.state.saveState is TransactionSaveState.Saving
    val saveError = (transactionViewModel.state.saveState as? TransactionSaveState.Error)?.message
    val visibleSteps = draft.visibleSteps(hasActiveSavingGoals)

    val hasUnsaved = draft.type != null ||
        draft.categoryId != null ||
        draft.amountText.isNotBlank() ||
        draft.reasonId != null ||
        draft.reasonText.isNotBlank() ||
        draft.paymentMethod != null ||
        draft.savingGoalId != null ||
        draft.step != TransactionStep.TYPE

    registerHasUnsaved?.invoke { hasUnsaved }

    LaunchedEffect(Unit) {
        val completed = TutorialPreferences.isCompleted(context, "tutorial_transacciones").first()
        if (!completed) {
            tutorialController = TutorialController(
                steps = listOf(
                    TutorialStep("type_screen", "Elige si es un gasto o un ingreso. Así filtramos las categorías después."),
                    TutorialStep("category_grid", "Aquí solo aparecen categorías del tipo que elegiste."),
                    TutorialStep("amount_field", "Escribe el monto — el teclado numérico aparece solo."),
                    TutorialStep("reason_field", "Escribe el motivo. Si coincide con uno anterior, tócalo en las sugerencias."),
                    TutorialStep("payment_method_row", "Elige cómo pagaste."),
                    TutorialStep("saving_goal_row", "Si este ingreso es para una meta, elígela aquí. Es opcional."),
                    TutorialStep("save_button", "Guarda. Verás al instante cómo afecta tu presupuesto.")
                )
            ) {
                scope.launch { TutorialPreferences.markCompleted(context, "tutorial_transacciones") }
                tutorialController = null
            }
        }
    }

    LaunchedEffect(draft.step) {
        if (draft.step == TransactionStep.SAVING_GOAL) {
            savingGoalViewModel.fetchSavingGoals(userId)
        }
    }

    when (draft.step) {
        TransactionStep.TYPE -> TypeSelectionStep(
            onSelect = transactionViewModel::updateType,
            modifier = modifier.tutorialTarget("type_screen", targetBounds)
        )

        TransactionStep.CATEGORY -> CategorySelectionStep(
            categories = options.categories
                .filter { it.type == draft.type }
                .sortedBy { it.label.lowercase() },
            onSelect = transactionViewModel::updateCategory,
            onCreateCategory = onCreateCategory,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .tutorialTarget("category_grid", targetBounds)
        )

        TransactionStep.DETAILS -> TransactionDetailsStep(
            draft = draft,
            categoryLabel = options.categories.firstOrNull { it.id == draft.categoryId }?.label,
            reasons = options.reasons,
            hasSavingGoalStep = visibleSteps.contains(TransactionStep.SAVING_GOAL),
            isSaving = isSaving,
            saveError = saveError,
            onAmountChange = transactionViewModel::updateAmount,
            onReasonChange = transactionViewModel::updateReason,
            onPaymentChange = transactionViewModel::updatePaymentMethod,
            onSubmit = {
                val submitted = submitDraft(
                    userId = userId,
                    draft = draft,
                    transactionViewModel = transactionViewModel,
                    saveNow = !visibleSteps.contains(TransactionStep.SAVING_GOAL)
                )
                if (submitted && visibleSteps.contains(TransactionStep.SAVING_GOAL)) {
                    transactionViewModel.goToStep(TransactionStep.SAVING_GOAL)
                }
            },
            registry = targetBounds,
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        )

        TransactionStep.SAVING_GOAL -> {
            val goals = when (val state = savingGoalViewModel.savingGoalState) {
                is SavingGoalState.Success -> state.goals
                    .filter { it.active == true && it.state == org.openapitools.client.models.SavingGoalResponse.State.RUNNING }
                    .mapNotNull { goal ->
                        val id = goal.id ?: return@mapNotNull null
                        SelectOption(id = id, label = goal.description.orEmpty())
                    }
                else -> options.savingGoals
            }
            SavingGoalSelectionStep(
                savingGoals = goals,
                selectedId = draft.savingGoalId,
                onSelect = transactionViewModel::updateSavingGoal,
                onCreate = onCreateSavingGoal,
                isSaving = isSaving,
                saveError = saveError,
                onSave = {
                    submitDraft(
                        userId = userId,
                        draft = draft,
                        transactionViewModel = transactionViewModel,
                        saveNow = true
                    )
                },
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .tutorialTarget("saving_goal_row", targetBounds),
                registry = targetBounds
            )
        }
    }

    LaunchedEffect(transactionViewModel.state.saveState) {
        if (transactionViewModel.state.saveState is TransactionSaveState.Success) {
            onSaved()
        }
    }

    tutorialController?.let { controller ->
        val current = controller.currentStep
        SpotlightOverlay(
            targetBounds = current?.let { targetBounds[it.targetKey] },
            message = current?.message.orEmpty(),
            isLastStep = controller.isLastStep,
            onNext = { controller.next() },
            onSkip = {
                scope.launch { TutorialPreferences.markCompleted(context, "tutorial_transacciones") }
                tutorialController = null
            }
        )
    }
}

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
            savingGoalId = if (type == TransactionType.INCOME) draft.savingGoalId else null
        )
    )
    return true
}

@Composable
private fun TypeSelectionStep(
    onSelect: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { onSelect(TransactionType.SPENT) },
            contentAlignment = Alignment.Center
        ) {
            Text("GASTO", style = MaterialTheme.typography.displayMedium)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onSelect(TransactionType.INCOME) },
            contentAlignment = Alignment.Center
        ) {
            Text("INGRESO", style = MaterialTheme.typography.displayMedium)
        }
    }
}

@Composable
private fun CategorySelectionStep(
    categories: List<com.example.finanx.data.model.CategoryOption>,
    onSelect: (Long) -> Unit,
    onCreateCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Selecciona una categoría",
            style = MaterialTheme.typography.titleMedium
        )
        if (categories.isEmpty()) {
            Text("No hay categorías para este tipo.")
            Button(onClick = onCreateCategory, modifier = Modifier.fillMaxWidth()) {
                Text("Crear categoría")
            }
        } else {
            categories.forEach { category ->
                Card(
                    onClick = { onSelect(category.id) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = category.label,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun SavingGoalSelectionStep(
    savingGoals: List<SelectOption>,
    selectedId: Long?,
    onSelect: (Long?) -> Unit,
    onCreate: () -> Unit,
    isSaving: Boolean,
    saveError: String?,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    registry: MutableMap<String, Rect>
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Meta de ahorro (opcional)")
        SavingGoalSelectorRow(
            goals = savingGoals,
            selectedGoalId = selectedId,
            onGoalSelected = onSelect,
            onCreateGoal = onCreate,
            modifier = Modifier.fillMaxWidth()
        )
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
        Button(
            onClick = onSave,
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .tutorialTarget("save_button", registry),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Guardar")
            }
        }
    }
}

package com.example.finanx.ui.screens.transaction.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finanx.data.model.NewTransactionCommand
import com.example.finanx.data.model.TransactionFormOptions
import com.example.finanx.data.model.PaymentMethod
import com.example.finanx.data.preferences.TransactionQuickPrefs
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.viewmodel.TransactionSaveState

private const val AMOUNT_STEP = 1
private const val DETAILS_STEP = 2
private const val TOTAL_STEPS = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionScreen(
    registerHasUnsaved: ((() -> Boolean) -> Unit)? = null,
    userId: Long,
    formOptions: TransactionFormOptions,
    isLoading: Boolean,
    loadError: String?,
    saveState: TransactionSaveState,
    step: Int,
    onStepChange: (Int) -> Unit,
    title: String = "Nuevo movimiento",
    initialForm: CreateTransactionFormState =
        CreateTransactionFormState(),
    allowSchedule: Boolean = false,
    lockScheduleMode: Boolean = false,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onSubmit: (NewTransactionCommand) -> Unit,
    onSaved: () -> Unit
) {
    val rememberedPaymentMethod = TransactionQuickPrefs.lastPaymentMethod() ?: PaymentMethod.CASH
    val preferredInitialForm = initialForm.copy(
        paymentMethod = rememberedPaymentMethod
    )

    var form by rememberSaveable(
        preferredInitialForm.type,
        preferredInitialForm.categoryId,
        preferredInitialForm.paymentMethod,
        preferredInitialForm.isScheduled,
        stateSaver = CreateTransactionFormStateSaver
    ) {
        mutableStateOf(preferredInitialForm)
    }

    val isSaving = saveState is TransactionSaveState.Saving

    LaunchedEffect(saveState) {
        if (saveState is TransactionSaveState.Success) { onSaved() }
    }

    LaunchedEffect(formOptions.categories, form.type) {
        if (form.categoryId == null) {
            val preferredCategoryId = TransactionQuickPrefs.lastCategoryId(form.type)
            val categoryId = preferredCategoryId?.let { preferredId ->
                formOptions.categories.firstOrNull { it.id == preferredId && it.type == form.type }?.id
            } ?: formOptions.categories.firstOrNull { it.type == form.type }?.id

            if (categoryId != null) {
                form = form.copy(categoryId = categoryId)
            }
        }
    }

    // register unsaved-check callback for parent navigation to use when deciding to pop
    registerHasUnsaved?.invoke {
        form != preferredInitialForm
    }

    BackHandler(enabled = step == DETAILS_STEP && !isSaving) {
        onStepChange(AMOUNT_STEP)
    }

    when (step) {
        AMOUNT_STEP -> {
            TransactionAmountStep(
                form = form,
                onFormChange = {
                    form = it
                },
                onContinue = {
                    onStepChange(DETAILS_STEP)
                }
            )
        }

        DETAILS_STEP -> {
            TransactionDetailsStep(
                form = form,
                options = formOptions,
                isLoading = isLoading,
                loadError = loadError,
                saveState = saveState,
                allowSchedule = allowSchedule,
                lockScheduleMode = lockScheduleMode,
                onFormChange = {
                    form = it
                },
                onRetry = onRetry,
                onPrevious = {
                    onStepChange(AMOUNT_STEP)
                },
                onSave = {
                    form.toCommand(
                        userId = userId,
                        hasExistingReasons =
                            formOptions
                                .reasons
                                .isNotEmpty()
                    )?.let(onSubmit)
                }
            )
        }
    }
}

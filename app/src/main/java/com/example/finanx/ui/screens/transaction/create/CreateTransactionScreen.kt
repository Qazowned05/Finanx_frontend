package com.example.finanx.ui.screens.transaction.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.example.finanx.data.model.NewTransactionCommand
import com.example.finanx.data.model.TransactionFormOptions
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.viewmodel.TransactionSaveState

private const val AMOUNT_STEP = 1
private const val DETAILS_STEP = 2
private const val TOTAL_STEPS = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionScreen(
    userId: Long,
    formOptions: TransactionFormOptions,
    isLoading: Boolean,
    loadError: String?,
    saveState: TransactionSaveState,
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
    var step by rememberSaveable {
        mutableIntStateOf(AMOUNT_STEP)
    }

    var form by rememberSaveable(
        stateSaver =
            CreateTransactionFormStateSaver
    ) {
        mutableStateOf(
            initialForm
        )
    }

    val isSaving =
        saveState is
                TransactionSaveState.Saving

    LaunchedEffect(saveState) {
        if (
            saveState is
                    TransactionSaveState.Success
        ) {
            onSaved()
        }
    }

    BackHandler(
        enabled = step == DETAILS_STEP || isSaving
    ) {
        if (!isSaving && step == DETAILS_STEP) {
            step = AMOUNT_STEP
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
                            text = title,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text =
                                "Paso $step de $TOTAL_STEPS",
                            style =
                                MaterialTheme
                                    .typography
                                    .labelMedium,
                            color = TextGray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        enabled = !isSaving,
                        onClick = {
                            if (step == DETAILS_STEP) {
                                step = AMOUNT_STEP
                            } else {
                                onBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (step) {
            AMOUNT_STEP -> {
                TransactionAmountStep(
                    paddingValues = paddingValues,
                    form = form,
                    onFormChange = {
                        form = it
                    },
                    onContinue = {
                        step = DETAILS_STEP
                    }
                )
            }

            DETAILS_STEP -> {
                TransactionDetailsStep(
                    paddingValues = paddingValues,
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
                        step = AMOUNT_STEP
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
}

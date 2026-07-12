package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.model.NewTransactionCommand
import com.elias.mismonedas.data.model.TransactionFormOptions
import com.elias.mismonedas.data.preferences.TransactionQuickPrefs
import com.elias.mismonedas.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse
import javax.inject.Inject

sealed interface TransactionSaveState {
    data object Idle : TransactionSaveState
    data object Saving : TransactionSaveState
    data object Success : TransactionSaveState
    data class Error(val message: String) : TransactionSaveState
}

data class TransactionUiState(
    val isLoading: Boolean = false,
    override val isRefreshing: Boolean = false,
    val transactions: List<TransactionResponse> = emptyList(),
    val formOptions: TransactionFormOptions =
        TransactionFormOptions(),
    val query: String = "",
    val error: String? = null,
    val actionMessage: String? = null,
    val saveState: TransactionSaveState =
        TransactionSaveState.Idle
) : com.elias.mismonedas.ui.viewmodel.RefreshableUiState

enum class TransactionStep { TYPE, CATEGORY, DETAILS, SAVING_GOAL }

enum class TransactionDraftField { CATEGORY, AMOUNT, PAYMENT_METHOD }

data class TransactionDraftState(
    val step: TransactionStep = TransactionStep.TYPE,
    val type: TransactionRequest.Type? = null,
    val categoryId: Long? = null,
    val amountText: String = "",
    val reasonId: Long? = null,
    val reasonText: String = "",
    val paymentMethod: TransactionRequest.PaymentMethod? = null,
    val savingGoalId: Long? = null,
    val receiptId: Long? = null,
    val convertCurrency: Boolean = false,
    val ocrCurrency: String = "PEN",
    val fieldErrors: Set<TransactionDraftField> = emptySet()
)

@Suppress("UNUSED_PARAMETER")
fun TransactionDraftState.visibleSteps(hasActiveSavingGoals: Boolean): List<TransactionStep> {
    val steps = mutableListOf(TransactionStep.TYPE, TransactionStep.CATEGORY, TransactionStep.DETAILS)
    if (this.type == TransactionRequest.Type.INCOME) steps += TransactionStep.SAVING_GOAL
    return steps
}

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    var state by mutableStateOf(TransactionUiState())
        private set

    var draft by mutableStateOf(TransactionDraftState())
        private set

    fun updateType(type: TransactionRequest.Type) {
        draft = draft.copy(
            type = type,
            categoryId = null,
            savingGoalId = if (type == TransactionRequest.Type.INCOME) draft.savingGoalId else null,
            step = TransactionStep.CATEGORY
        )
    }

    fun updateCategory(categoryId: Long) {
        draft = draft.copy(
            categoryId = categoryId,
            step = TransactionStep.DETAILS,
            fieldErrors = draft.fieldErrors - TransactionDraftField.CATEGORY
        )
    }

    fun updateAmount(text: String) {
        draft = draft.copy(
            amountText = text,
            fieldErrors = draft.fieldErrors - TransactionDraftField.AMOUNT
        )
    }

    fun updateReason(id: Long?, text: String) {
        draft = draft.copy(reasonId = id, reasonText = text)
    }

    fun updatePaymentMethod(method: TransactionRequest.PaymentMethod) {
        draft = draft.copy(
            paymentMethod = method,
            fieldErrors = draft.fieldErrors - TransactionDraftField.PAYMENT_METHOD
        )
    }

    fun updateSavingGoal(id: Long?) {
        draft = draft.copy(savingGoalId = id)
    }

    fun updateReceiptId(id: Long?) {
        draft = draft.copy(receiptId = id)
    }

    fun updateConvertCurrency(value: Boolean) {
        draft = draft.copy(convertCurrency = value)
    }

    fun updateOcrCurrency(currency: String) {
        draft = draft.copy(ocrCurrency = currency)
    }

    fun prefillFromOcr(
        amount: String,
        merchant: String,
        receiptId: Long,
        currency: String,
        paymentMethodValue: String,
        transactionTypeValue: String,
        categoryId: Long,
        availableCategories: List<com.elias.mismonedas.data.model.CategoryOption>
    ) {
        if (draft.type != null) return

        val detectedType = when (transactionTypeValue.uppercase()) {
            "INCOME" -> TransactionRequest.Type.INCOME
            else -> TransactionRequest.Type.SPENT
        }

        val matchedCategory = if (categoryId > 0 && availableCategories.any { it.id == categoryId }) {
            categoryId
        } else null

        val matchedPaymentMethod = paymentMethodValue.isNotBlank().let {
            TransactionRequest.PaymentMethod.entries
                .firstOrNull { m -> m.value.equals(paymentMethodValue, ignoreCase = true) }
        }

        draft = draft.copy(
            type = detectedType,
            categoryId = matchedCategory,
            amountText = amount,
            reasonId = null,
            reasonText = merchant,
            paymentMethod = matchedPaymentMethod,
            receiptId = if (receiptId > 0) receiptId else null,
            ocrCurrency = currency,
            step = TransactionStep.DETAILS,
            fieldErrors = emptySet()
        )
    }

    suspend fun getWalletCurrency(userId: Long): String {
        return try {
            repository.getPrimaryWalletCurrency(userId)
        } catch (_: Exception) {
            "PEN"
        }
    }

    fun goToStep(step: TransactionStep) {
        draft = draft.copy(step = step)
    }

    fun resetDraft() {
        draft = TransactionDraftState()
    }

    fun initDefaultsFromPrefs() {
        val lastType = TransactionQuickPrefs.lastTransactionType() ?: return
        val lastCategoryId = TransactionQuickPrefs.lastCategoryId(lastType)
        val lastPaymentMethod = TransactionQuickPrefs.lastPaymentMethod()
        if (lastType != null && lastPaymentMethod != null) {
            draft = draft.copy(
                type = lastType,
                categoryId = lastCategoryId,
                paymentMethod = lastPaymentMethod
            )
        }
    }

    fun setFieldErrors(errors: Set<TransactionDraftField>) {
        draft = draft.copy(fieldErrors = errors)
    }

    var selectedTransactionId: Long? by mutableStateOf(null)
        private set

    fun selectTransaction(id: Long?) {
        selectedTransactionId = id
    }

    fun clearSelectedTransaction() {
        selectedTransactionId = null
    }

    private var currentUserId: Long? = null

    fun load(userId: Long, force: Boolean = false) {
        currentUserId = userId
        if (!force && state.transactions.isNotEmpty() && state.formOptions.categories.isNotEmpty()) return
        currentUserId = userId

        viewModelScope.launch {
            val hasData = state.transactions.isNotEmpty()
            state = state.copy(
                isLoading = !hasData,
                isRefreshing = hasData,
                error = null
            )

            try {
                val transactionData =
                    repository.loadTransactionData(userId)

                state = state.copy(
                    isLoading = false,
                    isRefreshing = false,
                    transactions = transactionData.transactions,
                    formOptions = transactionData.formOptions,
                    error = null
                )
            } catch (exception: Exception) {
                state = state.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = exception.message
                        ?: "No se pudieron cargar los movimientos"
                )
            }
        }
    }

    fun refresh() {
        currentUserId?.let { load(it, force = true) }
    }

    fun setQuery(query: String) {
        state = state.copy(query = query)
    }

    fun createTransaction(command: NewTransactionCommand) {
        if (state.saveState is TransactionSaveState.Saving) {
            return
        }

        viewModelScope.launch {
            state = state.copy(
                saveState = TransactionSaveState.Saving
            )

            try {
                validate(command)
                repository.createTransaction(command)

                state = state.copy(
                    saveState = TransactionSaveState.Success,
                    actionMessage = "Movimiento registrado"
                )

                TransactionQuickPrefs.rememberSelection(
                    type = command.type,
                    categoryId = command.categoryId,
                    paymentMethod = command.paymentMethod
                )

                load(command.userId, force = true)
            } catch (exception: Exception) {
                state = state.copy(
                    saveState = TransactionSaveState.Error(
                        exception.message
                            ?: "No se pudo registrar el movimiento"
                    )
                )
            }
        }
    }

    fun disableTransaction(transactionId: Long) {
        viewModelScope.launch {
            try {
                repository.disableTransaction(transactionId)
                state = state.copy(
                    actionMessage = "Movimiento eliminado"
                )
                refresh()
            } catch (exception: Exception) {
                state = state.copy(
                    error = exception.message
                )
            }
        }
    }

    fun consumeSaveState() {
        state = state.copy(
            saveState = TransactionSaveState.Idle
        )
    }

    fun consumeMessage() {
        state = state.copy(actionMessage = null)
    }

    private fun validate(command: NewTransactionCommand) {
        require(command.amount.signum() > 0) {
            "El monto debe ser mayor que cero"
        }
    }
}

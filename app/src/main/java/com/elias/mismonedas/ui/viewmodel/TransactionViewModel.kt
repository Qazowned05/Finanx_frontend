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
import org.openapitools.client.models.PageResponseTransactionResponse
import org.openapitools.client.models.PaymentMethod
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse
import org.openapitools.client.models.TransactionType
import org.openapitools.client.models.WalletResponse
import java.time.LocalDate
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
        TransactionSaveState.Idle,
    val wallets: List<WalletResponse> = emptyList(),
    val filterType: TransactionType? = null,
    val filterWalletId: Long? = null,
    val filterCategoryId: Long? = null,
    val filterFrom: LocalDate? = null,
    val filterTo: LocalDate? = null,
    val page: Int = 0,
    val hasMore: Boolean = true,
    val isLoadingMore: Boolean = false
) : RefreshableUiState

enum class TransactionStep { TYPE, CATEGORY, DETAILS, SAVING_GOAL }

enum class TransactionDraftField { CATEGORY, AMOUNT, PAYMENT_METHOD }

data class TransactionDraftState(
    val step: TransactionStep = TransactionStep.TYPE,
    val type: TransactionType? = null,
    val categoryId: Long? = null,
    val amountText: String = "",
    val reasonId: Long? = null,
    val reasonText: String = "",
    val paymentMethod: PaymentMethod? = null,
    val savingGoalId: Long? = null,
    val receiptId: Long? = null,
    val convertCurrency: Boolean = false,
    val ocrCurrency: String = "PEN",
    val fieldErrors: Set<TransactionDraftField> = emptySet()
)

@Suppress("UNUSED_PARAMETER")
fun TransactionDraftState.visibleSteps(hasActiveSavingGoals: Boolean): List<TransactionStep> {
    val steps = mutableListOf(TransactionStep.TYPE, TransactionStep.CATEGORY, TransactionStep.DETAILS)
    if (this.type == TransactionType.INCOME) steps += TransactionStep.SAVING_GOAL
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

    fun updateType(type: TransactionType) {
        draft = draft.copy(
            type = type,
            categoryId = null,
            savingGoalId = if (type == TransactionType.INCOME) draft.savingGoalId else null,
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

    fun updatePaymentMethod(method: PaymentMethod) {
        draft = draft.copy(
            paymentMethod = method,
            fieldErrors = draft.fieldErrors - TransactionDraftField.PAYMENT_METHOD
        )
    }

    fun updateSavingGoal(id: Long?) {
        draft = draft.copy(savingGoalId = id)
    }
    //todo verificar uso
    fun updateReceiptId(id: Long?) {
        draft = draft.copy(receiptId = id)
    }

    fun updateConvertCurrency(value: Boolean) {
        draft = draft.copy(convertCurrency = value)
    }

    //todo verificar uso
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
            "INCOME" -> TransactionType.INCOME
            else -> TransactionType.SPENT
        }

        val matchedCategory = if (categoryId > 0 && availableCategories.any { it.id == categoryId }) {
            categoryId
        } else null

        val matchedPaymentMethod = paymentMethodValue.isNotBlank().let {
            PaymentMethod.entries
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
            repository.getPrimaryWalletCurrency()
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

        viewModelScope.launch {
            val hasData = state.transactions.isNotEmpty()
            state = state.copy(
                isLoading = !hasData,
                isRefreshing = hasData,
                error = null
            )

            try {
                val options = if (state.formOptions.categories.isEmpty()) {
                    repository.loadFormOptions()
                } else {
                    state.formOptions
                }
                val wallets = if (state.wallets.isEmpty()) {
                    runCatching { repository.listWallets() }.getOrDefault(emptyList())
                } else {
                    state.wallets
                }
                val page = repository.listTransactionPage(
                    type = state.filterType,
                    walletId = state.filterWalletId,
                    categoryId = state.filterCategoryId,
                    from = state.filterFrom,
                    to = state.filterTo,
                    page = 0
                )

                state = state.copy(
                    isLoading = false,
                    isRefreshing = false,
                    transactions = page.items ?: emptyList(),
                    formOptions = options,
                    wallets = wallets,
                    page = page.pagination?.page ?: 0,
                    hasMore = page.hasMorePages(),
                    isLoadingMore = false,
                    error = null
                )
            } catch (exception: Exception) {
                state = state.copy(
                    isLoading = false,
                    isRefreshing = false,
                    isLoadingMore = false,
                    error = exception.message
                        ?: "No se pudieron cargar los movimientos"
                )
            }
        }
    }

    fun refresh() {
        currentUserId?.let { load(it, force = true) }
    }

    fun loadMore() {
        if (currentUserId == null) return
        if (!state.hasMore || state.isLoadingMore || state.isLoading || state.isRefreshing) return

        viewModelScope.launch {
            state = state.copy(isLoadingMore = true)

            try {
                val next = state.page + 1
                val page = repository.listTransactionPage(
                    type = state.filterType,
                    walletId = state.filterWalletId,
                    categoryId = state.filterCategoryId,
                    from = state.filterFrom,
                    to = state.filterTo,
                    page = next
                )

                state = state.copy(
                    transactions = state.transactions + (page.items ?: emptyList()),
                    page = page.pagination?.page ?: next,
                    hasMore = page.hasMorePages(),
                    isLoadingMore = false,
                    error = null
                )
            } catch (exception: Exception) {
                state = state.copy(
                    isLoadingMore = false,
                    error = exception.message
                        ?: "No se pudieron cargar más movimientos"
                )
            }
        }
    }

    fun setQuery(query: String) {
        state = state.copy(query = query)
    }

    fun setTypeFilter(type: TransactionType?) {
        applyFilters(state.copy(filterType = type))
    }

    fun setWalletFilter(walletId: Long?) {
        applyFilters(state.copy(filterWalletId = walletId))
    }

    fun setCategoryFilter(categoryId: Long?) {
        applyFilters(state.copy(filterCategoryId = categoryId))
    }

    fun setDateFilter(from: LocalDate?, to: LocalDate?) {
        applyFilters(state.copy(filterFrom = from, filterTo = to))
    }

    fun clearFilters() {
        applyFilters(
            state.copy(
                filterType = null,
                filterWalletId = null,
                filterCategoryId = null,
                filterFrom = null,
                filterTo = null
            )
        )
    }

    private fun applyFilters(filtered: TransactionUiState) {
        state = filtered.copy(
            transactions = emptyList(),
            page = 0,
            hasMore = true,
            isLoading = true,
            isRefreshing = false,
            isLoadingMore = false
        )
        currentUserId?.let { load(it, force = true) }
    }

    private fun PageResponseTransactionResponse.hasMorePages(): Boolean {
        val current = pagination?.page ?: 0
        val total = pagination?.totalPages ?: 1
        return current + 1 < total
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

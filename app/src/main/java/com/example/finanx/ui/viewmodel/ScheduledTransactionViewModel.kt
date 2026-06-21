package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.model.NewTransactionCommand
import com.example.finanx.data.model.ScheduledTransactionRecord
import com.example.finanx.data.model.TransactionFormOptions
import com.example.finanx.data.repository.TransactionRepository
import kotlinx.coroutines.launch

data class ScheduledTransactionUiState(
    val isLoading: Boolean = false,
    val scheduledTransactions: List<ScheduledTransactionRecord> = emptyList(),
    val formOptions: TransactionFormOptions = TransactionFormOptions(),
    val query: String = "",
    val error: String? = null,
    val actionMessage: String? = null,
    val saveState: TransactionSaveState = TransactionSaveState.Idle
)

class ScheduledTransactionViewModel(
    private val repository: TransactionRepository = TransactionRepository()
) : ViewModel() {

    var state by mutableStateOf(ScheduledTransactionUiState())
        private set

    private var currentUserId: Long? = null

    fun load(userId: Long) {
        currentUserId = userId

        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            try {
                val transactionData =
                    repository.loadTransactionData(userId)

                val scheduled =
                    repository.loadScheduledTransactions(userId)

                state = state.copy(
                    isLoading = false,
                    scheduledTransactions = scheduled,
                    formOptions = transactionData.formOptions,
                    error = null
                )
            } catch (exception: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = exception.message
                        ?: "No se pudieron cargar las programaciones"
                )
            }
        }
    }

    fun refresh() {
        currentUserId?.let(::load)
    }

    fun setQuery(query: String) {
        state = state.copy(query = query)
    }

    fun createSchedule(command: NewTransactionCommand) {
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
                    actionMessage = "Programación registrada"
                )

                load(command.userId)
            } catch (exception: Exception) {
                state = state.copy(
                    saveState = TransactionSaveState.Error(
                        exception.message
                            ?: "No se pudo registrar la programación"
                    )
                )
            }
        }
    }

    fun pauseSchedule(scheduleId: Long) {
        executeScheduleAction("Programación pausada") {
            repository.pauseSchedule(scheduleId)
        }
    }

    fun resumeSchedule(scheduleId: Long) {
        executeScheduleAction("Programación reanudada") {
            repository.resumeSchedule(scheduleId)
        }
    }

    fun cancelSchedule(scheduleId: Long) {
        executeScheduleAction("Programación cancelada") {
            repository.cancelSchedule(scheduleId)
        }
    }

    fun consumeSaveState() {
        state = state.copy(saveState = TransactionSaveState.Idle)
    }

    fun consumeMessage() {
        state = state.copy(actionMessage = null)
    }

    private fun executeScheduleAction(
        successMessage: String,
        action: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                action()
                state = state.copy(actionMessage = successMessage)
                refresh()
            } catch (exception: Exception) {
                state = state.copy(error = exception.message)
            }
        }
    }

    private fun validate(command: NewTransactionCommand) {
        require(command.schedule != null) {
            "Configura la recurrencia de la programación"
        }

        require(command.amount.signum() > 0) {
            "El monto debe ser mayor que cero"
        }

        require(
            command.reasonId != null ||
                    !command.newReason.isNullOrBlank()
        ) {
            "Selecciona o escribe un motivo"
        }
    }
}

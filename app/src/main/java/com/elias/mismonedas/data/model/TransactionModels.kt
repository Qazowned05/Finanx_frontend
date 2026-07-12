package com.elias.mismonedas.data.model

import org.openapitools.client.models.TransactionResponse
import java.math.BigDecimal

data class SelectOption(
    val id: Long,
    val label: String
)

data class CategoryOption(
    val id: Long,
    val label: String,
    val type: org.openapitools.client.models.TransactionRequest.Type
)

data class TransactionFormOptions(
    val categories: List<CategoryOption> = emptyList(),
    val reasons: List<SelectOption> = emptyList(),
    val savingGoals: List<SelectOption> = emptyList()
)

data class TransactionData(
    val transactions: List<TransactionResponse>,
    val formOptions: TransactionFormOptions
)

data class NewTransactionCommand(
    val userId: Long,
    val walletId: Long? = null,
    val categoryId: Long,
    val amount: BigDecimal,
    val paymentMethod: org.openapitools.client.models.TransactionRequest.PaymentMethod,
    val type: org.openapitools.client.models.TransactionRequest.Type,
    val reasonId: Long? = null,
    val newReason: String? = null,
    val savingGoalId: Long? = null,
    val receiptId: Long? = null,
    val convertCurrency: Boolean = false,
    val schedule: NewTransactionSchedule? = null
)

data class NewTransactionSchedule(
    val recurrenceType: org.openapitools.client.models.RecurrenceRuleRequest.RecurrenceType,
    val interval: Int,
    val startDate: java.time.LocalDate,
    val endDate: java.time.LocalDate,
    val dayOfWeek: java.time.DayOfWeek? = null
)

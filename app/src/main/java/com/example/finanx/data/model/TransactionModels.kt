package com.example.finanx.data.model

import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

enum class MovementType {
    SPENT,
    INCOME
}

enum class PaymentMethod {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    BANK_TRANSFER,
    YAPE,
    PLIN,
    OTHER
}

enum class TransactionsTab {
    ALL,
    SCHEDULED
}

enum class TransactionRecurrenceType {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}

data class SelectOption(
    val id: Long,
    val label: String
)

data class TransactionRecord(
    val id: Long,
    val categoryId: Long?,
    val categoryName: String,
    val reasonId: Long?,
    val description: String,
    val amount: BigDecimal,
    val paymentMethod: PaymentMethod,
    val type: MovementType,
    val createdAt: LocalDateTime?,
    val active: Boolean
)

data class ScheduledTransactionRecord(
    val id: Long,
    val categoryName: String,
    val description: String,
    val amount: BigDecimal,
    val paymentMethod: PaymentMethod,
    val type: MovementType,
    val state: String,
    val nextRunAt: LocalDateTime?
)

data class TransactionFormOptions(
    val categories: List<SelectOption> = emptyList(),
    val reasons: List<SelectOption> = emptyList(),
    val savingGoals: List<SelectOption> = emptyList()
)

data class TransactionData(
    val transactions: List<TransactionRecord>,
    val formOptions: TransactionFormOptions
)

data class NewTransactionCommand(
    val userId: Long,
    val categoryId: Long,
    val amount: BigDecimal,
    val paymentMethod: PaymentMethod,
    val type: MovementType,
    val reasonId: Long? = null,
    val newReason: String? = null,
    val savingGoalId: Long? = null,
    val schedule: NewTransactionSchedule? = null
)

data class NewTransactionSchedule(
    val recurrenceType: TransactionRecurrenceType,
    val interval: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dayOfWeek: DayOfWeek? = null
)

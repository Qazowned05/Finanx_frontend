package com.example.finanx.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
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

@Entity(tableName = "transactions")
@TypeConverters(RoomTypeConverters::class)
data class TransactionRecord(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long?,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "reason_id")
    val reasonId: Long?,
    val description: String,
    val amount: BigDecimal,
    @ColumnInfo(name = "payment_method")
    val paymentMethod: PaymentMethod,
    val type: MovementType,
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime?,
    val active: Boolean
)

@Entity(tableName = "scheduled_transactions")
@TypeConverters(RoomTypeConverters::class)
data class ScheduledTransactionRecord(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    val description: String,
    val amount: BigDecimal,
    @ColumnInfo(name = "payment_method")
    val paymentMethod: PaymentMethod,
    val type: MovementType,
    val state: String,
    @ColumnInfo(name = "next_run_at")
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

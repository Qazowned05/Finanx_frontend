package com.example.finanx.data.models

import java.math.BigDecimal
import java.time.OffsetDateTime

data class TransactionSchedule(
    val id: Long,
    val userId: Long,
    val categoryId: Long? = null,
    val reasonId: Long? = null,
    val amount: BigDecimal,
    val paymentMethod: String,
    val transactionType: String,
    val description: String,
    val recurrenceRuleId: Long,
    val nextRunAt: OffsetDateTime,
    val createdAt: OffsetDateTime,
    val lastRunAt: OffsetDateTime? = null,
    val disabledAt: OffsetDateTime? = null,
    val active: Boolean = true,
    val state: String // e.g., "ACTIVE", "PAUSED", "CANCELLED"
)

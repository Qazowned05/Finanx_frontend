package com.example.finanx.data.models

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Transaction(
    val id: Long,
    val userId: Long,
    val categoryId: Long? = null,
    val reasonId: Long? = null,
    val transactionScheduleId: Long? = null,
    val budgetId: Long? = null,
    val savingGoalId: Long? = null,
    val paymentMethod: String,
    val type: String,
    val amount: BigDecimal,
    val description: String,
    val createdAt: OffsetDateTime,
    val active: Boolean = true,
    val disabledAt: OffsetDateTime? = null
)
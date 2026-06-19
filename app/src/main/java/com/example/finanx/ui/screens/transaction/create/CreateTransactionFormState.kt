package com.example.finanx.ui.screens.transaction.create

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.example.finanx.data.model.MovementType
import com.example.finanx.data.model.NewTransactionCommand
import com.example.finanx.data.model.PaymentMethod
import java.math.BigDecimal

data class CreateTransactionFormState(
    val type: MovementType =
        MovementType.SPENT,

    val amountText: String = "",

    val categoryId: Long? = null,

    val reasonId: Long? = null,

    val useNewReason: Boolean = false,

    val newReason: String = "",

    val paymentMethod: PaymentMethod =
        PaymentMethod.CASH,

    val savingGoalId: Long? = null
) {

    val parsedAmount: BigDecimal?
        get() = amountText
            .replace(",", ".")
            .toBigDecimalOrNull()

    val isFirstStepValid: Boolean
        get() = parsedAmount
            ?.signum() == 1

    fun usesNewReason(
        hasExistingReasons: Boolean
    ): Boolean {
        return useNewReason ||
                !hasExistingReasons
    }

    fun isValid(
        hasExistingReasons: Boolean
    ): Boolean {
        val reasonIsValid =
            if (usesNewReason(hasExistingReasons)) {
                newReason.isNotBlank()
            } else {
                reasonId != null
            }

        return isFirstStepValid &&
                categoryId != null &&
                reasonIsValid
    }

    fun toCommand(
        userId: Long,
        hasExistingReasons: Boolean
    ): NewTransactionCommand? {
        val amount =
            parsedAmount ?: return null

        val category =
            categoryId ?: return null

        if (!isValid(hasExistingReasons)) {
            return null
        }

        val createReason =
            usesNewReason(hasExistingReasons)

        return NewTransactionCommand(
            userId = userId,
            categoryId = category,
            amount = amount,
            paymentMethod = paymentMethod,
            type = type,
            reasonId = if (createReason) {
                null
            } else {
                reasonId
            },
            newReason = if (createReason) {
                newReason.trim()
            } else {
                null
            },
            savingGoalId = savingGoalId
        )
    }
}

val CreateTransactionFormStateSaver:
        Saver<CreateTransactionFormState, Any> =
    listSaver(
        save = { form ->
            listOf(
                form.type.name,
                form.amountText,
                form.categoryId ?: -1L,
                form.reasonId ?: -1L,
                form.useNewReason,
                form.newReason,
                form.paymentMethod.name,
                form.savingGoalId ?: -1L
            )
        },
        restore = { values ->
            CreateTransactionFormState(
                type = runCatching {
                    MovementType.valueOf(
                        values[0] as String
                    )
                }.getOrDefault(
                    MovementType.SPENT
                ),
                amountText =
                    values[1] as String,
                categoryId =
                    (values[2] as Long)
                        .takeIf { it >= 0 },
                reasonId =
                    (values[3] as Long)
                        .takeIf { it >= 0 },
                useNewReason =
                    values[4] as Boolean,
                newReason =
                    values[5] as String,
                paymentMethod = runCatching {
                    PaymentMethod.valueOf(
                        values[6] as String
                    )
                }.getOrDefault(
                    PaymentMethod.CASH
                ),
                savingGoalId =
                    (values[7] as Long)
                        .takeIf { it >= 0 }
            )
        }
    )
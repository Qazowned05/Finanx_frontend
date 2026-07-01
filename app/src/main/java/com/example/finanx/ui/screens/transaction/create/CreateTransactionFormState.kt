package com.example.finanx.ui.screens.transaction.create

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.example.finanx.data.model.TransactionType
import com.example.finanx.data.model.NewTransactionCommand
import com.example.finanx.data.model.NewTransactionSchedule
import com.example.finanx.data.model.PaymentMethod
import com.example.finanx.data.model.TransactionRecurrenceType
import java.math.BigDecimal
import java.time.LocalDate

data class CreateTransactionFormState(
    val type: TransactionType = TransactionType.SPENT,

    val amountText: String = "",

    val categoryId: Long? = null,

    val reasonId: Long? = null,

    val useNewReason: Boolean = false,

    val newReason: String = "",

    val paymentMethod: PaymentMethod =
        PaymentMethod.CASH,

    val savingGoalId: Long? = null,

    val isScheduled: Boolean = false,

    val recurrenceType: TransactionRecurrenceType =
        TransactionRecurrenceType.MONTHLY,

    val recurrenceIntervalText: String = "1",

    val scheduleStartDateText: String =
        LocalDate.now().toString(),

    val scheduleEndDateText: String =
        LocalDate.now().plusYears(1).toString()
) {

    val parsedAmount: BigDecimal?
        get() = amountText
            .replace(",", ".")
            .toBigDecimalOrNull()

    val isFirstStepValid: Boolean
        get() = parsedAmount
            ?.signum() == 1

    val parsedRecurrenceInterval: Int?
        get() = recurrenceIntervalText
            .toIntOrNull()
            ?.takeIf { it > 0 }

    val parsedScheduleStartDate: LocalDate?
        get() = scheduleStartDateText
            .toLocalDateOrNull()

    val parsedScheduleEndDate: LocalDate?
        get() = scheduleEndDateText
            .toLocalDateOrNull()

    val isScheduleValid: Boolean
        get() {
            if (!isScheduled) {
                return true
            }

            val startDate =
                parsedScheduleStartDate ?: return false

            val endDate =
                parsedScheduleEndDate ?: return false

            return parsedRecurrenceInterval != null &&
                    !endDate.isBefore(startDate)
        }

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
                reasonIsValid &&
                isScheduleValid
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

        val startDate =
            parsedScheduleStartDate

        val endDate =
            parsedScheduleEndDate

        val interval =
            parsedRecurrenceInterval

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
            savingGoalId = if (isScheduled) {
                null
            } else {
                savingGoalId
            },
            schedule = if (
                isScheduled &&
                startDate != null &&
                endDate != null &&
                interval != null
            ) {
                NewTransactionSchedule(
                    recurrenceType = recurrenceType,
                    interval = interval,
                    startDate = startDate,
                    endDate = endDate,
                    dayOfWeek = startDate.dayOfWeek
                )
            } else {
                null
            }
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
                form.savingGoalId ?: -1L,
                form.isScheduled,
                form.recurrenceType.name,
                form.recurrenceIntervalText,
                form.scheduleStartDateText,
                form.scheduleEndDateText
            )
        },
        restore = { values ->
            CreateTransactionFormState(
                type = runCatching {
                    TransactionType.valueOf(
                        values[0] as String
                    )
                }.getOrDefault(
                    TransactionType.SPENT
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
                        .takeIf { it >= 0 },
                isScheduled =
                    values.getOrNull(8) as? Boolean
                        ?: false,
                recurrenceType = runCatching {
                    TransactionRecurrenceType.valueOf(
                        values.getOrNull(9) as String
                    )
                }.getOrDefault(
                    TransactionRecurrenceType.MONTHLY
                ),
                recurrenceIntervalText =
                    values.getOrNull(10) as? String
                        ?: "1",
                scheduleStartDateText =
                    values.getOrNull(11) as? String
                        ?: LocalDate.now().toString(),
                scheduleEndDateText =
                    values.getOrNull(12) as? String
                        ?: LocalDate.now().plusYears(1).toString()
            )
        }
    )

private fun String.toLocalDateOrNull(): LocalDate? =
    runCatching {
        LocalDate.parse(trim())
    }.getOrNull()

package com.elias.mismonedas.ui.screens.transaction.create

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.elias.mismonedas.data.model.NewTransactionCommand
import com.elias.mismonedas.data.model.NewTransactionSchedule
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.TransactionRequest
import java.math.BigDecimal
import java.time.LocalDate

data class CreateTransactionFormState(
    val type: TransactionRequest.Type = TransactionRequest.Type.SPENT,

    val amountText: String = "",

    val categoryId: Long? = null,

    val reasonId: Long? = null,

    val newReason: String = "",

    val paymentMethod: TransactionRequest.PaymentMethod =
        TransactionRequest.PaymentMethod.CASH,

    val savingGoalId: Long? = null,

    val isScheduled: Boolean = false,

    val recurrenceType: RecurrenceRuleRequest.RecurrenceType =
        RecurrenceRuleRequest.RecurrenceType.MONTHLY,

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

    fun isValid(
        hasExistingReasons: Boolean = false
    ): Boolean {
        return isFirstStepValid &&
                categoryId != null &&
                isScheduleValid
    }

    fun toCommand(
        userId: Long,
        walletId: Long? = null,
        hasExistingReasons: Boolean = false
    ): NewTransactionCommand? {
        val amount =
            parsedAmount ?: return null

        val category =
            categoryId ?: return null

        if (!isValid(hasExistingReasons)) {
            return null
        }

        val createReason =
            reasonId == null

        val startDate =
            parsedScheduleStartDate

        val endDate =
            parsedScheduleEndDate

        val interval =
            parsedRecurrenceInterval

        return NewTransactionCommand(
            userId = userId,
            walletId = walletId,
            categoryId = category,
            amount = amount,
            paymentMethod = paymentMethod,
            type = type,
            reasonId = reasonId,
            newReason = if (createReason) {
                newReason.trim().takeIf(String::isNotBlank)
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
                    TransactionRequest.Type.valueOf(
                        values[0] as String
                    )
                }.getOrDefault(
                    TransactionRequest.Type.SPENT
                ),
                amountText =
                    values[1] as String,
                categoryId =
                    (values[2] as Long)
                        .takeIf { it >= 0 },
                reasonId =
                    (values[3] as Long)
                        .takeIf { it >= 0 },
                newReason =
                    values[4] as String,
                paymentMethod = runCatching {
                    TransactionRequest.PaymentMethod.valueOf(
                        values[5] as String
                    )
                }.getOrDefault(
                    TransactionRequest.PaymentMethod.CASH
                ),
                savingGoalId =
                    (values[6] as Long)
                        .takeIf { it >= 0 },
                isScheduled =
                    values.getOrNull(7) as? Boolean
                        ?: false,
                recurrenceType = runCatching {
                    RecurrenceRuleRequest.RecurrenceType.valueOf(
                        values.getOrNull(8) as String
                    )
                }.getOrDefault(
                    RecurrenceRuleRequest.RecurrenceType.MONTHLY
                ),
                recurrenceIntervalText =
                    values.getOrNull(9) as? String
                        ?: "1",
                scheduleStartDateText =
                    values.getOrNull(10) as? String
                        ?: LocalDate.now().toString(),
                scheduleEndDateText =
                    values.getOrNull(11) as? String
                        ?: LocalDate.now().plusYears(1).toString()
            )
        }
    )

private fun String.toLocalDateOrNull(): LocalDate? =
    runCatching {
        LocalDate.parse(trim())
    }.getOrNull()

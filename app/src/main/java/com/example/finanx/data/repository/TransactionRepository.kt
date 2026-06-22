package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.model.MovementType
import com.example.finanx.data.model.NewTransactionCommand
import com.example.finanx.data.model.PaymentMethod
import com.example.finanx.data.model.ScheduledTransactionRecord
import com.example.finanx.data.model.SelectOption
import com.example.finanx.data.model.TransactionData
import com.example.finanx.data.model.TransactionFormOptions
import com.example.finanx.data.model.TransactionRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.CategoryControllerApi
import org.openapitools.client.apis.ReasonControllerApi
import org.openapitools.client.apis.SavingGoalControllerApi
import org.openapitools.client.apis.ScheduleControllerApi
import org.openapitools.client.apis.TransactionControllerApi
import org.openapitools.client.apis.TransactionScheduleControllerApi
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse
import org.openapitools.client.models.TransactionScheduleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import org.openapitools.client.models.Update2Request
import retrofit2.Response
import java.io.IOException
import java.math.BigDecimal
import java.time.DayOfWeek
class TransactionRepository(
    private val transactionApi: TransactionControllerApi =
        RetrofitClient.transactionApi,
    private val categoryApi: CategoryControllerApi =
        RetrofitClient.categoryApi,
    private val reasonApi: ReasonControllerApi =
        RetrofitClient.reasonApi,
    private val savingGoalApi: SavingGoalControllerApi =
        RetrofitClient.savingGoalApi,
    private val transactionScheduleApi: TransactionScheduleControllerApi =
        RetrofitClient.transactionScheduleApi,
    private val scheduleApi: ScheduleControllerApi =
        RetrofitClient.scheduleApi
) {

    suspend fun loadTransactionData(userId: Long): TransactionData =
        withContext(Dispatchers.IO) {
            coroutineScope {
            val transactionsDeferred = async {
                transactionApi
                    .findAllActiveByUser(userId)
                    .bodyOrThrow("No se pudieron obtener los movimientos")
            }

            val categoriesDeferred = async {
                categoryApi
                    .findAllByUser3(userId)
                    .bodyOrThrow("No se pudieron obtener las categorías")
            }

            val reasonsDeferred = async {
                reasonApi
                    .findAllByUser1(userId)
                    .bodyOrThrow("No se pudieron obtener los motivos")
            }

            val goalsDeferred = async {
                savingGoalApi
                    .findAllActiveByUser1(userId)
                    .bodyOrThrow("No se pudieron obtener las metas")
            }

            val categories = categoriesDeferred.await()
            val reasons = reasonsDeferred.await()
            val goals = goalsDeferred.await()

            val categoryNames = categories
                .mapNotNull { category ->
                    val id = category.id ?: return@mapNotNull null
                    id to category.name.orEmpty()
                }
                .toMap()

            val reasonNames = reasons
                .mapNotNull { reason ->
                    val id = reason.id ?: return@mapNotNull null
                    id to reason.description.orEmpty()
                }
                .toMap()

            val savingGoalNames = goals
                .mapNotNull { goal ->
                    val id = goal.id ?: return@mapNotNull null
                    id to goal.description.orEmpty()
                }
                .toMap()

            val transactions = transactionsDeferred.await()
                .mapNotNull { response ->
                    response.toRecord(
                        categoryNames = categoryNames,
                        reasonNames = reasonNames,
                        savingGoalNames = savingGoalNames
                    )
                }
                .sortedByDescending { it.createdAt }

            val options = TransactionFormOptions(
                categories = categories
                    .filter { it.active == true }
                    .mapNotNull { category ->
                        val id = category.id ?: return@mapNotNull null
                        SelectOption(
                            id = id,
                            label = category.name ?: "Categoría $id"
                        )
                    }
                    .sortedBy { it.label },

                reasons = reasons
                    .filter { it.active == true }
                    .mapNotNull { reason ->
                        val id = reason.id ?: return@mapNotNull null
                        SelectOption(
                            id = id,
                            label = reason.description ?: "Motivo $id"
                        )
                    }
                    .sortedBy { it.label },

                savingGoals = goals
                    .filter { it.active == true && it.completed != true }
                    .mapNotNull { goal ->
                        val id = goal.id ?: return@mapNotNull null
                        SelectOption(
                            id = id,
                            label = goal.description ?: "Meta $id"
                        )
                    }
                    .sortedBy { it.label }
            )

            TransactionData(
                transactions = transactions,
                formOptions = options
            )
            }
        }

    suspend fun loadScheduledTransactions(
        userId: Long
    ): List<ScheduledTransactionRecord> = withContext(Dispatchers.IO) {
        coroutineScope {
            val runningDeferred = async {
                transactionScheduleApi
                    .findAllByUserAndState1(
                        idUser = userId,
                        state = TransactionScheduleControllerApi
                            .StateFindAllByUserAndState1
                            .RUNNING
                            .value
                    )
                    .bodyOrThrow("No se pudieron obtener las programaciones")
            }

            val pausedDeferred = async {
                transactionScheduleApi
                    .findAllByUserAndState1(
                        idUser = userId,
                        state = TransactionScheduleControllerApi
                            .StateFindAllByUserAndState1
                            .PAUSED
                            .value
                    )
                    .bodyOrThrow("No se pudieron obtener las programaciones pausadas")
            }

            (runningDeferred.await() + pausedDeferred.await())
                .mapNotNull(TransactionScheduleResponse::toRecord)
                .sortedBy { it.nextRunAt }
        }
    }

    suspend fun createTransaction(
        command: NewTransactionCommand
    ): Unit = withContext(Dispatchers.IO) {
        if (command.schedule != null) {
            scheduleApi
                .create1(command.toScheduleRequest())
                .bodyOrThrow("No se pudo registrar la programación")
            return@withContext
        }

        val request = command.toRequest()

        transactionApi
            .create(request)
            .bodyOrThrow("No se pudo registrar el movimiento")
    }

    suspend fun updateTransaction(
        transactionId: Long,
        command: NewTransactionCommand
    ): TransactionRecord = withContext(Dispatchers.IO) {
        val updated = transactionApi
            .update1(
                id = transactionId,
                transactionRequest = command.toRequest()
            )
            .bodyOrThrow("No se pudo actualizar el movimiento")

        updated.toRecord(
            categoryNames = emptyMap(),
            reasonNames = emptyMap(),
            savingGoalNames = emptyMap()
        ) ?: throw ApiException("La respuesta del servidor es inválida")
    }

    suspend fun disableTransaction(transactionId: Long) = withContext(Dispatchers.IO) {
        transactionApi
            .disable(transactionId)
            .ensureSuccessful("No se pudo eliminar el movimiento")
    }

    suspend fun pauseSchedule(scheduleId: Long) = withContext(Dispatchers.IO) {
        scheduleApi
            .pause(scheduleId)
            .ensureSuccessful("No se pudo pausar la programación")
    }

    suspend fun resumeSchedule(scheduleId: Long) = withContext(Dispatchers.IO) {
        scheduleApi
            .resume(scheduleId)
            .ensureSuccessful("No se pudo reanudar la programación")
    }

    suspend fun cancelSchedule(scheduleId: Long) = withContext(Dispatchers.IO) {
        scheduleApi
            .cancel(scheduleId)
            .ensureSuccessful("No se pudo cancelar la programación")
    }
}

class ApiException(
    override val message: String
) : IOException(message)

private fun NewTransactionCommand.toRequest(): TransactionRequest =
    TransactionRequest(
        userId = userId,
        categoryId = categoryId,
        amount = amount,
        paymentMethod = TransactionRequest.PaymentMethod.valueOf(
            paymentMethod.name
        ),
        type = TransactionRequest.Type.valueOf(type.name),
        reasonId = reasonId,
        description = newReason
            ?.trim()
            ?.takeIf(String::isNotBlank),
        savingGoalId = savingGoalId
    )

private fun NewTransactionCommand.toScheduleRequest(): Update2Request {
    val scheduleConfig =
        schedule ?: throw ApiException("Falta configurar la recurrencia")

    return Update2Request(
        userId = userId,
        categoryId = categoryId,
        type = "TRANSACTION",
        alertable = false,
        percentAlert = 0,
        limitAmount = amount,
        description = newReason?.trim()?.takeIf(String::isNotBlank) ?: "Programación",
        amount = amount,
        paymentMethod = Update2Request.PaymentMethod.valueOf(paymentMethod.name),
        transactionType = Update2Request.TransactionType.valueOf(type.name),
        recurrenceRule = RecurrenceRuleRequest(
            recurrenceType = RecurrenceRuleRequest.RecurrenceType.valueOf(
                scheduleConfig.recurrenceType.name
            ),
            interval = scheduleConfig.interval,
            start = scheduleConfig.startDate,
            end = scheduleConfig.endDate,
            dayOfWeek = scheduleConfig.dayOfWeek?.toRecurrenceDayOfWeek()
        ),
        reasonId = reasonId
    )
}

private fun DayOfWeek.toRecurrenceDayOfWeek(): RecurrenceRuleRequest.DayOfWeek =
    RecurrenceRuleRequest.DayOfWeek.valueOf(name)

private fun TransactionResponse.toRecord(
    categoryNames: Map<Long, String>,
    reasonNames: Map<Long, String>,
    savingGoalNames: Map<Long, String>
): TransactionRecord? {
    val transactionId = id ?: return null
    val responseType = type ?: return null

    return TransactionRecord(
        id = transactionId,
        categoryId = categoryId,
        categoryName = categoryId
            ?.let(categoryNames::get)
            ?.takeIf(String::isNotBlank)
            ?: "Sin categoría",
        savingGoalId = savingGoalId,
        savingGoalName = savingGoalId
            ?.let(savingGoalNames::get)
            ?.takeIf(String::isNotBlank),
        reasonId = reasonId,
        description = reasonId
            ?.let(reasonNames::get)
            ?.takeIf(String::isNotBlank)
            ?: "Movimiento",
        amount = amount ?: BigDecimal.ZERO,
        paymentMethod = PaymentMethod.valueOf(
            paymentMethod?.name ?: PaymentMethod.OTHER.name
        ),
        type = MovementType.valueOf(responseType.name),
        createdAt = createdAt,
        active = active == true
    )
}

private fun TransactionScheduleResponse.toRecord(): ScheduledTransactionRecord? {
    val scheduleId = id ?: return null
    val responseType = transactionType ?: return null

    return ScheduledTransactionRecord(
        id = scheduleId,
        categoryName = categoryName ?: "Sin categoría",
        description = reasonDesc ?: "Movimiento programado",
        amount = amount ?: BigDecimal.ZERO,
        paymentMethod = PaymentMethod.valueOf(
            paymentMethod?.name ?: PaymentMethod.OTHER.name
        ),
        type = MovementType.valueOf(responseType.name),
        state = state?.name ?: "CREATED",
        nextRunAt = nextRunAt
    )
}

private fun <T> Response<T>.bodyOrThrow(defaultMessage: String): T {
    if (!isSuccessful) {
        throw ApiException(extractErrorMessage(defaultMessage))
    }
    return body() ?: throw ApiException("$defaultMessage: respuesta vacía")
}

private fun Response<*>.ensureSuccessful(defaultMessage: String) {
    if (!isSuccessful) {
        throw ApiException(extractErrorMessage(defaultMessage))
    }
}

private fun Response<*>.extractErrorMessage(defaultMessage: String): String {
    val content = runCatching {
        errorBody()?.string()
    }.getOrNull().orEmpty()

    val backendMessage = Regex(
        """"message"\s*:\s*"([^"]+)""""
    ).find(content)?.groupValues?.getOrNull(1)

    return backendMessage ?: "$defaultMessage (${code()})"
}

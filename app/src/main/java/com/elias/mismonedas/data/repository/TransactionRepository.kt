package com.elias.mismonedas.data.repository

import com.elias.mismonedas.data.model.CategoryOption
import com.elias.mismonedas.data.model.NewTransactionCommand
import com.elias.mismonedas.data.model.SelectOption
import com.elias.mismonedas.data.model.TransactionData
import com.elias.mismonedas.data.model.TransactionFormOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.CategoriesApi
import org.openapitools.client.apis.ReasonsApi
import org.openapitools.client.apis.SavingsApi
import org.openapitools.client.apis.ScheduleApi
import org.openapitools.client.apis.ScheduleTransactionsApi
import org.openapitools.client.apis.TransactionsApi
import org.openapitools.client.apis.WalletsApi
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse
import org.openapitools.client.models.TransactionScheduleRequest
import retrofit2.Response
import java.io.IOException
import java.time.DayOfWeek
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionApi: TransactionsApi,
    private val categoryApi: CategoriesApi,
    private val reasonApi: ReasonsApi,
    private val savingGoalApi: SavingsApi,
    private val transactionScheduleApi: ScheduleTransactionsApi,
    private val scheduleApi: ScheduleApi,
    private val walletsApi: WalletsApi
) {

    suspend fun loadTransactionData(userId: Long): TransactionData =
        withContext(Dispatchers.IO) {
            coroutineScope {
                val transactionsDeferred = async {
                    transactionApi
                        .findAllActiveTransactionByUser(userId)
                        .bodyOrThrow("No se pudieron obtener los movimientos")
                }

                val categoriesDeferred = async {
                    categoryApi
                        .findAllCategoriesByUser(userId)
                        .bodyOrThrow("No se pudieron obtener las categorías")
                }

                val reasonsDeferred = async {
                    reasonApi
                        .findAllReasonByUser(userId)
                        .bodyOrThrow("No se pudieron obtener los motivos")
                }

                val goalsDeferred = async {
                    savingGoalApi
                        .findAllActiveSavingGoalByUser(userId)
                        .bodyOrThrow("No se pudieron obtener las metas")
                }

                val categories = categoriesDeferred.await()
                val reasons = reasonsDeferred.await()
                val goals = goalsDeferred.await()

                val transactions = transactionsDeferred.await()
                    .sortedByDescending { it.createdAt }

                val options = TransactionFormOptions(
                    categories = categories
                        .filter { it.active == true }
                        .mapNotNull { category ->
                            val id = category.id ?: return@mapNotNull null
                            val typeName = category.type?.name ?: return@mapNotNull null
                            CategoryOption(
                                id = id,
                                label = category.name ?: "Categoría $id",
                                type = TransactionRequest.Type.valueOf(typeName)
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

    suspend fun createTransaction(
        command: NewTransactionCommand
    ): Unit = withContext(Dispatchers.IO) {
        val walletId = command.walletId ?: resolvePrimaryWalletId(command.userId)

        if (command.schedule != null) {
            scheduleApi.createSchedule(command.toScheduleRequest(walletId))
                .bodyOrThrow("No se pudo registrar la programación")
            return@withContext
        }

        transactionApi
            .createTransaction(command.toRequest(walletId))
            .bodyOrThrow("No se pudo registrar el movimiento")
    }

    suspend fun updateTransaction(
        transactionId: Long,
        command: NewTransactionCommand
    ): TransactionResponse = withContext(Dispatchers.IO) {
        val walletId = command.walletId ?: resolvePrimaryWalletId(command.userId)
        transactionApi
            .updateTransaction(
                id = transactionId,
                transactionRequest = command.toRequest(walletId)
            )
            .bodyOrThrow("No se pudo actualizar el movimiento")
    }

    suspend fun disableTransaction(transactionId: Long) = withContext(Dispatchers.IO) {
        transactionApi
            .transactionDelete(transactionId)
            .ensureSuccessful("No se pudo eliminar el movimiento")
    }

    private suspend fun resolvePrimaryWalletId(userId: Long): Long {
        val wallets = walletsApi
            .findAllByUser(userId)
            .bodyOrThrow("No se pudieron obtener las billeteras")
        return wallets.firstOrNull { it.primary == true }?.id
            ?: wallets.firstOrNull()?.id
            ?: throw ApiException("No se encontró ninguna billetera")
    }

    suspend fun getPrimaryWalletCurrency(userId: Long): String {
        val wallets = walletsApi
            .findAllByUser(userId)
            .bodyOrThrow("No se pudieron obtener las billeteras")
        return wallets.firstOrNull { it.primary == true }?.currencyCode
            ?: wallets.firstOrNull()?.currencyCode
            ?: "PEN"
    }
}

class ApiException(
    override val message: String
) : IOException(message)

private fun NewTransactionCommand.toRequest(walletId: Long): TransactionRequest =
    TransactionRequest(
        walletId = walletId,
        categoryId = categoryId,
        amount = amount,
        paymentMethod = paymentMethod,
        type = type,
        reasonId = reasonId,
        description = newReason
            ?.trim()
            ?.takeIf(String::isNotBlank),
        savingGoalId = savingGoalId,
        receiptId = receiptId,
        convertCurrency = convertCurrency
    )

private fun NewTransactionCommand.toScheduleRequest(walletId: Long?): TransactionScheduleRequest {
    val scheduleConfig =
        schedule ?: throw ApiException("Falta configurar la recurrencia")

    return TransactionScheduleRequest(
        userId = userId,
        categoryId = categoryId,
        type = "TRANSACTION",
        description = newReason?.trim()?.takeIf(String::isNotBlank) ?: "Programación",
        amount = amount,
        paymentMethod = TransactionScheduleRequest.PaymentMethod.valueOf(paymentMethod.name),
        transactionType = TransactionScheduleRequest.TransactionType.valueOf(type.name),
        recurrenceRule = RecurrenceRuleRequest(
            recurrenceType = scheduleConfig.recurrenceType,
            interval = scheduleConfig.interval,
            start = scheduleConfig.startDate,
            end = scheduleConfig.endDate,
            dayOfWeek = scheduleConfig.dayOfWeek?.toRecurrenceDayOfWeek()
        ),
        walletId = walletId,
        reasonId = reasonId
    )
}

private fun DayOfWeek.toRecurrenceDayOfWeek(): RecurrenceRuleRequest.DayOfWeek =
    RecurrenceRuleRequest.DayOfWeek.valueOf(name)

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

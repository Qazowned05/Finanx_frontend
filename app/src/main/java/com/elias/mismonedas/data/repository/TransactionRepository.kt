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
import org.openapitools.client.models.PageResponseTransactionResponse
import org.openapitools.client.models.PaymentMethod
import org.openapitools.client.models.RecurrenceRuleRequest
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse
import org.openapitools.client.models.TransactionScheduleRequest
import org.openapitools.client.models.TransactionType
import org.openapitools.client.models.WalletResponse
import retrofit2.Response
import java.io.IOException
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionApi: TransactionsApi,
    private val categoryApi: CategoriesApi,
    private val reasonApi: ReasonsApi,
    private val savingGoalApi: SavingsApi,
    private val scheduleApi: ScheduleApi,
    private val walletsApi: WalletsApi
) {

    suspend fun loadTransactionData(): TransactionData =
        withContext(Dispatchers.IO) {
            coroutineScope {
                val transactionsDeferred = async {
                    transactionApi
                        .listTransactions()
                        .bodyOrThrow("No se pudieron obtener los movimientos")
                }

                val options = loadFormOptions()

                val transactions = transactionsDeferred.await().items
                    ?.sortedByDescending { it.createdAt }
                    ?: emptyList()

                TransactionData(
                    transactions = transactions,
                    formOptions = options
                )
            }
        }

    suspend fun loadFormOptions(): TransactionFormOptions =
        withContext(Dispatchers.IO) {
            coroutineScope {
                val categoriesDeferred = async {
                    categoryApi
                        .listCategories(size = 200)
                        .bodyOrThrow("No se pudieron obtener las categorías")
                }

                val reasonsDeferred = async {
                    reasonApi
                        .listReasons(size = 200)
                        .bodyOrThrow("No se pudieron obtener los motivos")
                }

                val goalsDeferred = async {
                    savingGoalApi
                        .listSavingGoals(size = 200)
                        .bodyOrThrow("No se pudieron obtener las metas")
                }

                val categories = categoriesDeferred.await()
                val reasons = reasonsDeferred.await()
                val goals = goalsDeferred.await()

                TransactionFormOptions(
                    categories = categories.items
                        ?.filter { it.active == true }
                        ?.mapNotNull { category ->
                            val id = category.id ?: return@mapNotNull null
                            val typeName = category.type?.name ?: return@mapNotNull null
                            CategoryOption(
                                id = id,
                                label = category.name ?: "Categoría $id",
                                type = TransactionType.valueOf(typeName)
                            )
                        }
                        ?.sortedBy { it.label } ?: emptyList(),

                    reasons = reasons.items
                        ?.filter { it.active == true }
                        ?.mapNotNull { reason ->
                            val id = reason.id ?: return@mapNotNull null
                            SelectOption(
                                id = id,
                                label = reason.description ?: "Motivo $id"
                            )
                        }
                        ?.sortedBy { it.label } ?: emptyList(),

                    savingGoals = goals.items
                        ?.filter { it.active == true && it.completed != true }
                        ?.mapNotNull { goal ->
                            val id = goal.id ?: return@mapNotNull null
                            SelectOption(
                                id = id,
                                label = goal.description ?: "Meta $id"
                            )
                        }
                        ?.sortedBy { it.label } ?: emptyList()
                )
            }
        }

    suspend fun createTransaction(
        command: NewTransactionCommand
    ): Unit = withContext(Dispatchers.IO) {
        val walletId = command.walletId ?: resolvePrimaryWalletId()

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
        val walletId = command.walletId ?: resolvePrimaryWalletId()
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

    private suspend fun resolvePrimaryWalletId(): Long {
        val wallets = walletsApi
            .listWallets()
            .bodyOrThrow("No se pudieron obtener las billeteras")
        return wallets.items?.firstOrNull { it.primary == true }?.id
            ?: wallets.items?.firstOrNull()?.id
            ?: throw ApiException("No se encontró ninguna billetera")
    }

    suspend fun getPrimaryWalletCurrency(): String {
        val wallets = walletsApi
            .listWallets()
            .bodyOrThrow("No se pudieron obtener las billeteras")
        return wallets.items?.firstOrNull { it.primary == true }?.currencyCode
            ?: wallets.items?.firstOrNull()?.currencyCode
            ?: "PEN"
    }

    suspend fun listTransactionPage(
        type: TransactionType? = null,
        walletId: Long? = null,
        categoryId: Long? = null,
        from: LocalDate? = null,
        to: LocalDate? = null,
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE
    ): PageResponseTransactionResponse =
        withContext(Dispatchers.IO) {
            transactionApi
                .listTransactions(
                    active = true,
                    type = type,
                    walletId = walletId,
                    categoryId = categoryId,
                    from = from,
                    to = to,
                    page = page,
                    size = size
                )
                .bodyOrThrow("No se pudieron obtener los movimientos")
        }

    suspend fun listWallets(): List<WalletResponse> =
        withContext(Dispatchers.IO) {
            walletsApi
                .listWallets(size = 200)
                .bodyOrThrow("No se pudieron obtener las billeteras")
                .items
                ?: emptyList()
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
        paymentMethod = PaymentMethod.valueOf(paymentMethod.name),
        transactionType = TransactionType.valueOf(type.name),
        recurrenceRule = RecurrenceRuleRequest(
            recurrenceType = scheduleConfig.recurrenceType,
            interval = scheduleConfig.interval,
            start = scheduleConfig.startDate,
            end = scheduleConfig.endDate,
            dayOfWeek = scheduleConfig.dayOfWeek
        ),
        walletId = walletId,
        reasonId = reasonId
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

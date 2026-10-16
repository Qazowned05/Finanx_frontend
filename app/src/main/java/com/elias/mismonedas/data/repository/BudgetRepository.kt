package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.AnalyticsApi
import org.openapitools.client.apis.BudgetsApi
import org.openapitools.client.models.BudgetHealth
import org.openapitools.client.models.BudgetRequest
import org.openapitools.client.models.BudgetResponse
import org.openapitools.client.models.BudgetState
import org.openapitools.client.models.PageResponseBudgetExecutionResponse
import org.openapitools.client.models.PageResponseBudgetResponse
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val api: BudgetsApi,
    private val analyticsApi: AnalyticsApi
) {
    suspend fun list(): Response<PageResponseBudgetResponse> =
        withContext(Dispatchers.IO) { api.listBudgets(active = true) }

    suspend fun listBudgetPage(
        active: Boolean = true,
        state: BudgetState? = null,
        health: BudgetHealth? = null,
        from: LocalDate? = null,
        to: LocalDate? = null,
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE,
        sort: List<String> = listOf("end,ASC")
    ): PageResponseBudgetResponse =
        withContext(Dispatchers.IO) {
            api.listBudgets(
                active = active,
                state = state,
                health = health,
                from = from,
                to = to,
                page = page,
                size = size,
                sort = sort
            ).bodyOrThrow("No se pudieron obtener los presupuestos")
        }

    suspend fun listDetailed(from: LocalDate, to: LocalDate): Response<PageResponseBudgetExecutionResponse> =
        withContext(Dispatchers.IO) { analyticsApi.getBudgetsExecutionsByPeriod(from = from, to = to) }

    suspend fun getBudgetById(id: Long): Response<BudgetResponse> =
        withContext(Dispatchers.IO) { api.findBudgetById(id) }

    suspend fun createBudget(request: BudgetRequest): Response<BudgetResponse> =
        withContext(Dispatchers.IO) { api.createBudget(request) }

    suspend fun updateBudget(id: Long, request: BudgetRequest): Response<BudgetResponse> =
        withContext(Dispatchers.IO) { api.updateBudget(id, request) }

    suspend fun cancelBudget(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.cancelBudget(id) }

    suspend fun disableBudget(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disableBudget(id) }

}

private fun <T> Response<T>.bodyOrThrow(defaultMessage: String): T {
    if (!isSuccessful) {
        throw ApiException(extractErrorMessage(defaultMessage))
    }
    return body() ?: throw ApiException("$defaultMessage: respuesta vacía")
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

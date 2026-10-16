package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.SavingsApi
import org.openapitools.client.models.PageResponseSavingGoalResponse
import org.openapitools.client.models.SavingGoalRequest
import org.openapitools.client.models.SavingGoalResponse
import org.openapitools.client.models.SavingGoalState
import retrofit2.Response
import javax.inject.Inject

class SavingGoalRepository @Inject constructor(
    private val api: SavingsApi
) {

    suspend fun list(active: Boolean?): Response<PageResponseSavingGoalResponse> =
        withContext(Dispatchers.IO) { api.listSavingGoals(active = active) }

    suspend fun listSavingGoalPage(
        active: Boolean? = true,
        state: SavingGoalState? = null,
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE
    ): PageResponseSavingGoalResponse =
        withContext(Dispatchers.IO) {
            api.listSavingGoals(
                active = active,
                state = state,
                page = page,
                size = size
            ).savingGoalBodyOrThrow("No se pudieron obtener las metas")
        }

    suspend fun getSavingGoalById(id: Long): Response<SavingGoalResponse> =
        withContext(Dispatchers.IO) { api.findSavingGoalById(id) }

    suspend fun createSavingGoal(request: SavingGoalRequest): Response<SavingGoalResponse> =
        withContext(Dispatchers.IO) { api.createSavingGoal(request) }

    suspend fun updateSavingGoal(id: Long, request: SavingGoalRequest): Response<SavingGoalResponse> =
        withContext(Dispatchers.IO) { api.updateSavingGoal(id, request) }

    suspend fun disableSavingGoal(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disableSavingGoal(id) }

    suspend fun cancelSavingGoal(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.cancelSavingGoal(id) }
}

private fun <T> Response<T>.savingGoalBodyOrThrow(defaultMessage: String): T {
    if (!isSuccessful) {
        throw ApiException(savingGoalErrorMessage(defaultMessage))
    }
    return body() ?: throw ApiException("$defaultMessage: respuesta vacía")
}

private fun Response<*>.savingGoalErrorMessage(defaultMessage: String): String {
    val content = runCatching {
        errorBody()?.string()
    }.getOrNull().orEmpty()

    val backendMessage = Regex(
        """"message"\s*:\s*"([^"]+)""""
    ).find(content)?.groupValues?.getOrNull(1)

    return backendMessage ?: "$defaultMessage (${code()})"
}

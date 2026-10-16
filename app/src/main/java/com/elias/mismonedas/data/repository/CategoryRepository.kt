package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.CategoriesApi
import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse
import org.openapitools.client.models.PageResponseCategoryResponse
import org.openapitools.client.models.TransactionType
import retrofit2.Response
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val api: CategoriesApi
) {
    suspend fun list(
        active: Boolean,
        type: TransactionType?,
        size: Int = 200
    ): Response<PageResponseCategoryResponse> =
        withContext(Dispatchers.IO) { api.listCategories(active = active, type = type, size = size) }

    suspend fun listCategoriesPage(
        active: Boolean? = true,
        type: TransactionType? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE
    ): PageResponseCategoryResponse =
        withContext(Dispatchers.IO) {
            api.listCategories(
                active = active,
                type = type,
                search = search,
                page = page,
                size = size
            ).categoryBodyOrThrow("No se pudieron obtener las categorías")
        }

    suspend fun createCategory(request: CategoryRequest): Response<CategoryResponse> =
        withContext(Dispatchers.IO) { api.createCategory(request) }

    suspend fun updateCategory(id: Long, request: CategoryRequest): Response<CategoryResponse> =
        withContext(Dispatchers.IO) { api.updateCategory(id, request) }

    suspend fun disableCategory(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disableCategory(id) }
}

private fun <T> Response<T>.categoryBodyOrThrow(defaultMessage: String): T {
    if (!isSuccessful) {
        throw ApiException(categoryErrorMessage(defaultMessage))
    }
    return body() ?: throw ApiException("$defaultMessage: respuesta vacía")
}

private fun Response<*>.categoryErrorMessage(defaultMessage: String): String {
    val content = runCatching {
        errorBody()?.string()
    }.getOrNull().orEmpty()

    val backendMessage = Regex(
        """"message"\s*:\s*"([^"]+)""""
    ).find(content)?.groupValues?.getOrNull(1)

    return backendMessage ?: "$defaultMessage (${code()})"
}

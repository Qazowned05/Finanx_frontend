package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.CategoryControllerApi
import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CategoryRepository(
    private val api: CategoryControllerApi = RetrofitClient.categoryApi
) {
    suspend fun getCategoriesByActive(userId: Long, active: Boolean): Response<List<CategoryResponse>> =
        withContext(Dispatchers.IO) { api.findAllByUserAndActive(userId, active) }

    suspend fun getActiveCategories(userId: Long): Response<List<CategoryResponse>> =
        withContext(Dispatchers.IO) { api.findAllByUserAndActive(idUser = userId, active = true) }

    suspend fun createCategory(request: CategoryRequest): Response<CategoryResponse> =
        withContext(Dispatchers.IO) { api.create4(request) }

    suspend fun updateCategory(id: Long, request: CategoryRequest): Response<CategoryResponse> =
        withContext(Dispatchers.IO) { api.update5(id, request) }

    suspend fun disableCategory(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disable4(id) }
}

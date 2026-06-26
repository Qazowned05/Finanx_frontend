package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.CategoriesApi
import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse
import retrofit2.Response

class CategoryRepository(
    private val api: CategoriesApi = RetrofitClient.categoryApi
) {
    suspend fun getCategoriesByActive(userId: Long, active: Boolean): Response<List<CategoryResponse>> =
        withContext(Dispatchers.IO) { api.findAllCategoriesyUserAndActive(userId, active) }

    suspend fun getActiveCategories(userId: Long): Response<List<CategoryResponse>> =
        withContext(Dispatchers.IO) { api.findAllCategoriesyUserAndActive(idUser = userId, active = true) }

    suspend fun createCategory(request: CategoryRequest): Response<CategoryResponse> =
        withContext(Dispatchers.IO) { api.createCategory(request) }

    suspend fun updateCategory(id: Long, request: CategoryRequest): Response<CategoryResponse> =
        withContext(Dispatchers.IO) { api.updateCategory(id, request) }

    suspend fun disableCategory(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.disableCategory(id) }
}

package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.CategoryControllerApi
import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse
import retrofit2.Response

class CategoryRepository(
    private val api: CategoryControllerApi = RetrofitClient.categoryApi
) {

    suspend fun getCategories(userId: Long): Response<List<CategoryResponse>> {
        return api.findCategoriesByUserAndActive(userId, true)
    }

    suspend fun getCategoriesByActive(userId: Long, active: Boolean): Response<List<CategoryResponse>> {
        return api.findCategoriesByUserAndActive(userId, active)
    }

    suspend fun getActiveCategories(userId: Long): Response<List<CategoryResponse>> {
        return api.findCategoriesByUserAndActive(idUser = userId, active = true)
    }

    suspend fun createCategory(request: CategoryRequest): Response<CategoryResponse> {
        return api.createCategory(request)
    }

    suspend fun updateCategory(id: Long, request: CategoryRequest): Response<CategoryResponse> {
        return api.updateCategory(id, request)
    }

    suspend fun disableCategory(id: Long): Response<Unit> {
        return api.disableCategory(id)
    }

    suspend fun activateCategory(id: Long): Response<CategoryResponse> {
        return api.activateCategory(id)
    }
}
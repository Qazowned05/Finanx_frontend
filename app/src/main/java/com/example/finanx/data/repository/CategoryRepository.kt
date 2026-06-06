package com.example.finanx.data.repository

import org.openapitools.client.apis.CategoryControllerApi
import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse
import retrofit2.Response

class CategoryRepository(private val api: CategoryControllerApi) {

    suspend fun getCategories(userId: Long): Response<List<CategoryResponse>> {
        return api.findAllByUser2(userId)
    }

    suspend fun getCategoriesByActive(userId: Long, active: Boolean): Response<List<CategoryResponse>> {
        return api.findAllByUserAndActive(userId, active)
    }



    suspend fun createCategory(request: CategoryRequest): Response<CategoryResponse> {
        return api.create4(request)
    }

    suspend fun updateCategory(id: Long, request: CategoryRequest): Response<CategoryResponse> {
        return api.update5(id, request)
    }

    suspend fun disableCategory(id: Long): Response<Unit> {
        return api.disable4(id)
    }

    suspend fun activateCategory(id: Long): Response<CategoryResponse> {
        return api.activate(id)
    }
}

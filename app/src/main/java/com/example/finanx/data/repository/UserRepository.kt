package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.UserControllerApi
import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.UserRequest
import org.openapitools.client.models.UserResponse
import retrofit2.Response

class UserRepository(
    private val api: UserControllerApi = RetrofitClient.userApi
) {
    suspend fun findById(id: Long): Response<UserResponse> {
        return api.findById(id)
    }

    suspend fun findAll(): Response<List<UserResponse>> {
        return api.findAll()
    }

    suspend fun update(id: Long, request: UserRequest): Response<UserResponse> {
        return api.update(id, request)
    }

    suspend fun deactivate(id: Long): Response<UserResponse> {
        return api.disable2(id)
    }

    suspend fun enable(id: Long): Response<UserResponse> {
        return api.enable(id)
    }

    suspend fun block(id: Long): Response<UserResponse> {
        return api.block(id)
    }

    suspend fun searchByName(query: String): Response<List<UserResponse>> {
        return api.searchByName(query)
    }

    suspend fun listTimeZones(): Response<List<String>> {
        return api.listTimeZones()
    }

    suspend fun registerAccount(request: UserRequest): Response<LoginResponse> {
        return api.register(request)
    }
}

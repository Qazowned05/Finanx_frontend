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
        return api.findUserById(id)
    }

    suspend fun findAll(): Response<List<UserResponse>> {
        return api.findAllUsers()
    }

    suspend fun update(id: Long, request: UserRequest): Response<UserResponse> {
        return api.updateUser(id, request)
    }

    suspend fun deactivate(id: Long): Response<UserResponse> {
        return api.disableUser(id)
    }

    suspend fun activate(id: Long): Response<UserResponse> {
        return api.activateUser(id)
    }

    suspend fun block(id: Long): Response<UserResponse> {
        return api.blockUser(id)
    }

    suspend fun searchByName(query: String): Response<List<UserResponse>> {
        return api.searchUsersByName(query)
    }

    suspend fun listTimeZones(): Response<List<String>> {
        return api.listTimeZones()
    }

    suspend fun registerAccount(request: UserRequest): Response<LoginResponse> {
        return api.registerUserAccount(request)
    }
}

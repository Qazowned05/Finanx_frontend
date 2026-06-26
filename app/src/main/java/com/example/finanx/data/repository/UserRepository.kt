package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.UsersApi
import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.UserRequest
import org.openapitools.client.models.UserResponse
import retrofit2.Response

class UserRepository(
    private val api: UsersApi = RetrofitClient.userApi
) {
    suspend fun findById(id: Long): Response<UserResponse> =
        withContext(Dispatchers.IO) { api.findUserById(id) }

    suspend fun findAll(): Response<List<UserResponse>> =
        withContext(Dispatchers.IO) { api.findAllUser() }

    suspend fun update(id: Long, request: UserRequest): Response<UserResponse> =
        withContext(Dispatchers.IO) { api.updateUser(id, request) }

    suspend fun disable(id: Long): Response<UserResponse> =
        withContext(Dispatchers.IO) { api.disableUser(id) }

    suspend fun enable(id: Long): Response<UserResponse> =
        withContext(Dispatchers.IO) { api.enableUser(id) }

    suspend fun delete(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.deleteUser(id) }

    suspend fun resetData(id: Long): Response<Unit> =
        withContext(Dispatchers.IO) { api.resetUserData(id) }

    suspend fun block(id: Long): Response<UserResponse> =
        withContext(Dispatchers.IO) { api.blockUser(id) }

    suspend fun searchByName(query: String): Response<List<UserResponse>> =
        withContext(Dispatchers.IO) { api.searchUserByName(query) }

    suspend fun listTimeZones(): Response<List<String>> =
        withContext(Dispatchers.IO) { api.listTimeZones() }

    suspend fun registerAccount(request: UserRequest): Response<LoginResponse> =
        withContext(Dispatchers.IO) { api.registerUser(request) }
}

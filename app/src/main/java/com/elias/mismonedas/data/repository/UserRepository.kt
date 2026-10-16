package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.UsersApi
import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.PageResponseUserResponse
import org.openapitools.client.models.TimeZone
import org.openapitools.client.models.UserRequest
import org.openapitools.client.models.UserResponse
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UsersApi
) {
    suspend fun findById(id: Long): Response<UserResponse> =
        withContext(Dispatchers.IO) { api.findUserById(id) }

    suspend fun list(): Response<PageResponseUserResponse> =
        withContext(Dispatchers.IO) { api.listUsers() }

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

    suspend fun listTimeZones(): Response<List<TimeZone>> =
        withContext(Dispatchers.IO) { api.listTimeZones() }

    suspend fun registerAccount(request: UserRequest): Response<LoginResponse> =
        withContext(Dispatchers.IO) { api.registerUser(request) }
}

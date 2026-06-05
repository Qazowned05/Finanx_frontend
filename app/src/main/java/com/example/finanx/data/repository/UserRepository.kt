package com.example.finanx.data.repository

import org.openapitools.client.apis.UserControllerApi
import org.openapitools.client.models.UserRequest
import org.openapitools.client.models.UserResponse
import retrofit2.Response

class UserRepository(private val api: UserControllerApi) {
    suspend fun findById(id: Long): Response<UserResponse> {
        return api.findById(id)
    }

    suspend fun update(id: Long, request: UserRequest): Response<UserResponse> {
        return api.update(id, request)
    }

    suspend fun deactivate(id: Long): Response<UserResponse> {
        return api.disable3(id)
    }
}

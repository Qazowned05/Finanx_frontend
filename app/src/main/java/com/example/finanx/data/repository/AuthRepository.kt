package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.AuthControllerApi
import org.openapitools.client.models.GoogleLoginRequest
import org.openapitools.client.models.LoginRequest
import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.UserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthRepository(
    private val api: AuthControllerApi = RetrofitClient.authApi
) {
    suspend fun login(email: String, password: String): Response<LoginResponse> =
        withContext(Dispatchers.IO) { api.login(LoginRequest(email, password)) }

    suspend fun loginWithGoogle(idToken: String): Response<LoginResponse> =
        withContext(Dispatchers.IO) { api.google(GoogleLoginRequest(idToken = idToken)) }

    suspend fun register(
        username: String,
        name: String,
        lastname: String,
        email: String,
        password: String,
        timeZone: UserRequest.TimeZone
    ): Response<LoginResponse> = withContext(Dispatchers.IO) {
        val user = UserRequest(
            username = username,
            name = name,
            lastname = lastname,
            email = email,
            password = password,
            timeZone = timeZone
        )

        api.register1(user)
    }
}

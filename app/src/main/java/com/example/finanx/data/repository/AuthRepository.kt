package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.AuthApi
import org.openapitools.client.models.GoogleLoginRequest
import org.openapitools.client.models.LoginRequest
import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.UserRequest
import retrofit2.Response

class AuthRepository(
    private val api: AuthApi = RetrofitClient.authApi
) {
    suspend fun login(email: String, password: String): Response<LoginResponse> =
        withContext(Dispatchers.IO) { api.login(LoginRequest(email, password)) }

    suspend fun loginWithGoogle(idToken: String): Response<LoginResponse> =
        withContext(Dispatchers.IO) { api.authWithGoogle(GoogleLoginRequest(idToken = idToken)) }

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

        api.authRegister(user)
    }
}

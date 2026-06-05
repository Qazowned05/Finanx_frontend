package com.example.finanx.data.repository

import org.openapitools.client.apis.AuthControllerApi
import org.openapitools.client.models.GoogleLoginRequest
import org.openapitools.client.models.LoginRequest
import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.UserRequest
import retrofit2.Response
import kotlin.math.log

class AuthRepository(private val api: AuthControllerApi) {
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }

    suspend fun loginWithGoogle(idToken: String): Response<LoginResponse> {
        return api.google(GoogleLoginRequest(idToken = idToken))
    }

    suspend fun register(name: String, email: String, password: String): Response<LoginResponse> {
        val user = UserRequest(
            name = name,
            lastname = "User",
            email = email,
            username = email.substringBefore("@"),
            password = password,
            timeZone = UserRequest.TimeZone.AMERICA_LIMA
        )
        println("registering user: $user")

        return api.register1(user)
    }
}

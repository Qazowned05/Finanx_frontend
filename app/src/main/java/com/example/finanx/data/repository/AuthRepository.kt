package com.example.finanx.data.repository

import com.example.finanx.data.api.FinanxApi
import com.example.finanx.data.model.*
import retrofit2.Response

class AuthRepository(private val api: FinanxApi) {
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }

    suspend fun register(name: String, email: String, password: String): Response<LoginResponse> {
        return api.register(
            UserRequest(
                name = name,
                lastname = "User", // Valor por defecto para pruebas
                email = email,
                username = email.substringBefore("@"),
                password = password,
                timeZone = "AMERICA_LIMA"
            )
        )
    }
}

package com.example.finanx.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class UserRequest(
    @Json(name = "name") val name: String,
    @Json(name = "lastname") val lastname: String,
    @Json(name = "email") val email: String,
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String,
    @Json(name = "timeZone") val timeZone: String = "AMERICA_LIMA"
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "user") val user: UserResponse,
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "lastname") val lastname: String,
    @Json(name = "email") val email: String,
    @Json(name = "username") val username: String,
    @Json(name = "moneyBalance") val moneyBalance: Double,
    @Json(name = "role") val role: String?,
    @Json(name = "state") val state: String?
)

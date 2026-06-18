package com.example.finanx.data.api

data class ApiError(
    val method: String? = null,
    val path: String? = null,
    val message: String? = null,
    val status: Int? = null,
    val timestamp: String? = null
)

fun retrofit2.Response<*>.parseErrorMessage(): String {
    val raw = errorBody()?.string()
    if (raw.isNullOrBlank()) return "Código ${code()}"

    return try {
        val error = org.openapitools.client.infrastructure.Serializer.moshi
            .adapter(ApiError::class.java)
            .fromJson(raw)
        error?.message ?: "Código ${code()}"
    } catch (e: Exception) {
        "Código ${code()}"
    }
}
package com.example.finanx.data.api

object TokenManager {
    var token: String? = null
        private set

    fun setToken(newToken: String) {
        token = newToken
    }

    fun clear() {
        token = null
    }
}

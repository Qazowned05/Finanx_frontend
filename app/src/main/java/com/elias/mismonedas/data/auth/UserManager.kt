package com.elias.mismonedas.data.auth

import org.openapitools.client.models.UserResponse

object UserManager {
    var userId: Long = 0L
    var currentUser: UserResponse? = null
    var loginType: LoginType = LoginType.LOCAL
}

enum class LoginType {
    GOOGLE, LOCAL
}
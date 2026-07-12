package com.elias.mismonedas.ui

import org.openapitools.client.models.UserResponse


fun UserResponse.displayName(): String {
    val nameAndLastname = listOfNotNull(
        name?.takeIf(String::isNotBlank),
        lastname?.takeIf(String::isNotBlank)
    ).joinToString(" ")

    return fullName?.takeIf(String::isNotBlank)
        ?: nameAndLastname.takeIf(String::isNotBlank)
        ?: username?.takeIf(String::isNotBlank)
        ?: "Usuario"
}
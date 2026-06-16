package com.example.finanx.data.auth

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialProviderConfigurationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

class GoogleAuthManager private constructor(
    private val credentialManager: CredentialManager
) {

    companion object {
        fun create(context: Context): GoogleAuthManager =
            GoogleAuthManager(CredentialManager.create(context))
    }

    /**
     * Flujo de "hoja inferior" recomendado:
     * 1) Intenta con cuentas previamente autorizadas (autoSelect opcional).
     * 2) Si no hay credenciales (NoCredentialException), reintenta mostrando todas las cuentas.
     */
    suspend fun signInWithBottomSheet(
        activity: Activity,
        serverClientId: String,
        enableAutoSelect: Boolean = true,
        nonce: String = generateSecureRandomNonce()
    ): GoogleAuthOutcome {
        val authorizedOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(enableAutoSelect)
            .setNonce(nonce)
            .build()

        val authorizedRequest = GetCredentialRequest.Builder()
            .addCredentialOption(authorizedOption)
            .build()

        return try {
            val response = credentialManager.getCredential(
                request = authorizedRequest,
                context = activity
            )
            handleSignInResponse(response)
        } catch (e: NoCredentialException) {
            val allAccountsOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .setNonce(nonce)
                .build()

            val allAccountsRequest = GetCredentialRequest.Builder()
                .addCredentialOption(allAccountsOption)
                .build()

            try {
                val response = credentialManager.getCredential(
                    request = allAccountsRequest,
                    context = activity
                )
                handleSignInResponse(response)
            } catch (inner: GetCredentialException) {
                inner.toOutcome()
            }
        } catch (e: GetCredentialException) {
            e.toOutcome()
        }
    }

    /**
     * Flujo de botón: fuerza "Sign in with Google" (útil si el usuario descartó la hoja
     * inferior o si necesitas que elija una cuenta explícitamente).
     */
    suspend fun signInWithButton(
        activity: Activity,
        serverClientId: String,
        nonce: String = generateSecureRandomNonce()
    ): GoogleAuthOutcome {
        val option = GetSignInWithGoogleOption.Builder(serverClientId)
            .setNonce(nonce)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()

        return try {
            val response = credentialManager.getCredential(
                request = request,
                context = activity
            )
            handleSignInResponse(response)
        } catch (e: GetCredentialException) {
            e.toOutcome()
        }
    }

    /** Borra el estado de credenciales para permitir elegir otra cuenta en el siguiente login. */
    suspend fun signOut() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    private fun handleSignInResponse(response: GetCredentialResponse): GoogleAuthOutcome {
        val credential = response.credential

        return when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        GoogleAuthOutcome.Success(
                            GoogleSignInResult(
                                idToken = googleCredential.idToken,
                                subject = googleCredential.id,
                                displayName = googleCredential.displayName,
                                familyName = googleCredential.familyName,
                                givenName = googleCredential.givenName,
                                profilePictureUri = googleCredential.profilePictureUri
                            )
                        )
                    } catch (_: GoogleIdTokenParsingException) {
                        GoogleAuthOutcome.Failure(GoogleAuthError.TokenParsingFailed)
                    }
                } else {
                    GoogleAuthOutcome.Failure(GoogleAuthError.UnexpectedCredential)
                }
            }

            else -> GoogleAuthOutcome.Failure(GoogleAuthError.UnexpectedCredential)
        }
    }
}

/** Resultado del intento de login. */
sealed class GoogleAuthOutcome {
    data class Success(val result: GoogleSignInResult) : GoogleAuthOutcome()
    data class Failure(val error: GoogleAuthError, val cause: Throwable? = null) : GoogleAuthOutcome()
}

private fun GetCredentialException.toOutcome(): GoogleAuthOutcome {
    val error = when (this) {
        is GetCredentialCancellationException -> GoogleAuthError.Cancelled
        is NoCredentialException -> GoogleAuthError.NoCredential
        is GetCredentialProviderConfigurationException -> GoogleAuthError.ProviderConfiguration
        else -> GoogleAuthError.Unknown(this.message)
    }
    return GoogleAuthOutcome.Failure(error, this)
}


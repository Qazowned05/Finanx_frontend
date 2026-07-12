package com.elias.mismonedas.data.auth

import android.util.Base64
import java.security.SecureRandom

/**
 * Genera un nonce aleatorio criptográficamente seguro y lo codifica en Base64 URL-safe.
 * Útil para evitar ataques de replay cuando el backend valida el idToken.
 */
fun generateSecureRandomNonce(byteLength: Int = 32): String {
    val randomBytes = ByteArray(byteLength)
    SecureRandom().nextBytes(randomBytes)
    return Base64.encodeToString(
        randomBytes,
        Base64.NO_WRAP or Base64.URL_SAFE or Base64.NO_PADDING
    )
}


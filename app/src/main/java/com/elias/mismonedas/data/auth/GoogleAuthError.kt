package com.elias.mismonedas.data.auth

/**
 * Errores comunes del flujo "Acceder con Google" usando Credential Manager.
 *
 * Nota: el mensaje visible al usuario debería construirse en la UI (o ViewModel)
 * a partir de este tipo, para poder internacionalizar.
 */
sealed class GoogleAuthError {
    /** El usuario cerró/canceló la hoja inferior o el diálogo. */
    data object Cancelled : GoogleAuthError()

    /** No hay credenciales disponibles en el dispositivo para la solicitud actual. */
    data object NoCredential : GoogleAuthError()

    /** La credencial recibida no es del tipo esperado. */
    data object UnexpectedCredential : GoogleAuthError()

    /** Se recibió un ID token pero no se pudo parsear. */
    data object TokenParsingFailed : GoogleAuthError()

    /** Error de configuración del proveedor (p.ej. Play Services no disponible / mal configurado). */
    data object ProviderConfiguration : GoogleAuthError()

    /** Cualquier otro error. */
    data class Unknown(val reason: String? = null) : GoogleAuthError()
}


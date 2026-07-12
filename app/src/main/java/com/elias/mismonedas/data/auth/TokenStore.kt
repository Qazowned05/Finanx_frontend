package com.elias.mismonedas.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit
import org.json.JSONObject

/**
 * Almacén persistente del JWT.
 *
 * Usa [androidx.security.crypto.EncryptedSharedPreferences] con cifrado AES-256 en reposo —
 * el token sobrevive a cierres de app y reinicios de proceso.
 *
 * Inicializar con [TokenStore.init] en Application.onCreate() antes
 * de cualquier llamada a Retrofit.
 */
object TokenStore {

    private const val PREFS_FILE = "finanx_secure_prefs"
    private const val KEY_TOKEN   = "jwt_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_LOGIN_TYPE = "login_type"
    private const val KEY_FCM_TOKEN = "fcm_token"
    private const val KEY_LAST_ACTIVITY = "last_activity_ms"

    /** Buffer de 30 segundos antes de que el JWT expire. */
    private const val JWT_EXPIRY_BUFFER_MS = 30_000L

    /** FIX-004: 30 minutos de inactividad para timeout de sesión. */
    private const val SESSION_TIMEOUT_MS = 30 * 60 * 1000L

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        prefs = EncryptedSharedPreferences.create(
            context,
            PREFS_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var token: String
        get() = prefs.getString(KEY_TOKEN, "") ?: ""
        set(value) = prefs.edit { putString(KEY_TOKEN, value) }

    var userId: Long
        get() = prefs.getLong(KEY_USER_ID, 0L)
        set(value) = prefs.edit().putLong(KEY_USER_ID, value).apply()

    var loginType: String
        get() = prefs.getString(KEY_LOGIN_TYPE, "LOCAL") ?: "LOCAL"
        set(value) = prefs.edit { putString(KEY_LOGIN_TYPE, value) }

    var fcmToken: String
        get() = prefs.getString(KEY_FCM_TOKEN, "") ?: ""
        set(value) = prefs.edit { putString(KEY_FCM_TOKEN, value) }

    /** FIX-004: Timestamp (ms) de la última actividad del usuario. */
    var lastActivityMs: Long
        get() = prefs.getLong(KEY_LAST_ACTIVITY, 0L)
        set(value) = prefs.edit { putLong(KEY_LAST_ACTIVITY, value) }

    /** FIX-004: Actualiza el timestamp de última actividad. */
    fun updateLastActivity() {
        lastActivityMs = System.currentTimeMillis()
    }

    /** FIX-004: True si pasaron más de 30 minutos sin actividad. */
    val isSessionExpired: Boolean
        get() {
            val last = lastActivityMs
            if (last == 0L) return false
            return System.currentTimeMillis() - last > SESSION_TIMEOUT_MS
        }

    /** FIX-002: Verifica si el JWT tiene un `exp` válido (sin red). */
    private fun isTokenExpired(): Boolean {
        val jwt = token
        if (jwt.isEmpty()) return true
        return try {
            val parts = jwt.split(".")
            if (parts.size < 3) return true
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)
            val expMs = json.getLong("exp") * 1000
            System.currentTimeMillis() >= expMs - JWT_EXPIRY_BUFFER_MS
        } catch (_: Exception) {
            true
        }
    }

    val isLoggedIn: Boolean
        get() = token.isNotEmpty() && !isTokenExpired()

    /** FIX-001: Limpia toda la sesión incluyendo FCM token y last activity. */
    fun clear() {
        prefs.edit {
            remove(KEY_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_LOGIN_TYPE)
            remove(KEY_FCM_TOKEN)
            remove(KEY_LAST_ACTIVITY)
        }
    }
}
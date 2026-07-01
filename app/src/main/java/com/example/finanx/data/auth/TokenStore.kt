package com.example.finanx.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

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

    val isLoggedIn: Boolean
        get() = token.isNotEmpty()

    fun clear() {
        prefs.edit {
            remove(KEY_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_LOGIN_TYPE)
        }
    }
}
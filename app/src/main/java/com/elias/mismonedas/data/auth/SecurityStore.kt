package com.elias.mismonedas.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest

object SecurityStore {

    private const val PREFS_FILE = "finanx_security_prefs"
    private const val KEY_PIN_HASH = "pin_hash"
    private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
    private const val KEY_FAILED_ATTEMPTS = "failed_attempts"
    private const val KEY_LAST_FAILED_ATTEMPT = "last_failed_attempt"
    private const val KEY_USER_ID = "user_id"

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

    var pinHash: String?
        get() = prefs.getString(KEY_PIN_HASH, null)
        set(value) {
            if (value != null) prefs.edit().putString(KEY_PIN_HASH, value).apply()
            else prefs.edit().remove(KEY_PIN_HASH).apply()
        }

    var biometricEnabled: Boolean
        get() = prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, value).apply()

    var failedAttempts: Int
        get() = prefs.getInt(KEY_FAILED_ATTEMPTS, 0)
        set(value) = prefs.edit().putInt(KEY_FAILED_ATTEMPTS, value).apply()

    var lastFailedAttempt: Long
        get() = prefs.getLong(KEY_LAST_FAILED_ATTEMPT, 0L)
        set(value) = prefs.edit().putLong(KEY_LAST_FAILED_ATTEMPT, value).apply()

    var userId: Long
        get() = prefs.getLong(KEY_USER_ID, -1L)
        set(value) = prefs.edit().putLong(KEY_USER_ID, value).apply()

    fun isLockEnabledForUser(currentUserId: Long): Boolean =
        pinHash != null && userId == currentUserId

    val isLockEnabled: Boolean get() = pinHash != null

    fun hashPin(pin: String): String =
        MessageDigest.getInstance("SHA-256").digest(pin.toByteArray()).joinToString("") { "%02x".format(it) }

    fun verifyPin(pin: String): Boolean =
        pinHash != null && pinHash == hashPin(pin)

    fun setPin(pin: String, currentUserId: Long) {
        pinHash = hashPin(pin)
        userId = currentUserId
        failedAttempts = 0
        lastFailedAttempt = 0L
    }

    fun clearPin() {
        pinHash = null
        biometricEnabled = false
        failedAttempts = 0
        lastFailedAttempt = 0L
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}

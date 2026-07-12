package com.elias.mismonedas.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.openapitools.client.models.TransactionRequest

object TransactionQuickPrefs {
    private const val PREFS_NAME = "transaction_quick_prefs"
    private const val KEY_LAST_TYPE = "last_type"
    private const val KEY_LAST_CATEGORY_SPENT = "last_category_SPENT"
    private const val KEY_LAST_CATEGORY_INCOME = "last_category_INCOME"
    private const val KEY_LAST_PAYMENT_METHOD = "last_payment_method"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        prefs = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun rememberSelection(
        type: TransactionRequest.Type,
        categoryId: Long,
        paymentMethod: TransactionRequest.PaymentMethod
    ) {
        prefs.edit()
            .putString(KEY_LAST_TYPE, type.name)
            .putLong(categoryKey(type), categoryId)
            .putString(KEY_LAST_PAYMENT_METHOD, paymentMethod.name)
            .apply()
    }

    fun lastTransactionType(): TransactionRequest.Type? {
        val value = prefs.getString(KEY_LAST_TYPE, null) ?: return null
        return runCatching { TransactionRequest.Type.valueOf(value) }.getOrNull()
    }

    fun lastCategoryId(type: TransactionRequest.Type): Long? {
        val key = categoryKey(type)
        val value = prefs.getLong(key, -1L)
        return if (value >= 0) value else null
    }

    fun lastPaymentMethod(): TransactionRequest.PaymentMethod? {
        val value = prefs.getString(KEY_LAST_PAYMENT_METHOD, null) ?: return null
        return runCatching { TransactionRequest.PaymentMethod.valueOf(value) }.getOrNull()
    }

    private fun categoryKey(type: TransactionRequest.Type): String =
        when (type) {
            TransactionRequest.Type.SPENT -> KEY_LAST_CATEGORY_SPENT
            TransactionRequest.Type.INCOME -> KEY_LAST_CATEGORY_INCOME
        }
}

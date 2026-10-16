package com.elias.mismonedas.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.openapitools.client.models.PaymentMethod
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionType

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
        type: TransactionType,
        categoryId: Long,
        paymentMethod: PaymentMethod
    ) {
        prefs.edit()
            .putString(KEY_LAST_TYPE, type.name)
            .putLong(categoryKey(type), categoryId)
            .putString(KEY_LAST_PAYMENT_METHOD, paymentMethod.name)
            .apply()
    }

    fun lastTransactionType(): TransactionType? {
        val value = prefs.getString(KEY_LAST_TYPE, null) ?: return null
        return runCatching { TransactionType.valueOf(value) }.getOrNull()
    }

    fun lastCategoryId(type: TransactionType): Long? {
        val key = categoryKey(type)
        val value = prefs.getLong(key, -1L)
        return if (value >= 0) value else null
    }

    fun lastPaymentMethod(): PaymentMethod? {
        val value = prefs.getString(KEY_LAST_PAYMENT_METHOD, null) ?: return null
        return runCatching { PaymentMethod.valueOf(value) }.getOrNull()
    }

    private fun categoryKey(type: TransactionType): String =
        when (type) {
            TransactionType.SPENT -> KEY_LAST_CATEGORY_SPENT
            TransactionType.INCOME -> KEY_LAST_CATEGORY_INCOME
        }
}

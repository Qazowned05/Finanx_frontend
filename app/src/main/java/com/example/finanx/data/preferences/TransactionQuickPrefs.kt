package com.example.finanx.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.finanx.data.model.PaymentMethod
import com.example.finanx.data.model.TransactionType

object TransactionQuickPrefs {

    private const val PREFS_FILE = "finanx_transaction_quick_prefs"
    private const val KEY_LAST_TYPE = "last_type"
    private const val KEY_LAST_PAYMENT_METHOD = "last_payment_method"
    private const val KEY_LAST_CATEGORY_SPENT = "last_category_spent"
    private const val KEY_LAST_CATEGORY_INCOME = "last_category_income"

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

    fun rememberSelection(
        type: TransactionType,
        categoryId: Long,
        paymentMethod: PaymentMethod
    ) {
        prefs.edit {
            putString(KEY_LAST_TYPE, type.name)
            putLong(categoryKey(type), categoryId)
            putString(KEY_LAST_PAYMENT_METHOD, paymentMethod.name)
        }
    }

    fun lastTransactionType(): TransactionType? {
        val value = prefs.getString(KEY_LAST_TYPE, null) ?: return null
        return runCatching { TransactionType.valueOf(value) }.getOrNull()
    }

    fun lastCategoryId(type: TransactionType): Long? {
        val value = prefs.getLong(categoryKey(type), -1L)
        return value.takeIf { it >= 0 }
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

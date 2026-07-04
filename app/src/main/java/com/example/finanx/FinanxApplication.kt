package com.example.finanx

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.finanx.data.auth.TokenStore
import com.example.finanx.data.preferences.TransactionQuickPrefs

/**
 * Application entry point.
 * Inicializa [TokenStore] antes de que cualquier componente
 * intente leer o escribir el JWT.
 *
 * Registrar en AndroidManifest.xml:
 *   <application android:name=".FinanxApplication" ...>
 */
class FinanxApplication : Application() {
    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()
        TokenStore.init(this)
        TransactionQuickPrefs.init(this)
    }
}

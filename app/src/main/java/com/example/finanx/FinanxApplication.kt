package com.example.finanx

import android.app.Application
import com.example.finanx.data.auth.TokenStore

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
        super.onCreate()
        TokenStore.init(this)
    }
}
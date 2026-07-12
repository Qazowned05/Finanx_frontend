package com.elias.mismonedas

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.elias.mismonedas.data.auth.SecurityStore
import com.elias.mismonedas.data.auth.TokenStore
import com.elias.mismonedas.data.preferences.TransactionQuickPrefs
import com.elias.mismonedas.data.preferences.TutorialPrefs
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MisMonedasApplication : Application() {
    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()
        TokenStore.init(this)
        SecurityStore.init(this)
        TransactionQuickPrefs.init(this)
        TutorialPrefs.init(this)
    }
}

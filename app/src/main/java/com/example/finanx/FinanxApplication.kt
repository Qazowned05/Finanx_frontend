package com.example.finanx

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.finanx.data.auth.TokenStore
import com.example.finanx.data.local.TutorialDao
import com.example.finanx.data.preferences.TransactionQuickPrefs
import com.example.finanx.ui.components.TutorialPreferences
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FinanxApplication : Application() {
    @Inject lateinit var tutorialDao: TutorialDao

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()
        TokenStore.init(this)
        TransactionQuickPrefs.init(this)
        TutorialPreferences.init(tutorialDao)
    }
}

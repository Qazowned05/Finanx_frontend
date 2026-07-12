package com.elias.mismonedas.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object TutorialPrefs {
    private const val PREFS_FILE = "finanx_tutorial_prefs"
    private const val KEY_TUTORIAL_PREFIX = "tutorial_"
    private const val GROUP_ONBOARDING = "onboarding_sequence"
    private const val GROUP_REGISTRATION = "registration_complete"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
    }

    private fun key(userId: Long, group: String): String =
        "${KEY_TUTORIAL_PREFIX}${userId}_$group"

    fun isCompleted(userId: Long, group: String): Flow<Boolean> = flow {
        emit(prefs.getBoolean(key(userId, group), false))
    }

    suspend fun markCompleted(userId: Long, group: String) = withContext(Dispatchers.IO) {
        prefs.edit { putBoolean(key(userId, group), true) }
    }

    suspend fun clear(userId: Long) = withContext(Dispatchers.IO) {
        prefs.edit {
            remove(key(userId, GROUP_ONBOARDING))
            remove(key(userId, GROUP_REGISTRATION))
        }
    }

    fun isOnboardingCompleted(userId: Long): Flow<Boolean> =
        isCompleted(userId, GROUP_ONBOARDING)

    suspend fun markOnboardingCompleted(userId: Long) =
        markCompleted(userId, GROUP_ONBOARDING)

    fun isRegistrationCompleted(userId: Long): Flow<Boolean> =
        isCompleted(userId, GROUP_REGISTRATION)

    suspend fun markRegistrationCompleted(userId: Long) =
        markCompleted(userId, GROUP_REGISTRATION)
}

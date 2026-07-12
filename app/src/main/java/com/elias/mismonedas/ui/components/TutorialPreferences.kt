package com.elias.mismonedas.ui.components

import com.elias.mismonedas.data.preferences.TutorialPrefs
import kotlinx.coroutines.flow.Flow

object TutorialPreferences {
    fun isCompleted(userId: Long, group: String): Flow<Boolean> =
        TutorialPrefs.isCompleted(userId, group)

    suspend fun markCompleted(userId: Long, group: String) {
        TutorialPrefs.markCompleted(userId, group)
    }

    suspend fun clear(userId: Long) {
        TutorialPrefs.clear(userId)
    }

    fun isOnboardingCompleted(userId: Long): Flow<Boolean> =
        TutorialPrefs.isOnboardingCompleted(userId)

    suspend fun markOnboardingCompleted(userId: Long) {
        TutorialPrefs.markOnboardingCompleted(userId)
    }

    fun isRegistrationCompleted(userId: Long): Flow<Boolean> =
        TutorialPrefs.isRegistrationCompleted(userId)

    suspend fun markRegistrationCompleted(userId: Long) {
        TutorialPrefs.markRegistrationCompleted(userId)
    }

    fun currentUserId(): Long =
        com.elias.mismonedas.data.auth.TokenStore.userId
}

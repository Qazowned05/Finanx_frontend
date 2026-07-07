package com.example.finanx.ui.components

import com.example.finanx.data.local.TutorialDao
import com.example.finanx.data.local.TutorialStatus
import kotlinx.coroutines.flow.Flow

object TutorialPreferences {
    private var dao: TutorialDao? = null

    fun init(dao: TutorialDao) {
        this.dao = dao
    }

    fun isCompleted(userId: Long, group: String): Flow<Boolean> {
        return dao?.isCompleted(userId, group) ?: emptyFlow()
    }

    suspend fun markCompleted(userId: Long, group: String) {
        dao?.upsert(
            TutorialStatus(
                userId = userId,
                tutorialGroup = group,
                completed = true,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    fun isOnboardingCompleted(userId: Long): Flow<Boolean> {
        return isCompleted(userId, GROUP_ONBOARDING)
    }

    suspend fun markOnboardingCompleted(userId: Long) {
        markCompleted(userId, GROUP_ONBOARDING)
    }

    fun currentUserId(): Long {
        return com.example.finanx.data.auth.TokenStore.userId
    }

    private const val GROUP_ONBOARDING = "onboarding_sequence"
}

private fun <T> emptyFlow(): Flow<T> = kotlinx.coroutines.flow.emptyFlow()

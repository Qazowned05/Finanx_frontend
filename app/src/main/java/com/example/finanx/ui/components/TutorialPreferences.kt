package com.example.finanx.ui.components

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "tutorial_prefs"

val Context.tutorialDataStore by preferencesDataStore(name = DATASTORE_NAME)

object TutorialPreferences {
    fun isCompleted(context: Context, group: String): Flow<Boolean> {
        val key = booleanPreferencesKey("tutorial_completed_" + group)
        return context.tutorialDataStore.data.map { prefs -> prefs[key] ?: false }
    }

    suspend fun markCompleted(context: Context, group: String) {
        val key = booleanPreferencesKey("tutorial_completed_" + group)
        context.tutorialDataStore.edit { prefs -> prefs[key] = true }
    }
}
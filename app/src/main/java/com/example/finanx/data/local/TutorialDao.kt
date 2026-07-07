package com.example.finanx.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TutorialDao {
    @Query("SELECT COALESCE(completed, 0) AS is_completed FROM tutorial_status WHERE user_id = :userId AND tutorial_group = :group")
    fun isCompleted(userId: Long, group: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(status: TutorialStatus)

    @Query("DELETE FROM tutorial_status WHERE user_id = :userId")
    suspend fun deleteAllForUser(userId: Long)
}

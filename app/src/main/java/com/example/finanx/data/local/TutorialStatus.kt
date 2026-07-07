package com.example.finanx.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "tutorial_status",
    primaryKeys = ["user_id", "tutorial_group"]
)
data class TutorialStatus(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "tutorial_group") val tutorialGroup: String,
    val completed: Boolean = false,
    @ColumnInfo(name = "completed_at") val completedAt: Long? = null
)

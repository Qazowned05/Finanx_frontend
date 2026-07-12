package com.elias.mismonedas.data.local

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase

@Entity(tableName = "cache_metadata")
data class CacheMetadata(
    @PrimaryKey val key: String,
    val lastSyncedAt: Long,
    val etag: String? = null
)

@Dao
interface CacheMetadataDao {
    @Query("SELECT * FROM cache_metadata WHERE `key` = :key")
    suspend fun get(key: String): CacheMetadata?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(metadata: CacheMetadata)

    @Query("DELETE FROM cache_metadata")
    suspend fun clearAll()
}

@Database(
    entities = [CacheMetadata::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cacheMetadataDao(): CacheMetadataDao
}

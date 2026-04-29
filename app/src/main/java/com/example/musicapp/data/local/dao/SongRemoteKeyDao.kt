package com.example.musicapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicapp.data.local.entity.SongCategory
import com.example.musicapp.data.local.entity.SongRemoteKey

@Dao
interface SongRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<SongRemoteKey>)

    @Query("SELECT * FROM song_remote_keys WHERE songId = :songId AND category = :category")
    suspend fun getKey(songId: String, category: SongCategory): SongRemoteKey?

    @Query("DELETE FROM song_remote_keys WHERE category = :category")
    suspend fun clearByCategory(category: SongCategory)
}
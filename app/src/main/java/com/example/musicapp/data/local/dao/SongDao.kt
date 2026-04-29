package com.example.musicapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicapp.data.local.entity.SongCategory
import com.example.musicapp.data.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songs: SongEntity)

    @Query("""
        DELETE FROM songs 
        WHERE category = :category 
        AND timestamp = (SELECT MIN(timestamp) FROM songs WHERE category = :category)
        AND (SELECT COUNT(*) FROM songs WHERE category = :category) > :limit
    """)
    suspend fun deleteOldestIfOverLimit(category: SongCategory, limit: Int)

    @Transaction
    suspend fun insertWithLimit(songs: SongEntity, limit: Int = 20) {
        insert(songs)
        deleteOldestIfOverLimit(songs.category, limit)
    }

    @Query("SELECT * FROM songs WHERE category = :category ORDER BY timestamp DESC")
    fun getSongsByCategory(category: SongCategory): Flow<List<SongEntity>>

    @Query("DELETE FROM songs WHERE id = :id AND category = :category")
    suspend fun delete(id: String, category: SongCategory)

    @Query("DELETE FROM songs WHERE category = :category")
    suspend fun deleteByCategory(category: SongCategory)

    @Query("SELECT EXISTS(SELECT 1 FROM songs WHERE id = :id AND category = 'LIKED')")
    fun isLiked(id: String): Flow<Boolean>


    //paging

    @Query("SELECT * FROM songs WHERE category = :category ORDER BY position ASC")
    fun getSongsPaged(category: SongCategory) : PagingSource<Int, SongEntity>

    @Query("SELECT * FROM songs WHERE category = :category ORDER BY cachedAt ASC LIMIT 1")
    suspend fun getFirstSong(category: SongCategory): SongEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<SongEntity>)

}
package com.example.musicapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicapp.data.local.entity.ArtistCategory
import com.example.musicapp.data.local.entity.ArtistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: ArtistEntity)

    @Query("""
        DELETE FROM artists 
        WHERE category = :category 
        AND timestamp = (SELECT MIN(timestamp) FROM artists WHERE category = :category)
        AND (SELECT COUNT(*) FROM artists WHERE category = :category) > :limit
    """)
    suspend fun deleteOldestIfOverLimit(category: ArtistCategory, limit: Int)

    @Transaction
    suspend fun insertWithLimit(artist: ArtistEntity, limit: Int = 20) {
        insert(artist)
        deleteOldestIfOverLimit(artist.category, limit)
    }

    @Query("SELECT * FROM artists WHERE category = :category ORDER BY timestamp DESC")
    fun getArtistsByCategory(category: ArtistCategory): Flow<List<ArtistEntity>>

    @Query("DELETE FROM artists WHERE id = :id AND category = :category")
    suspend fun delete(id: String, category: ArtistCategory)

    @Query("DELETE FROM artists WHERE category = :category")
    suspend fun deleteByCategory(category: ArtistCategory)
}
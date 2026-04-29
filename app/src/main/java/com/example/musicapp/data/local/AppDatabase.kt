package com.example.musicapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.musicapp.data.local.converter.Converters
import com.example.musicapp.data.local.dao.ArtistDao
import com.example.musicapp.data.local.dao.SongDao
import com.example.musicapp.data.local.dao.SongRemoteKeyDao
import com.example.musicapp.data.local.entity.ArtistEntity
import com.example.musicapp.data.local.entity.SongEntity
import com.example.musicapp.data.local.entity.SongRemoteKey

@Database(
    entities = [SongEntity::class, ArtistEntity::class, SongRemoteKey::class],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun artistDao(): ArtistDao
    abstract fun songRemoteKeyDao(): SongRemoteKeyDao
}


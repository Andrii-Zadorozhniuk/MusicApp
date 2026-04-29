package com.example.musicapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "song_remote_keys",
    primaryKeys = ["songId", "category"]
)
data class SongRemoteKey(
    val songId: String,
    val category: SongCategory,
    val prevOffset: Int?,
    val nextOffset: Int?
)
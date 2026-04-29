package com.example.musicapp.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "songs",
    primaryKeys = ["id", "category"]
)
data class SongEntity(
    val id: String,
    val category: SongCategory,
    val albumId: String,
    val albumImage: String,
    val albumName: String,
    val artistId: String,
    val artistIdstr: String,
    val artistName: String,
    val audio: String?,
    val audiodownload: String,
    val audiodownloadAllowed: Boolean,
    val contentIdFree: Boolean,
    val duration: Int,
    val image: String?,
    val licenseCcurl: String,
    val name: String,
    val position: Int,
    val apiPosition: Int,
    val prourl: String,
    val releasedate: String,
    val shareurl: String,
    val shorturl: String,
    val waveform: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val cachedAt: Long = System.currentTimeMillis()
)

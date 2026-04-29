package com.example.musicapp.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "artists",
    primaryKeys = ["id", "category"]
)
data class ArtistEntity(
    val id: String,
    val image: String,
    val joindate: String,
    val name: String,
    val shareurl: String?,
    val shorturl: String?,
    val website: String?,
    val tags: List<String>,

    val category: ArtistCategory,
    val timestamp: Long = System.currentTimeMillis()
)
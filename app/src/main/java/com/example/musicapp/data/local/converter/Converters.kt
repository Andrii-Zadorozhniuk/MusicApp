package com.example.musicapp.data.local.converter

import androidx.room.TypeConverter
import com.example.musicapp.data.local.entity.ArtistCategory
import com.example.musicapp.data.local.entity.SongCategory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromSongCategory(value: SongCategory): String = value.name

    @TypeConverter
    fun toSongCategory(value: String): SongCategory = SongCategory.valueOf(value)

    @TypeConverter
    fun fromArtistCategory(value: ArtistCategory): String = value.name

    @TypeConverter
    fun toArtistCategory(value: String): ArtistCategory = ArtistCategory.valueOf(value)

    @TypeConverter
    fun fromList(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun toList(value: String): List<String> = Gson().fromJson(value, object: TypeToken<List<String>>() {}.type) ?: emptyList()
}
package com.example.musicapp.domain.manager

import com.example.musicapp.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface PlayerPreferencesManager {
    suspend fun saveLastSong(song: Song)
    fun getLastSong(): Flow<Song?>
}
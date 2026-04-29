package com.example.musicapp.domain.usecases.last_song

import com.example.musicapp.domain.manager.PlayerPreferencesManager
import com.example.musicapp.domain.model.Song

class SaveLastSong(private val manager: PlayerPreferencesManager) {
    suspend operator fun invoke(song: Song) = manager.saveLastSong(song)
}
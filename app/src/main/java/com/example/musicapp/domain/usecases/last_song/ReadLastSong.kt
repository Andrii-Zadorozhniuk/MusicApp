package com.example.musicapp.domain.usecases.last_song

import com.example.musicapp.domain.manager.PlayerPreferencesManager

class ReadLastSong(private val manager: PlayerPreferencesManager) {
    operator fun invoke() = manager.getLastSong()
}
package com.example.musicapp.domain.usecases.local.history

import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.MusicRepository

class AddSongToHistory(private val repository: MusicRepository) {
    suspend operator fun invoke(song: Song) = repository.addSongToHistory(song)
}
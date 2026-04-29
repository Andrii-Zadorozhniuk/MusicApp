package com.example.musicapp.domain.usecases.local.search

import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.MusicRepository

class AddSongToSearchHistory(private val repository: MusicRepository) {
    suspend operator fun invoke(song: Song) = repository.addSongToSearchHistory(song)
}
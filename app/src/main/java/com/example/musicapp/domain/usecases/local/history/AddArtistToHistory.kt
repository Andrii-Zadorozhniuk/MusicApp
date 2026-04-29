package com.example.musicapp.domain.usecases.local.history

import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.repository.MusicRepository

class AddArtistToHistory(private val repository: MusicRepository) {
    suspend operator fun invoke(artist: Artist) = repository.addArtistToHistory(artist)
}
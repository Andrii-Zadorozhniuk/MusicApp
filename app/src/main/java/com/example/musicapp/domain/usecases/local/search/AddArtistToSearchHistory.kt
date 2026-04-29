package com.example.musicapp.domain.usecases.local.search

import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.repository.MusicRepository

class AddArtistToSearchHistory(private val repository: MusicRepository) {
    suspend operator fun invoke(artist: Artist) = repository.addArtistToSearchHistory(artist)
}
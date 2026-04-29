package com.example.musicapp.domain.usecases.network

import com.example.musicapp.domain.repository.MusicRepository

class SearchArtists(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(query: String) = repository.searchArtists(query)
}
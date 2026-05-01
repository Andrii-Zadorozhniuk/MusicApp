package com.example.musicapp.domain.usecases.network

import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.repository.MusicRepository
import com.example.musicapp.domain.exception.Result
class SearchArtists(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(query: String): Result<List<Artist>> = repository.searchArtists(query)
}
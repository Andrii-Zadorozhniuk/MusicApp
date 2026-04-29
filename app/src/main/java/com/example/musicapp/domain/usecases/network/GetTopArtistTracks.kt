package com.example.musicapp.domain.usecases.network

import com.example.musicapp.domain.repository.MusicRepository

class GetTopArtistTracks(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(artistId: String) = repository.getTopArtistTracks(artistId)
}
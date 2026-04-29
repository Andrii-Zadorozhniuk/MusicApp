package com.example.musicapp.domain.usecases.network

import com.example.musicapp.domain.repository.MusicRepository

class GetArtist(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(artistId: String) = repository.getArtist(artistId)
}
package com.example.musicapp.domain.usecases.network

import com.example.musicapp.domain.repository.MusicRepository

class GetArtistAlbums(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(albumId: String) = repository.getArtistAlbums(albumId)
}
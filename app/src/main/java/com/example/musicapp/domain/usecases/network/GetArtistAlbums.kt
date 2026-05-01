package com.example.musicapp.domain.usecases.network

import com.example.musicapp.domain.repository.MusicRepository
import com.example.musicapp.domain.exception.Result
import com.example.musicapp.domain.model.Album

class GetArtistAlbums(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(albumId: String): Result<List<Album>> = repository.getArtistAlbums(albumId)
}
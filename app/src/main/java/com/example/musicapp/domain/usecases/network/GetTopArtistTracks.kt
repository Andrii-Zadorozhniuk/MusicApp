package com.example.musicapp.domain.usecases.network

import com.example.musicapp.domain.repository.MusicRepository
import com.example.musicapp.domain.exception.Result
import com.example.musicapp.domain.model.Song

class GetTopArtistTracks(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(artistId: String): Result<List<Song>> = repository.getTopArtistTracks(artistId)
}
package com.example.musicapp.domain.usecases.network

import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.MusicRepository
import com.example.musicapp.domain.exception.Result
class SearchSongs(
    private val repository: MusicRepository
) {
    suspend operator fun invoke(query: String): Result<List<Song>> = repository.searchSongs(query)
}
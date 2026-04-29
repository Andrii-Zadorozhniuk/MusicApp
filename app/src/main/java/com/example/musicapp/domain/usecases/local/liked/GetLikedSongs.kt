package com.example.musicapp.domain.usecases.local.liked

import com.example.musicapp.domain.repository.MusicRepository

class GetLikedSongs(private val repository: MusicRepository) {
    operator fun invoke() = repository.getLikedSongs()
}
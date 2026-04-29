package com.example.musicapp.domain.usecases.local.liked

import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.MusicRepository

class LikeSong(private val repository: MusicRepository) {
    suspend operator fun invoke(song: Song) = repository.likeSong(song)
}
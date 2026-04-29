package com.example.musicapp.domain.usecases.local.liked

import com.example.musicapp.domain.repository.MusicRepository

class UnlikeSong(private val repository: MusicRepository) {
    suspend operator fun invoke(id: String) = repository.unlikeSong(id)
}
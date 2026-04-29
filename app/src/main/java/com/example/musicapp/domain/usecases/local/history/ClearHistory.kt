package com.example.musicapp.domain.usecases.local.history

import com.example.musicapp.domain.repository.MusicRepository

class ClearHistory(private val repository: MusicRepository) {
    suspend operator fun invoke() = repository.clearHistory()
}
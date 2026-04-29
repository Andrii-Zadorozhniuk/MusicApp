package com.example.musicapp.domain.usecases.local.history

import com.example.musicapp.domain.repository.MusicRepository

class GetHistory(private val repository: MusicRepository) {
    operator fun invoke() = repository.getHistory()
}
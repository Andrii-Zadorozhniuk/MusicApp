package com.example.musicapp.domain.usecases.local.search

import com.example.musicapp.domain.repository.MusicRepository

class ClearSearchHistory(private val repository: MusicRepository) {
    suspend operator fun invoke() = repository.clearSearchHistory()
}
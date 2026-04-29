package com.example.musicapp.domain.usecases.local.search

import com.example.musicapp.domain.repository.MusicRepository

class GetSearchHistory(private val repository: MusicRepository) {
    operator fun invoke() = repository.getSearchHistory()
}
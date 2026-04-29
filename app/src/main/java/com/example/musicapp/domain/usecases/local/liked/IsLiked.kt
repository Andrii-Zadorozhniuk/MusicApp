package com.example.musicapp.domain.usecases.local.liked

import com.example.musicapp.domain.repository.MusicRepository

class IsLiked(private val repository: MusicRepository) {
    operator fun invoke(id: String) = repository.isLiked(id)
}